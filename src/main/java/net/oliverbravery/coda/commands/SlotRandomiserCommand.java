package net.oliverbravery.coda.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.oliverbravery.coda.features.SlotRandomiser;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class SlotRandomiserCommand {
    public static LiteralCommandNode register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        return dispatcher.register(
                literal("coda")
                        .then(literal("slotrandomiser")
                                .executes(SlotRandomiserCommand::Run)));
    }

    private static int Run(CommandContext<FabricClientCommandSource> context){
        return SlotRandomiser.Toggle();
    }
}
