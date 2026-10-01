package dev.mguevara.f2f3.companion.mixin;

import dev.mguevara.f2f3.companion.BuildPlacer;
import dev.mguevara.f2f3.companion.BuildPreview;
import dev.mguevara.f2f3.companion.Guide;
import dev.mguevara.f2f3.companion.Materials;
import dev.mguevara.f2f3.companion.Plans;
import dev.mguevara.f2f3.companion.compat.Gizmos;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Runs the build preview, the build placer, the guide and the plans each client tick, while per-tick gizmos are being collected. */
@Mixin(Minecraft.class)
public abstract class MinecraftMixin {
	@Inject(method = "tick()V", at = @At("TAIL"))
	private void f2f3$tick(CallbackInfo ci) {
		Minecraft mc = (Minecraft) (Object) this;
		Gizmos.beginTick();
		BuildPreview.tick(mc);
		BuildPlacer.tick(mc);
		Guide.tick(mc);
		Plans.tick(mc);
		Materials.tick(mc);
		Gizmos.endTick();
	}
}
