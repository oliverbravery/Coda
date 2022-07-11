package net.oliverbravery.coda;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.inventory.Inventory;

public class Utils {
    public static boolean SHOULD_COMPENSATE_FOR_MODMENU_BUTTON = false;
    public Utils() {
        for (var c: FabricLoader.getInstance().getAllMods()) {
            if(c.getMetadata().getId().equals("modmenu")) {
                SHOULD_COMPENSATE_FOR_MODMENU_BUTTON = true;
            }
        }
    }
}
