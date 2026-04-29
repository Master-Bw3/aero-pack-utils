package mod.master_bw3.packutis.mixin;

import net.minecraft.world.level.levelgen.DensityFunctions;
import org.spongepowered.asm.mixin.Mixin;


import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

import static net.minecraft.util.Mth.lerp;

@Mixin(DensityFunctions.ShiftedNoise.class)
abstract class MixinDensityFunction {
    @Final
    @Shadow
    private DensityFunction.NoiseHolder noise;

    @Shadow
    public abstract DensityFunction.NoiseHolder noise();

    @Unique
    private boolean packutils$isTempNoise;

    @Inject(
            method = "<init>",
            at = @At("TAIL")
    )
    private void onConstruct(DensityFunction densityFunction, DensityFunction densityFunction2, DensityFunction densityFunction3, double d, double e, DensityFunction.NoiseHolder noiseHolder, CallbackInfo ci) {
        Optional<ResourceKey<NormalNoise.NoiseParameters>> key = noiseHolder.noiseData().unwrapKey();
        if (key.isPresent()) {
            final String noiseType = key.get().location().getPath();
            if (noiseType.equals("temperature") || noiseType.equals("temperature_large")) {
                packutils$isTempNoise = true;
            }
        }
    }

    @WrapMethod(
            method = "compute"
    )
    private double onCompute(DensityFunction.FunctionContext context, Operation<Double> original) {

        double savannaZone =  packutils$distanceWithNoise(context.blockX(), context.blockY(),1200.0, 200.0);
        double desertZone =  packutils$distanceWithNoise(context.blockX(), context.blockY(), 3500.0, 500.0);
        double blendWidth = 400.0;

        if (packutils$isTempNoise) {
            double distance = packutils$getMagnitude(context.blockX(), context.blockZ());
            if (distance <= savannaZone) {
                return 0.4;
            } else if (distance <= desertZone + blendWidth) {
                double t = (distance - savannaZone) / blendWidth;
                t = Math.max(0.0, Math.min(1.0, t));
                t = (t * t) * (3 - 2 * t);

                return lerp(0.4, 0.9, t);
            } else {
                return -0.9;
            }
        } else {
            return original.call(context);
        }
    }

    @Unique
    private double packutils$getMagnitude(double x, double y) {
        return Math.sqrt(x * x + y * y);
    }

    @Unique
    private double packutils$distanceWithNoise(double x, double z, double distance, double variance) {
        double noise = this.noise.getValue(x, 0, z);
        return distance + noise * variance;
    }
}