package mod.master_bw3.packutis.mixin;

import invtweaks.events.ClientEvents;
import invtweaks.gui.InvTweaksButtonSort;
import net.minecraft.client.gui.components.Button;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(ClientEvents.class)
public class MixinSortButtonPosition {

    @Redirect(method = "onScreenEventInit", at = @At(value = "NEW", target = "Linvtweaks/gui/InvTweaksButtonSort;", ordinal = 0))
    private static InvTweaksButtonSort offsetY(int x, int y, Button.OnPress handler) {
        return new InvTweaksButtonSort(x, y - 16, handler);
    }
}
