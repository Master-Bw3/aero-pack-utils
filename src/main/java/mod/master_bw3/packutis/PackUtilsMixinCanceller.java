package mod.master_bw3.packutis;

import com.bawnorton.mixinsquared.api.MixinCanceller;

import java.util.List;

public class PackUtilsMixinCanceller implements MixinCanceller {

    @Override
    public boolean shouldCancel(List<String> targetClassNames, String mixinClassName) {
        List<String> blockedMixins = List.of(
                "traben.flowing_fluids.mixin.mixins.MixinWaterPushing"
        );

        return blockedMixins.contains(mixinClassName);
    }
}
