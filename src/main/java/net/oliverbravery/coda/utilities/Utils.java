package net.oliverbravery.coda.utilities;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.text.Text;

public class Utils {
    public static boolean SHOULD_COMPENSATE_FOR_MODMENU_BUTTON = false;
    public static boolean SWITCHEROO_INSTALLED = false;
    public Utils() {
        for (var c: FabricLoader.getInstance().getAllMods()) {
            if(c.getMetadata().getId().equals("modmenu")) {
                SHOULD_COMPENSATE_FOR_MODMENU_BUTTON = true;
            }
            else if(c.getMetadata().getId().equals("switcheroo")) {
                SWITCHEROO_INSTALLED = true;
            }
        }
    }

    public static void SendChatMessage(String message) {
        ClientPlayerEntity cpe;
        if(MinecraftClient.getInstance().player != null) {
            cpe = MinecraftClient.getInstance().player;
            cpe.sendMessage(Text.literal(message), false);
        }
    }
}
