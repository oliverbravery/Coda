package net.oliverbravery.coda.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.oliverbravery.coda.features.AutoSaveTool;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class AutoSaveToolsCommand {
    public static LiteralCommandNode register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        return dispatcher.register(
                literal("coda")
                        .then(literal("autosavetools")
                                .executes(AutoSaveToolsCommand::Run)));
    }

    private static int Run(CommandContext<FabricClientCommandSource> context){
        return AutoSaveTool.Toggle();
    }
}
