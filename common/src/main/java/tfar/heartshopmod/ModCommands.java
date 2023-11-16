package tfar.heartshopmod;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.sun.jdi.connect.Connector;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;

import java.util.Collection;

public class ModCommands {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal(HeartShopMod.MOD_ID)
                .then(Commands.literal("add")
                        .then(Commands.argument("targets", EntityArgument.players())
                                .then(Commands.argument("amount", IntegerArgumentType.integer()).executes(ModCommands::addHearts))))
      //          .then(Commands.literal("query")
       //                 .then(Commands.argument("targets", EntityArgument.players())
      //                          .then(Commands.argument("amount", IntegerArgumentType.integer()).executes(ModCommands::queryHearts))))
                .then(Commands.literal("set")
                        .then(Commands.argument("targets", EntityArgument.players())
                                .then(Commands.argument("amount", IntegerArgumentType.integer()).executes(ModCommands::setHearts))))
        );
    }

    private static int addHearts(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        Collection<ServerPlayer> targets = EntityArgument.getPlayers(context, "targets");
        int amount = IntegerArgumentType.getInteger(context, "amount");
        for (ServerPlayer player : targets) {
            PlayerDuck playerDuck = (PlayerDuck) player;
            playerDuck.addHeartCurrency(amount);
        }
        return targets.size();
    }

    private static int queryHearts(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        Collection<ServerPlayer> targets = EntityArgument.getPlayers(context, "targets");
        int amount = IntegerArgumentType.getInteger(context, "amount");
        for (ServerPlayer player : targets) {
            PlayerDuck playerDuck = (PlayerDuck) player;
            playerDuck.addHeartCurrency(amount);
        }
        return targets.size();
    }

    private static int setHearts(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        Collection<ServerPlayer> targets = EntityArgument.getPlayers(context, "targets");
        int amount = IntegerArgumentType.getInteger(context, "amount");
        for (ServerPlayer player : targets) {
            PlayerDuck playerDuck = (PlayerDuck) player;
            playerDuck.setHeartCurrency(amount);
        }
        return targets.size();
    }
}
