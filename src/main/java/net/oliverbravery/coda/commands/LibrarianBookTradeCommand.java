package net.oliverbravery.coda.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.command.argument.EnchantmentArgumentType;
import net.minecraft.enchantment.Enchantment;
import net.oliverbravery.coda.features.LibrarianBookTrade;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class LibrarianBookTradeCommand {
    public static LiteralCommandNode register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        return dispatcher.register(
                literal("coda")
                        .then(literal("librariantrade")
                                .then(ClientCommandManager.argument("book", EnchantmentArgumentType.enchantment())
                                        .then(ClientCommandManager.argument("maxprice", IntegerArgumentType.integer(0,64))
                                                .executes(LibrarianBookTradeCommand::run)))));
    }

    private static int run(CommandContext<FabricClientCommandSource> context) {
        Integer bookPrice = IntegerArgumentType.getInteger(context, "maxprice");
        Enchantment book = context.getArgument("book", Enchantment.class);
        LibrarianBookTrade.Run(book, bookPrice);
        return 1;
    }
}
