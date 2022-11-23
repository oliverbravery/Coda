package net.oliverbravery.coda.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.oliverbravery.coda.config.Config;
import net.oliverbravery.coda.utilities.Utils;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class CodaMenuButtonCommand {
    public static LiteralCommandNode register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        return dispatcher.register(
                literal("coda")
                        .then(literal("menubutton")
                                .executes(CodaMenuButtonCommand::Run)));
    }

    private static int Run(CommandContext<FabricClientCommandSource> context){
        Config.SetValue("CodaButtonEnabled", String.valueOf(!Boolean.parseBoolean(Config.GetValue("CodaButtonEnabled", "true"))));
        Utils.SendChatMessage(String.format("§6Coda Menu Button has been toggled to §6%s", Config.GetValue("CodaButtonEnabled", "true")));
        return 1;
    }
}

