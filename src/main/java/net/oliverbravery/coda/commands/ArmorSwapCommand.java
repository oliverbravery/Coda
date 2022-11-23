package net.oliverbravery.coda.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.oliverbravery.coda.features.ArmorSwap;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class ArmorSwapCommand {
    public static LiteralCommandNode register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        return dispatcher.register(
                literal("coda")
                        .then(literal("armorswap")
                                .executes(ArmorSwapCommand::Run)));
    }

    private static int Run(CommandContext<FabricClientCommandSource> context){
        ArmorSwap.SwapPieces();
        return 1;
    }
}
