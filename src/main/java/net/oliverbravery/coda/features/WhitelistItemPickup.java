package net.oliverbravery.coda.features;

import net.minecraft.item.Item;
import net.oliverbravery.coda.utilities.Utils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WhitelistItemPickup {
    public static List<Item> whitelistItemList = new ArrayList<>(Arrays.asList(new Item[0]));
    public static boolean enabled;

    public static void AddItemToWhitelist(Item i) {
        if(whitelistItemList.contains(i)) {
            Utils.SendChatMessage(String.format("§6Item already exists within the whitelist"));
        }
        else {
            whitelistItemList.add(i);
            Utils.SendChatMessage(String.format("§6Successfully added item to the whitelist"));
        }
    }

    public static void RemoveItemFromWhitelist(Item i) {
        Boolean didRemove = whitelistItemList.remove(i);
        if(!didRemove){Utils.SendChatMessage(String.format("§6Could not find the item in the whitelist"));}
        else{Utils.SendChatMessage(String.format("§6Successfully removed item from the whitelist"));}
    }

    public static void RemoveAllItemsFromWhitelist() {
        whitelistItemList = new ArrayList<>(Arrays.asList(new Item[0]));
        Utils.SendChatMessage(String.format("§6Successfully removed all items from the whitelist"));
    }

    public static void DisplayWhitelistItems() {
        if(whitelistItemList.isEmpty()) {
            Utils.SendChatMessage(String.format("§6The item whitelist is empty"));
        }
        else {
            String allItems = "Whitelisted pickup items: ";
            for(int i = 0; i < whitelistItemList.size(); i++) {
                if(i+1 != whitelistItemList.size()) {
                    allItems += String.format("%s, ", whitelistItemList.get(i).asItem().toString());
                }
                else {
                    allItems += String.format("%s", whitelistItemList.get(i).asItem().toString());
                }
            }
            Utils.SendChatMessage(String.format("§6%s", allItems));
        }
    }

    public static int Toggle() {
        WhitelistItemPickup.enabled = !WhitelistItemPickup.enabled;
        Utils.SendChatMessage(String.format("§6Whitelist Item Pickup has been toggled to §6%s", WhitelistItemPickup.enabled));
        return 1;
    }
}
