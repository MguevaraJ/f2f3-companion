# F2+F3 Companion

Mod de Fabric (solo cliente) para Minecraft que acompaña a **[F2+F3](https://github.com/MguevaraJ/f2f3)**, un gestor de capturas de pantalla de
Minecraft. Cada vez que haces una captura (F2), el mod guarda junto a ella los datos exactos del juego, y además lleva
parte de la app dentro del juego.

## Qué hace

- **Datos exactos de cada captura**: junto a cada `.png` escribe un `.craftshot.json` con coordenadas, dimensión,
  bioma, luz, bloque y entidad apuntados, mobs visibles, estructuras, reglas del juego, mob caps y más. Sin depender
  del F3 ni de reconocer la imagen.
- **Builds** (`Mayús+F2`): selecciona una construcción en tres pasos (fondo, ancho, altura), ponle nombre y se guarda
  como estructura vanilla (`.nbt`) junto a la captura y en el mundo (`/place template craftshot:<nombre>`).
- **Galería dentro del juego** (`F6`): tus capturas con todo lo que se sabe de ellas. Desde ahí:
  - **Guiarme**: una flecha en el HUD te lleva al lugar de la captura (`H` la oculta).
  - **Colocar build**: una caja naranja muestra dónde quedará; `Enter` lo coloca y se puede deshacer.
  - **Materiales**: fija a la derecha de la pantalla la lista de lo que necesita el build y la va marcando según tu
    inventario (`M` la oculta).
- **Planes del mapa en el mundo** (`J`): el punto AFK con sus esferas de 24 y 128 bloques, los cuadrados de
  simulación, las granjas y el enlace de portales que planificaste en la app. `Mayús+J` muestra los chunks slime.

## Versiones

| Minecraft | Carpeta          | Java |
| --------- | ---------------- | ---- |
| 26.3      | `fabric-26.3/`   | 25   |
| 1.21.1    | `fabric-1.21.1/` | 21   |
| 1.20.1    | `fabric-1.20.1/` | 17   |

Solo Fabric Loader; no necesita Fabric API (todos los ganchos son mixins). `fabric-26.3/` es la versión de referencia y
las otras dos son ports con una pequeña capa de compatibilidad (`compat/`).

## Compilar

```bash
cd fabric-26.3        # o fabric-1.21.1, fabric-1.20.1
./gradlew build       # → build/libs/craftshot-companion-1.0.0+<versión>.jar
./gradlew runClient   # cliente de prueba
```

Copia el `.jar` en la carpeta `mods/` de un perfil de Fabric de esa versión.

## Configuración

`config/craftshot_companion.json` (se crea solo): cuándo guardar builds (`build`: `sneak` / `always` / `never`), tamaño
inicial de la caja (`buildSize`) y las teclas (`galleryKey`, `guideKey`, `plansKey`, `materialsKey`).

## Licencia

MIT.
