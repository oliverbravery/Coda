package net.oliverbravery.coda.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.util.Hand;
import net.oliverbravery.coda.Coda;
import net.oliverbravery.coda.config.Config;
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
        if(caughtFish && Boolean.parseBoolean(Config.GetValue("AutoFishEnabled", "true"))) {
            //Withdraw  rod
            client.interactionManager.interactItem(client.player, Hand.MAIN_HAND);
            Coda.LOGGER.info("Withdrew Rod");
            Coda.autoFish.RecastRod();
        }
    }
}
