package net.oliverbravery.coda.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.ItemStackArgumentType;
import net.minecraft.item.Item;
import net.oliverbravery.coda.features.WhitelistItemPickup;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class WhitelistItemPickupCommand {
    public static LiteralCommandNode register(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess) {
        return dispatcher.register(
                literal("coda")
                        .then(literal("itempickupwhitelist")
                                .then(literal("additem")
                                        .then(
                                                ClientCommandManager.argument("item", ItemStackArgumentType.itemStack(commandRegistryAccess))
                                                        .executes(WhitelistItemPickupCommand::AddItem)))
                                .then(literal("removeitem")
                                        .then(
                                                ClientCommandManager.argument("item",ItemStackArgumentType.itemStack(commandRegistryAccess))
                                                        .executes(WhitelistItemPickupCommand::RemoveItem)))
                                .then(literal("removeallitems")
                                        .executes(WhitelistItemPickupCommand::RemoveAllItem))
                                .then(literal("displayallitems")
                                        .executes(WhitelistItemPickupCommand::DisplayWhitelistItems))
                                .executes(WhitelistItemPickupCommand::Toggle)
                        )
        );
    }

    private static int Toggle(CommandContext<FabricClientCommandSource> context) {
        return WhitelistItemPickup.Toggle();
    }

    private static int AddItem(CommandContext<FabricClientCommandSource> context){
        Item i = ItemStackArgumentType.getItemStackArgument(context, "item").getItem();
        WhitelistItemPickup.AddItemToWhitelist(i);
        return 1;
    }

    private static int RemoveItem(CommandContext<FabricClientCommandSource> context) {
        Item i = ItemStackArgumentType.getItemStackArgument(context, "item").getItem();
        WhitelistItemPickup.RemoveItemFromWhitelist(i);
        return 1;
    }

    private static int RemoveAllItem(CommandContext<FabricClientCommandSource> context) {
        WhitelistItemPickup.RemoveAllItemsFromWhitelist();
        return 1;
    }

    private static int DisplayWhitelistItems(CommandContext<FabricClientCommandSource> context) {
        WhitelistItemPickup.DisplayWhitelistItems();
        return 1;
    }
}

