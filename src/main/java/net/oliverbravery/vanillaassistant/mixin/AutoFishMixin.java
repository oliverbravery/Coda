package net.oliverbravery.vanillaassistant.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FishingBobberEntity.class)
public class AutoFishMixin {
    @Shadow private boolean caughtFish;
    @Inject(at = @At("TAIL"), method = "onTrackedDataSet")
    public void onTrackedDataSet(CallbackInfo info) {
        MinecraftClient client = MinecraftClient.getInstance();
        if(caughtFish && net.oliverbravery.vanillaassistant.VanillaAssistant.autoFish.autoFishEnabled) {
            //Withdraw  rod
            client.interactionManager.interactItem(client.player, Hand.MAIN_HAND);
            net.oliverbravery.vanillaassistant.VanillaAssistant.LOGGER.info("Withdrew Rod");
            net.oliverbravery.vanillaassistant.VanillaAssistant.autoFish.RecastRod();
        }
    }
}
