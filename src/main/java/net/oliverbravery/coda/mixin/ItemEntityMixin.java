package net.oliverbravery.coda.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.oliverbravery.coda.features.WhitelistItemPickup;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin extends Entity {

    public ItemEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Shadow public abstract ItemStack getStack();
    @Shadow private int pickupDelay;

    @Inject(at = @At("HEAD"), method = "onPlayerCollision")
    public void onPlayerCollision(PlayerEntity player, CallbackInfo callbackInfo) {
        if(WhitelistItemPickup.enabled){
            ItemStack iStack = this.getStack();
            Item i = iStack.getItem();
            if(!WhitelistItemPickup.whitelistItemList.contains(i)) {
                this.pickupDelay = 1;
            }
        }
    }
}