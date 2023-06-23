package net.oliverbravery.coda.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket;
import net.minecraft.network.packet.s2c.play.SetTradeOffersS2CPacket;
import net.minecraft.village.TradeOfferList;
import net.oliverbravery.coda.features.LibrarianBookTrade;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(ClientConnection.class)
public class ClientConnectionMixin {
    @Inject(at = @At("HEAD"), method = "handlePacket", cancellable = true)
    private static <T extends PacketListener> void handlePacket(Packet<T> packet, PacketListener listener, CallbackInfo info) {
        if(packet instanceof SetTradeOffersS2CPacket && (LibrarianBookTrade.state == "SEARCH_TRADE")
                && LibrarianBookTrade.enabled) {
            String local_state = "SEARCH_TRADE";
            SetTradeOffersS2CPacket pkt = (SetTradeOffersS2CPacket)packet;
            TradeOfferList tol = pkt.getOffers();
            for (var trade:
                    tol) {
                ItemStack tradeItem = trade.getSellItem();
                if(tradeItem.getItem() instanceof EnchantedBookItem) {
                    Map<Enchantment, Integer> enchantment = EnchantmentHelper.get(tradeItem);
                    for (Map.Entry<Enchantment, Integer> entry : enchantment.entrySet()) {
                        if (entry.getKey().getTranslationKey().toUpperCase().contains(LibrarianBookTrade.wantedEnchant.getTranslationKey().toUpperCase())) {
                            Enchantment e = entry.getKey();
                            if (entry.getValue() == e.getMaxLevel()) {
                                //RIGHT BOOK
                                ItemStack emeraldPrice = trade.getAdjustedFirstBuyItem();
                                if(emeraldPrice.getCount() <= LibrarianBookTrade.maxEmeraldPrice) {
                                    local_state = "GOOD_TRADE";
                                }
                            }
                        }
                    }
                }
            }
            if(local_state != "GOOD_TRADE"){local_state = "BAD_TRADE";}
            if(MinecraftClient.getInstance() != null && MinecraftClient.getInstance().player != null) {
                info.cancel();
                MinecraftClient client = MinecraftClient.getInstance();
                client.send(() -> client.setScreen(LibrarianBookTrade.priorScreen));
                client.send(() -> client.getNetworkHandler().sendPacket(
                        new CloseHandledScreenC2SPacket(client.player.playerScreenHandler.syncId)));
                LibrarianBookTrade.state = local_state;
                return;
            }
        }
    }
}