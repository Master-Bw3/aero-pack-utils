package mod.master_bw3.packutis.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.momosoftworks.coldsweat.common.item.WaterskinItem;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluids;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import traben.flowing_fluids.FFFluidUtils;
import traben.flowing_fluids.FlowingFluids;

@Mixin(WaterskinItem.class)
public class MixinWaterSkin {

    @ModifyArg(
            method = "use",
            at = @At(value = "INVOKE", target = "Lcom/momosoftworks/coldsweat/common/item/WaterskinItem;getPlayerPOVHitResult(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/level/ClipContext$Fluid;)Lnet/minecraft/world/phys/BlockHitResult;"),
            index = 2
    )
    private ClipContext.Fluid packutils$allowAnyFluid(final ClipContext.Fluid par3) {
        if (FlowingFluids.config.enableMod
                && FlowingFluids.config.isWaterAllowed()
                && par3 == ClipContext.Fluid.SOURCE_ONLY) {
            return ClipContext.Fluid.ANY;
        }
        return par3;
    }

    @Inject(
            method = "use",
            at = @At(value = "INVOKE",
                    target = "Lcom/momosoftworks/coldsweat/common/item/WaterskinItem;handleFillWaterskin(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/InteractionHand;Lnet/minecraft/core/BlockPos;)V"),
            cancellable = true)
    private void packutils$drainWater(
            CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir,
            @Local(name = "itemstack") final ItemStack itemStack,
            @Local(argsOnly = true) final Level level, @Local(name = "hitPos") final BlockPos blockPos) {
        if (FlowingFluids.config.enableMod
                && FlowingFluids.config.isWaterAllowed()){
            int foundAmount = FFFluidUtils.collectConnectedFluidAmountAndRemove(level, blockPos, 2, 3, Fluids.WATER);
            if (foundAmount == 0) {
                cir.setReturnValue(InteractionResultHolder.pass(itemStack));
            }
        }
    }

    @ModifyExpressionValue(
            method = "use",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/material/FluidState;isSource()Z")
    )
    private boolean packutils$$allowNonSource(boolean original) {
        return (FlowingFluids.config.enableMod && FlowingFluids.config.isWaterAllowed()) || original;
    }
}
