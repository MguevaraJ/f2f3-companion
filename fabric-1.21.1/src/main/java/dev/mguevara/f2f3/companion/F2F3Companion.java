package dev.mguevara.f2f3.companion;

import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * F2+F3 Companion: when a screenshot is taken (F2), writes the exact game data next
 * to it ("name.f2f3.json") so the F2+F3 app can show coordinates, biome, mobs
 * and structures without relying on the F3 overlay or on image recognition.
 */
public final class F2F3Companion implements ClientModInitializer {
	public static final String MOD_ID = "f2f3_companion";
	public static final Logger LOG = LoggerFactory.getLogger("F2+F3 Companion");

	@Override
	public void onInitializeClient() {
		CompanionConfig.load();
		LOG.info("F2+F3 Companion ready: screenshots will get a .f2f3.json sidecar");
	}
}
