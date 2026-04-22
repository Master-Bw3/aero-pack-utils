package mod.master_bw3.packutis.mixin;

import extendedrenderer.particle.entity.ParticleTexLeafColor;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ParticleTexLeafColor.class)
public class MixinLeafColor {

    @Redirect(method="<init>", at = @At(value = "INVOKE", target = "Lcom/corosus/coroutil/util/CoroUtilColor;getColors(Lnet/minecraft/world/level/block/state/BlockState;)[I"))
    private int[] eraseMethodCall(BlockState blockState)
    {
        return new int[]{};
    }
}
