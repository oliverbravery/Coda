package net.oliverbravery.coda.mixin;

import net.oliverbravery.coda.features.SlotRandomiser;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.ThreadLocalRandom;

@Mixin(InGameHud.class)
public abstract class HotbarRandomiseMixin extends DrawableHelper {
    @Unique
    private static PlayerInventory inventory;
    private static int delay = 1;
    @Inject(method = "tick", at = @At("RETURN"))
    public void tick(final CallbackInfo info) {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if(player != null) {
            inventory = player.getInventory();
            if(SlotRandomiser.randomiseSlotsActive && !SlotRandomiser.randomiseSlotKeyList.isEmpty()) {
                if(delay == 0) {
                    int rnd = ThreadLocalRandom.current().nextInt(SlotRandomiser.randomiseSlotKeyList.size());
                    inventory.selectedSlot = SlotRandomiser.randomiseSlotKeyList.get(rnd);
                    delay = 1;
                }
                else {
                    delay--;
                }
            }
        }
    }
}
