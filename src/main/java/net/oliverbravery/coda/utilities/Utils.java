package net.oliverbravery.coda.utilities;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.Text;

import java.util.List;

public class Utils {
    public static boolean SHOULD_COMPENSATE_FOR_MODMENU_BUTTON = false;
    public static boolean SWITCHEROO_INSTALLED = false;

    public static void Initialize() {
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

    public static void SendActionBarMessage(String message) {
        ClientPlayerEntity cpe;
        if(MinecraftClient.getInstance().player != null) {
            cpe = MinecraftClient.getInstance().player;
            cpe.sendMessage(Text.literal(message), true);
        }
    }

    public static List RemoveItemFromList(List list, String itemToRemove) {
        for (int i = 0; i < list.size(); i++) {
            if(itemToRemove.equals(list.get(i).toString())) {
                list.remove(i);
            }
        }
        return list;
    }

}
