package net.oliverbravery.coda.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.oliverbravery.coda.features.SortInventory;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class SortInventoryCommand {
    public static LiteralCommandNode register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        return dispatcher.register(
                literal("coda")
                        .then(literal("sortinventory")
                                .executes(SortInventoryCommand::SortInventory))
                        .then(literal("saveinventory")
                                .executes(SortInventoryCommand::SaveInventory)));
    }

    private static int SortInventory(CommandContext<FabricClientCommandSource> context){
        SortInventory.LoadInventory();
        return 1;
    }

    private static int SaveInventory(CommandContext<FabricClientCommandSource> context){
        SortInventory.AddInventory();
        return 1;
    }
}
