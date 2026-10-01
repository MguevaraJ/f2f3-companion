package dev.mguevara.f2f3.companion;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.Minecraft;

/**
 * The mod's texts are written in Spanish; English is this dictionary, keyed by the Spanish
 * text. The game's language decides: Spanish for any Spanish, English for the rest. A text
 * without translation stays in Spanish.
 */
public final class Tr {
	private static final Map<String, String> EN = new HashMap<>();

	private Tr() {}

	public static String t(String spanish) {
		return english() ? EN.getOrDefault(spanish, spanish) : spanish;
	}

	private static boolean english() {
		Minecraft mc = Minecraft.getInstance();
		String code = mc == null || mc.options == null ? null : mc.options.languageCode;
		return code == null || !code.startsWith("es");
	}

	static {
		EN.put(" · baja ", " · go down ");
		EN.put(" · bloque ", " · block ");
		EN.put(" · en ", " · in ");
		EN.put(" · Esc cancela", " · Esc cancels");
		EN.put(" · Esc vuelve al ancho", " · Esc goes back to the width");
		EN.put(" · Esc vuelve al fondo", " · Esc goes back to the depth");
		EN.put(") · F2 guardar · ", ") · F2 save · ");
		EN.put(") · F2 siguiente · ", ") · F2 next · ");
		EN.put(") · gírate para rotarlo · Esc cancela", ") · turn around to rotate it · Esc cancels");
		EN.put(" · hacia el ", " · facing ");
		EN.put(" · portal en ", " · portal at ");
		EN.put(" · sube ", " · go up ");
		EN.put(" a la izquierda", " to the left");
		EN.put(": apunta al bloque de la esquina", ": aim at the corner block");
		EN.put(" bloques", " blocks");
		EN.put(" bloques de ti", " blocks from you");
		EN.put("A ", "");
		EN.put(" captura", " screenshot");
		EN.put(" capturas", " screenshots");
		EN.put("★ Favorita", "★ Favorite");
		EN.put(" hacia el fondo × ", " deep × ");
		EN.put(" la oculta)", " hides it)");
		EN.put(" más", " more");
		EN.put("✔ Materiales listos", "✔ Materials ready");
		EN.put(" y el build aparecerá en el mismo sitio respecto a ti.", " and the build will appear in the same place relative to you.");
		EN.put("a ras", "level");
		EN.put("Apunta a la base del build", "Aim at the base of the build");
		EN.put("Apunta a la base del build para fijar la caja", "Aim at the base of the build to pin the box");
		EN.put("Apunta a la base del build para guardarlo (Esc cancela)", "Aim at the base of the build to save it (Esc cancels)");
		EN.put("Apunta al bloque donde irá la esquina del build", "Aim at the block where the build's corner will go");
		EN.put("Apuntando a", "Targeting");
		EN.put("Bioma", "Biome");
		EN.put("Bloque", "Block");
		EN.put("Bloques", "Blocks");
		EN.put("Build colocado · se puede deshacer desde la galería", "Build placed · it can be undone from the gallery");
		EN.put("Build guardado: ", "Build saved: ");
		EN.put("Build guardado en el mundo como ", "Build saved in the world as ");
		EN.put("Captura", "Screenshot");
		EN.put("Cargando…", "Loading…");
		EN.put("Chunk slime", "Slime chunk");
		EN.put("Chunks slime ocultos", "Slime chunks hidden");
		EN.put("Chunks slime visibles", "Slime chunks visible");
		EN.put("cielo ", "sky ");
		EN.put("Clima", "Weather");
		EN.put("Coloca el build guardado con esta captura", "Places the build saved with this screenshot");
		EN.put("Colocación cancelada", "Placement cancelled");
		EN.put("Colocación deshecha", "Placement undone");
		EN.put("Colocar ", "Place ");
		EN.put("Colocar build", "Place build");
		EN.put("Coordenadas", "Coordinates");
		EN.put("Copia las coordenadas de la captura al portapapeles", "Copies the screenshot's coordinates to the clipboard");
		EN.put("Copiadas", "Copied");
		EN.put("[Copiar comando]", "[Copy command]");
		EN.put("Copiar XYZ", "Copy XYZ");
		EN.put("Deja de señalar el lugar de la captura", "Stops pointing at the screenshot's place");
		EN.put("Deshace la última colocación de un build", "Undoes the last build placement");
		EN.put("Deshacer", "Undo");
		EN.put("Despejado", "Clear");
		EN.put("Destino ideal de A", "Ideal destination of A");
		EN.put("Día", "Day");
		EN.put("Dimensión", "Dimension");
		EN.put("Elige una captura", "Choose a screenshot");
		EN.put("En el punto AFK", "At the AFK spot");
		EN.put("Enter coloca · Rueda agachado: sube o baja (", "Enter places · Wheel while sneaking: up or down (");
		EN.put("Esc cancela", "Esc cancels");
		EN.put("Ese build no necesita materiales", "That build needs no materials");
		EN.put("Esta captura es de otro mundo (", "This screenshot is from another world (");
		EN.put("Esta captura no tiene coordenadas", "This screenshot has no coordinates");
		EN.put("Este", "East");
		EN.put("Norte", "North");
		EN.put("Sur", "South");
		EN.put("Oeste", "West");
		EN.put("este", "east");
		EN.put("norte", "north");
		EN.put("sur", "south");
		EN.put("oeste", "west");
		EN.put("fija", "pins");
		EN.put("suelta", "releases");
		EN.put("Estructuras", "Structures");
		EN.put("Fija en pantalla lo que necesita este build y lo va tachando con tu inventario", "Pins what this build needs on screen and ticks it off against your inventory");
		EN.put("FIJADA · ", "PINNED · ");
		EN.put("SUELTA · ", "FREE · ");
		EN.put("Guardado del build cancelado", "Build saving cancelled");
		EN.put("Guardar", "Save");
		EN.put("GUÍA", "GUIDE");
		EN.put("Guía activada: sigue la flecha (", "Guide on: follow the arrow (");
		EN.put("Guía oculta", "Guide hidden");
		EN.put("Guía visible", "Guide visible");
		EN.put("Guiarme", "Guide me");
		EN.put("Has llegado al lugar de la captura", "You have reached the screenshot's place");
		EN.put("Lista de materiales fijada (", "Materials list pinned (");
		EN.put("Lista de materiales oculta", "Materials list hidden");
		EN.put("Lista de materiales visible", "Materials list visible");
		EN.put("Lluvia", "Rain");
		EN.put("Luz", "Light");
		EN.put("Los chunks slime solo están en el Overworld", "Slime chunks only exist in the Overworld");
		EN.put("Materiales", "Materials");
		EN.put("Materiales ", "Materials ");
		EN.put("Mirando al", "Facing");
		EN.put("Modo", "Mode");
		EN.put("Mundo", "World");
		EN.put("\nEjecútalo mirando al ", "\nRun it facing ");
		EN.put("No hay capturas", "No screenshots");
		EN.put("No hay capturas de este mundo", "No screenshots of this world");
		EN.put("No hay planes de la app para «", "No plans from the app for «");
		EN.put("No se pudo leer el build de esa captura", "Could not read that screenshot's build");
		EN.put("Nombre", "Name");
		EN.put("Nombre del build", "Build name");
		EN.put("Nombre del build (", "Build name (");
		EN.put("Partida", "Game");
		EN.put("Paso 1 de 3: el fondo · ", "Step 1 of 3: depth · ");
		EN.put("Paso 2 de 3: el ancho · ", "Step 2 of 3: width · ");
		EN.put("Paso 3 de 3: la altura · Build ", "Step 3 of 3: height · Build ");
		EN.put("Planes ocultos", "Plans hidden");
		EN.put("Planes visibles", "Plans visible");
		EN.put("Posición", "Position");
		EN.put("Punto AFK a ", "AFK spot at ");
		EN.put("Quita la lista de materiales de la pantalla", "Removes the materials list from the screen");
		EN.put("Quitar guía", "Remove guide");
		EN.put("Quitar lista", "Remove list");
		EN.put("Rueda agachado: altura (", "Wheel while sneaking: height (");
		EN.put("Rueda agachado: ancho (", "Wheel while sneaking: width (");
		EN.put("Rueda agachado: fondo (", "Wheel while sneaking: depth (");
		EN.put("Se guardará como f2f3:", "It will be saved as f2f3:");
		EN.put("Semilla", "Seed");
		EN.put("Sin datos: no se hizo con el mod y la app aún no la ha analizado.", "No data: it was not taken with the mod and the app has not analyzed it yet.");
		EN.put("Sin semilla: escríbela en el mapa de la app y envía un plan", "No seed: type it in the app's map and send a plan");
		EN.put("Sin slimes en este chunk", "No slimes in this chunk");
		EN.put("Solo en mundos de un jugador", "Singleplayer worlds only");
		EN.put("Solo este mundo", "This world only");
		EN.put("Todos los mundos", "All worlds");
		EN.put("Tamaño", "Size");
		EN.put("Tormenta", "Thunderstorm");
		EN.put("Ubicación", "Location");
		EN.put("Una flecha te lleva al lugar de esta captura", "An arrow leads you to this screenshot's place");
		EN.put("Vacío = build_1, build_2…", "Empty = build_1, build_2…");
		EN.put("Ve a esa dimensión para seguir la guía", "Go to that dimension to follow the guide");
		EN.put("Volver", "Back");
		EN.put("Ya llevas todos los materiales de ", "You carry all the materials for ");
	}
}
