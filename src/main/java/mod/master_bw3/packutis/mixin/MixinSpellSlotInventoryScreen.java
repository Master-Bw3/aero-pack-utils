package mod.master_bw3.packutis.mixin;

import com.bawnorton.mixinsquared.TargetHandler;
import dev.enjarai.trickster.screen.SpellSlotWidget;
import net.minecraft.client.gui.layouts.LayoutElement;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.InventoryMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value = InventoryScreen.class, priority = 1500)
public abstract class MixinSpellSlotInventoryScreen extends EffectRenderingInventoryScreen<InventoryMenu> {

    public List<SpellSlotWidget> spellSlots;

    public MixinSpellSlotInventoryScreen(InventoryMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @TargetHandler(
            mixin = "dev.enjarai.trickster.mixin.client.InventoryScreenMixin",
            name = "initSpellSlots"
    )
    @Redirect(
            method = "@MixinSquared:Handler",
            at = @At(
                    value = "NEW",
                    target = "(III)Ldev/enjarai/trickster/screen/SpellSlotWidget;"
            )
    )
    private SpellSlotWidget topSpellSlots(int x, int y, int i) {
        return new SpellSlotWidget(x, this.topPos - 16, i);
    }

    @TargetHandler(
            mixin = "dev.enjarai.trickster.mixin.client.InventoryScreenMixin",
            name = "tickSpellSlots"
    )
    @Inject(
            method = "@MixinSquared:Handler",
            at = @At("TAIL")
    )
    private void topSpellSlotsTick(CallbackInfo ci) {
        for (var slot : spellSlots) {
            ((LayoutElement) ((Object) slot)).setPosition(leftPos + 156 - slot.index * 18, topPos - 16);
        }
    }
}
