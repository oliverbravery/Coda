package net.oliverbravery.coda.features;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.inventory.Inventory;
import net.minecraft.text.Text;
import net.oliverbravery.coda.utilities.InventoryManipulator;
import org.lwjgl.glfw.GLFW;

import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

public class SortInventory {
    private static String filePath = "savedInventory.txt";
    public static KeyBinding saveInventoryKeybind;
    public static KeyBinding sortInventoryKeybind;

    public static void Initialize() {
        CreateTextFile();
    }

    private static void CreateTextFile() {
        File textFile = new File(filePath);
        {
            try {
                textFile.createNewFile();
            }
            catch(Exception e) {}
        }
    }

    private static void WriteToTextFile(String content) {
        try {
            FileWriter myWriter = new FileWriter(filePath);
            myWriter.write(content);
            myWriter.close();
        }
        catch(Exception e) {}
    }

    public static void AddInventory() {
        //save current inventory
        MinecraftClient mc = MinecraftClient.getInstance();
        Inventory inv;
        if(mc.player != null) {
            inv = mc.player.getInventory();
            String inventoryText = "";
            for (int i = 0; i < inv.size(); i++) {
                if(!inv.getStack(i).getItem().toString().contains("air")) {
                    if(i+1 == inv.size()) {
                        inventoryText += String.format("%s#%s",i, inv.getStack(i).getItem());
                    }
                    else {
                        inventoryText += String.format("%s#%s,",i, inv.getStack(i).getItem());
                    }
                }
            }
            WriteToTextFile(inventoryText);
        }
        mc.player.sendMessage(Text.literal("§aSuccessfully saved the inventory."), false);
    }

    public static void LoadInventory() {
        String inventoryToLoad = "";
        MinecraftClient mc = MinecraftClient.getInstance();
        if(mc.player != null) {
            Inventory inv = mc.player.getInventory();
            try {
                File myObj = new File(filePath);
                Scanner myReader = new Scanner(myObj);
                while (myReader.hasNextLine()) {
                    inventoryToLoad = myReader.nextLine();
                }
                myReader.close();
            }
            catch(Exception e){}
            if(inventoryToLoad != "") {
                String[] items = inventoryToLoad.split(",");
                for(var i : items) {
                    boolean found = false;
                    String x = i;
                    String[] thisItem = x.split("#");
                    int itemIndex = Integer.parseInt(thisItem[0]);
                    String itemName = thisItem[1];
                    for(int j = 0; j < inv.size() && !found; j++) {
                        String itemNameInInv = inv.getStack(j).getItem().toString();
                        if(itemNameInInv.equals(itemName)) {
                            if(j != itemIndex) {
                                //Swap to put in correct place
                                if(itemIndex >= 0 && itemIndex <= 8) {
                                    itemIndex += 36;
                                }
                                else if (itemIndex > 35 && itemIndex < 40){
                                    if(itemIndex == 36) {itemIndex -= 28;}
                                    else if(itemIndex == 37){itemIndex -= 30;}
                                    else if(itemIndex == 38){itemIndex -= 32;}
                                    else if(itemIndex == 39){itemIndex -= 34;}
                                }
                                else if (itemIndex == 40) {
                                    itemIndex = 45;
                                }
                                InventoryManipulator.swapSlots(j, itemIndex);
                                found = true;
                            }
                        }
                    }
                }
            }
        }
        mc.player.sendMessage(Text.literal("§aSuccessfully sorted the inventory."), false);
    }

    public static void SetupKeybinds() {
        saveInventoryKeybind = KeyBindingHelper.registerKeyBinding(
                new KeyBinding(
                        "key.coda.saveinventory",
                        InputUtil.Type.KEYSYM,
                        GLFW.GLFW_KEY_P,
                        "Coda"
                ));

        sortInventoryKeybind = KeyBindingHelper.registerKeyBinding(
                new KeyBinding(
                        "key.coda.sortinventory",
                        InputUtil.Type.KEYSYM,
                        GLFW.GLFW_KEY_U,
                        "Coda"
                ));
    }

    public static void KeybindCheck() {
        if(SortInventory.sortInventoryKeybind.wasPressed()) {
            SortInventory.LoadInventory();
        }
        if(SortInventory.saveInventoryKeybind.wasPressed()) {
            SortInventory.AddInventory();
        }
    }
}
