# F2+F3 Companion — contexto para agentes

Mod de Fabric (solo cliente) para Minecraft que acompaña a la app de escritorio **F2+F3** (repo aparte, en
`../craftshot`): guarda junto a cada captura (F2) los datos exactos del juego y lleva la app dentro del juego (galería,
guía, builds, planes del mapa, lista de materiales). **Textos del juego en español. Código y comentarios en inglés.**

Tres proyectos Gradle independientes con el mismo código: `fabric-26.3/` (el original), `fabric-1.21.1/` y
`fabric-1.20.1/` (ports). **Un cambio hay que repetirlo en los tres.** Cada uno tiene su cliente de prueba en `run/`
(ignorado por git).

## Comandos
```bash
cd fabric-26.3 && ./gradlew build -q        # jar en build/libs/craftshot-companion-1.0.0+<versión>.jar
./gradlew runClient --args='--quickPlaySingleplayer "New World (1)"'    # 26.3; en los ports el mundo es "Mundo nuevo"
```
La app incluye los jars: desde `../craftshot`, `npm run build:mod` compila los tres y los copia a su `resources/`
(con los clientes de prueba CERRADOS). Los fixtures reales de sidecars y `.nbt` viven en los tests de la app
(`tests/fixtures/companion/`): si cambia lo que escribe el mod, regenerarlos allí.
Commits: autor "Moises Guevara <mguevaraj27@gmail.com>", terminar con `Co-Authored-By: Claude Opus 5.5 <noreply@anthropic.com>`.
**Se conservan a propósito** los identificadores internos "craftshot" (id del mod `craftshot_companion`, paquete Java,
sufijos `.craftshot.json` / `.craftshot.nbt`, carpeta `craftshot/`, espacio `craftshot:` de las plantillas): la app
depende de ellos.

## El mod (26.3 es la referencia)
Gradle 9.7.1 con wrapper, plugin `net.fabricmc.fabric-loom` 1.18-SNAPSHOT, Java release 25 (JDK 26 y 21 instalados),
loader 0.19.5. **Sin dependencia de fabric-api: todos los ganchos son mixins.** Entrypoint
`dev.mguevara.craftshot.companion.CraftshotCompanion`, mixins en `craftshot_companion.mixins.json`.
Tras cambiarlo: `npm run build:mod`; el usuario lo reinstala desde Ajustes › "Guardar el mod".

### Contrato JSON del sidecar (schema 1) — mantenerlo estable, es la API entre mod y app
```json
{ "format": "craftshot-companion", "schema": 1,
  "mod": { "name": "F2+F3 Companion", "version": "1.0.0+26.3", "loader": "fabric", "minecraft": "26.3" },
  "capturedAt": "2026-09-29T15:32:10.123Z",
  "world": { "type": "singleplayer|multiplayer|realms", "name": "…", "seed": "123", "dimension": "minecraft:overworld",
             "day": 12, "timeOfDay": 6000, "weather": "clear|rain|thunder" },
  "player": { "position": {"x":0,"y":0,"z":0}, "block": {…}, "chunk": {…},
              "facing": {"direction":"north","yaw":-151.8,"pitch":6.4}, "gameMode": "survival" },
  "biome": "minecraft:plains", "light": {"sky":15,"block":0},
  "target": { "block": {"id":"minecraft:stone","pos":{…}}, "entity": {"id":"minecraft:zombie","distance":3.2} },
  "entities": [ {"id":"minecraft:cow","count":3,"nearest":12.5} ],
  "structures": { "inside": ["minecraft:village_plains"], "target": [] },
  "build": { "file": "….craftshot.nbt", "origin": {…}, "size": {…}, "blocks": 173, "entities": 0, "template": "craftshot:casa" } }
```
`seed` solo en un jugador y **como string**; `structures` ausente = desconocido (multijugador). `entities` = seres vivos
visibles (dentro del campo de visión y con línea de visión, ≤96 bloques). Más los campos opcionales de la fase 3.

### Clases
- `SnapshotCollector` / `CaptureSnapshot` / `ServerCollector` (hilo del servidor) / `SidecarWriter` (cola FIFO; escribe
  el `.nbt` antes del JSON, en `Util.ioPool()`, tmp + move atómico).
- `CompanionConfig` — `config/craftshot_companion.json`: `build` "sneak" | "always" | "never"; `buildSize` (16, 3–97;
  `buildRadius` antiguo = 2r+1); `buildBase` "corner" | "center"; `galleryKey` ("f6"); `guideKey` ("h"); `plansKey` ("j"); `materialsKey` ("m"). Las teclas son
  el nombre tras `key.keyboard.`; se detectan en el mixin de `KeyboardHandler` (sin KeyMapping, no salen en Controles).
- `BuildRegion.corner(level, target, depth, width, facing, height)`: el bloque apuntado es la esquina inferior cercana
  derecha; la caja crece `depth` hacia donde mira el jugador y `width` a su izquierda; `height` 0 = hasta el bloque más
  alto (máx. 97).
- `BuildPreview` — **Mayús+F2**, tres pasos con la rueda agachado y F2 para avanzar: 1 fondo, 2 ancho (la caja queda
  fijada al entrar), 3 altura; luego `BuildNameScreen` (nombre → `slug()` `[a-z0-9_.-]`; vacío → `build_N`; repetido →
  `_2`…), captura retrasada 2 ticks. En los pasos 1 y 2 la caja se dibuja de 1 bloque de alto. Enter fija/suelta, Esc
  retrocede un paso o cancela (sin abrir el menú de pausa); abrir chat o inventario cancela. Rayo propio de 96 bloques
  (`player.pick`). Texto con `drawLines` (ajustado al ancho con `font.split`) desde el mixin del HUD.
  El build se guarda también en el mundo: `getStructureTemplateManager().getOrCreate(craftshot:<nombre>)` + `save(id)`
  (`generated/craftshot/structure/`, singular), y el chat da "[Copiar comando]".
- `GalleryScreen` (**F6**): cuadrícula + panel (imagen al 60 %, datos en un recuadro oscuro con barra de scroll,
  botones de 16 px en dos columnas: "Guiarme", "Copiar XYZ", "Colocar build", "Materiales", "Deshacer", con tooltips). Filtro "Solo
  este mundo" / "Todos los mundos". `CaptureIndex` recorre `screenshots/` (4 niveles) fuera del hilo cliente: usa el
  `app-index.json` de la app y, si no hay, el propio sidecar (nombres con `Language.getInstance()`).
  `Thumbnails`: `NativeImage.read` + `resizeSubRectTo` (480 px), `DynamicTexture`, LRU de 96, se liberan en `removed()`.
- `Guide`: flecha en el HUD (escala 0,7; `g.pose()` translate + rotate), distancia y "sube/baja N". En el mundo solo un
  punto (`Gizmos.point`, a 48 bloques como mucho en esa dirección: los gizmos lejanos no se dibujan); a ≤24 bloques,
  además el bloque y una línea desde los pies. **H** oculta/muestra. Se borra al llegar (≤3 bloques en horizontal y ≤12
  de altura) o al salir del mundo. Entre Overworld y Nether apunta a las coordenadas equivalentes (no probado en juego).
- `Plans`: lee `craftshot/plans.json` (lo relee cada 40 ticks si cambió, fuera del hilo cliente) y dibuja el plan del
  mundo actual (mismo nombre que usa la galería): punto AFK, esferas de 24 y 128 (tres círculos máximos + el corte a la
  altura de los pies, más grueso), cuadrados de simulación y de solo bloques, granjas con su color, portales A/B y el
  cuadrado de búsqueda. **J** oculta/muestra los planes; **Mayús+J** los chunks slime (±4 chunks, solo Overworld;
  `WorldgenRandom.seedSlimeChunk`; semilla del servidor integrado o la `seed` del plan). Línea en el HUD a la izquierda.
  Probado en juego: AFK, granjas, HUD y recarga del archivo; portales y relleno de chunks slime sin ver en pantalla.
- `Materials`: lista de materiales de un build fijada en el HUD (derecha, centrada en altura, escala 0,75), al estilo
  de la lista de Satisfactory. Botón "Materiales" / "Quitar lista" de la galería. Lee `palette` + `blocks` del `.nbt`
  (bloque → `asItem()`; sin ítem se ignora; losa doble = 2; mitad superior de puertas/plantas y cabecera de cama = 0),
  recuenta el inventario cada 5 ticks, pendientes arriba y completos en verde, máx. 10 filas ("+N más"), aviso al
  tenerlo todo. **M** oculta/muestra. No mira dentro de shulkers ni bundles. Probado en juego.
- `BuildPlacer` (solo un jugador): carga el `.craftshot.nbt` (`StructureTemplate.load(BuiltInRegistries.BLOCK, tag)`),
  caja naranja con la misma convención de esquina, girada por cuartos según hacia dónde mira el jugador ahora frente a
  la captura. Enter coloca en el hilo del servidor con `placeInWorld` (no usa `/place`: funciona sin trucos); rueda
  agachado sube/baja; Esc cancela. Ancla por rotación: 90° → min+(sz−1,0,0); 180° → min+(sx−1,0,sz−1); 270° →
  min+(0,0,sx−1) (probado en juego S/E/O). Antes guarda la zona con `fillFromWorld` para "Deshacer" (solo la última;
  las entidades colocadas no se quitan).
- Mixins: `ScreenshotMixin` (`grab(Minecraft,boolean)` HEAD cancelable + `getFile` RETURN), `MinecraftMixin`
  (`tick()` TAIL: vista previa, colocador y guía, dentro de `collectPerTickGizmos`), `MouseHandlerMixin` (`onScroll`),
  `KeyboardHandlerMixin` (`keyPress` HEAD, acción 1 = pulsación), `HudMixin` (`Hud.extractRenderState` TAIL).

### Port a 1.21.1 (`fabric-1.21.1/`, probado en juego: sidecar, builds, galería, materiales, planes, colocador)
Copia del código de 26.3 adaptada; **un cambio en el mod hay que hacerlo en los tres proyectos**. Loom
`net.fabricmc.fabric-loom-remap` + `loom.officialMojangMappings()`, Java 21, mismo id de mod y mismo contrato JSON.
Para tocar lo mínimo, `compat/` imita las clases de 26.3: `Gizmos` / `GizmoStyle` / `TextGizmo` (formas por tick que
`LevelRendererMixin` dibuja al final de `renderLevel` con Tesselator: quads `position_color`, líneas `rendertype_lines`
troceadas a 16 bloques, texto `drawInBatch`; `MinecraftMixin` llama `beginTick`/`endTick`), `GuiGraphicsExtractor`
(envuelve `GuiGraphics`), `KeyEvent`, `RenderPipelines`. Diferencias: `ResourceLocation`, `mc.screen`/`mc.setScreen`,
`Gui.render`, `Screen.render` (la galería pinta el fondo primero y luego los widgets a mano), `keyPress(JIIII)V`,
F2 = `Screenshot.grab(File, RenderTarget, Consumer)`, NBT clásico (`getList(k, 10)`), `GameRules.visitGameRuleTypes`
(ids convertidos a `minecraft:snake_case`; los que 26.x renombró no coinciden con `KEY_RULES`), `getDayTime`,
`registryOrThrow`, `server.getStructureManager()`, `fillFromWorld(..., Block)`. Mundo de prueba: `Mundo nuevo`
(`./gradlew runClient --args='--quickPlaySingleplayer "Mundo nuevo"'`); javap contra el jar con nombres:
`~/.gradle/caches/fabric-loom/minecraftMaven/net/minecraft/minecraft-merged/1.21.1-*/…jar`.
App: `shared/modVersions.ts` (`MOD_VERSIONS`), `components/SaveModButton.tsx` (selector de versión + botón, en
Ajustes y onboarding), `companion.saveMod(version)`, jars `resources/craftshot-companion.jar` (26.3) y
`craftshot-companion-<versión>.jar`. Fixtures `tests/fixtures/companion/build-1.21.1.*` y `build-1.20.1.*`.

### Port a 1.20.1 (`fabric-1.20.1/`, probado en juego igual que el de 1.21.1)
Copia del de 1.21.1 con Java 17. Diferencias: `Gui.render(GuiGraphics, float)`, `LevelRenderer.renderLevel(PoseStack,
…)` (el modelview es `PoseStack`; `BufferBuilder.begin/vertex/endVertex`, `endOrDiscardIfEmpty`), texto de `Gizmos`
con escala X negativa (la rotación de cámara mira al revés antes de 1.21), `Screen.renderBackground(GuiGraphics)` no lo
llama `render` (BuildNameScreen pinta su panel a mano), `mouseScrolled` de 3 argumentos, `new ResourceLocation`,
`NbtIo` con `File`, sin `/tick` (tick fijo 20 / "normal", `getAverageTickTime`), `mc.player.getServerBrand()`,
`mc.isConnectedToRealms()`, encantamientos con `EnchantmentHelper.getEnchantments`, builds del mundo en
`generated/craftshot/structures/` (plural). La app elige `structure` o `structures` al instalar una plantilla según
el `DataVersion` del `level.dat` (< 3953 = anterior a 1.21). Mundo de prueba: `Mundo nuevo`.

### APIs de 26.3 (sin ofuscar; verificar con `javap -p -cp ~/.minecraft/versions/26.3/26.3.jar <clase>`)
- `Identifier` (no ResourceLocation): `fromNamespaceAndPath`, `isValidPath`; `ResourceKey.identifier()`.
- Pantallas: `mc.gui.screen()` / `mc.gui.setScreen()` (no `mc.screen`); HUD `net.minecraft.client.gui.Hud`
  (`extractRenderState(GuiGraphicsExtractor, DeltaTracker)`); `Options.hideGui` no existe.
  `Screen.extractRenderState` / `extractBackground`; `mouseClicked(MouseButtonEvent, boolean)`,
  `mouseScrolled(x, y, dx, dy)`, `keyPressed(KeyEvent)`; `KeyEvent.isConfirmation()` / `isEscape()` / `key()`.
- `GuiGraphicsExtractor`: `text`, `centeredText`, `fill`, `outline(x, y, w, h, color)`, `enableScissor`, `pose()`
  (Matrix3x2fStack), `blit(RenderPipelines.GUI_TEXTURED, id, x, y, 0, 0, w, h, w, h)`, `guiWidth()` / `guiHeight()`.
- `Gizmos.cuboid` / `line` / `point` / `billboardText` + `.setAlwaysOnTop()`; `GizmoStyle.fill` / `stroke`.
- `Screenshot.grab(Minecraft, boolean)` es la entrada de F2; `KeyboardHandler.keyPress(long, int, KeyEvent)`,
  `setClipboard`; `MouseHandler.onScroll(long, double, double)`; `player.sendOverlayMessage` / `sendSystemMessage`;
  `ClickEvent.CopyToClipboard`, `HoverEvent.ShowText`.
- `Level.getOverworldClockTime()` (no hay `getDayTime`); servidor integrado: `mc.getSingleplayerServer()`,
  `server.execute/submit`, `getLevel(dim)`, `getStructureTemplateManager()`; `StructureTemplate.fillFromWorld(level,
  pos, size, entities, List<Block>)`, `placeInWorld(level, pos, pos, StructurePlaceSettings, random, 2)`; la paleta de
  las estructuras usa `id` en 26.x (antes `Name`); `/place` cachea las búsquedas fallidas mientras el mundo está abierto.


## Pruebas
- Mod: `cd fabric-26.3 && ./gradlew runClient --args='--quickPlaySingleplayer "New World (1)"'` (~70 s). `New World
  (1)` tiene trucos; `New World` no; `CraftshotTest` ya no carga. La ventana se busca con `xdotool search --class
  Minecraft` (la titulada "…Singleplayer"). Poner `pauseOnLostFocus:false` en `run/options.txt` mientras se prueba.
  `pkill -f '[j]ava.*Knot'` **en un comando aparte** (si la línea contiene "java…Knot" se mata la propia shell).
  No ejecutar `build:mod` con el cliente abierto. Mover el ratón con xdotool gira la cámara al cerrar una pantalla; un
  Esc sin pantalla abierta abre el menú de pausa; en espectador Mayús hace descender. Al leer `mc.log`, recortar líneas
  (`cut -c1-200`): hay una de varios KB.
- El usuario usa **i3**: medir con `xdotool getwindowgeometry` antes de hacer clic, comprobar
  `xdotool getactivewindow getwindowname` antes de teclear. Captura: `import -window <id>`.
- Preferencia del usuario: **ahorrar tokens** — pocas capturas (recortadas), salidas filtradas, mensajes breves.
- Nunca instalar el jar por cuenta propia en una carpeta `mods` del usuario.

## Pendientes
- Ports 1.21.1 y 1.20.1 sin probar: multijugador, guía, deshacer, villagers/cofres apuntados, gráficos "Fabulosos".
- Abrir chat/inventario cancela la selección y la colocación de builds aunque estén fijadas.
- Sin probar en real: guía entre dimensiones, galería en multijugador.
- No hay port a Forge.
