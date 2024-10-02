package fqf.qua_mario;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import fqf.qua_mario.characters.MarioCharacter;
import fqf.qua_mario.powerups.PowerUp;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class MarioCommand {
	public static String setMarioCommand(CommandContext<ServerCommandSource> context, boolean playerArg) throws CommandSyntaxException {
		boolean isMario = BoolArgumentType.getBool(context, "value");

		ServerPlayerEntity player;
		if(playerArg) player = EntityArgumentType.getPlayer(context, "whoThough");
		else player = context.getSource().getPlayerOrThrow();

		ModMarioQuaMario.sendSetMarioPacket(player, isMario);

		ModMarioQuaMario.getMarioPersistentData(player).putBoolean("isMario", isMario);

		return (isMario ? "Enabled" : "Disabled") + " Mario mode for " + player + ".";
	}

	private static String setMarioCommand(ServerPlayerEntity player, boolean isMario) {
		ModMarioQuaMario.getMarioPersistentData(player).putBoolean("isMario", isMario);
		ModMarioQuaMario.sendSetMarioPacket(player, isMario);

		return (isMario ? "Enabled" : "Disabled") + " Mario mode for " + player + ".";
	}

	private static LiteralArgumentBuilder<ServerCommandSource> marioCharacterCommand() {
		LiteralArgumentBuilder<ServerCommandSource> setCharacter = literal("setCharacter");

		ModMarioQuaMario.LOGGER.info("Populating setCharacter command.");
		for(MarioCharacter character : MarioCharacter.CHILDREN) {
			ModMarioQuaMario.LOGGER.info("Adding " + character + " to setCharacter command.");
			setCharacter.then(literal(character.getName())
				.executes(context -> {
					String feedback = ModMarioQuaMario.setCharacter(context.getSource().getPlayerOrThrow(), character);
					context.getSource().sendFeedback(() -> Text.literal(feedback), true);
					return 1;
				})
				.then(argument("whoThough", EntityArgumentType.player())
						.executes(context -> {
							String feedback = ModMarioQuaMario.setCharacter(EntityArgumentType.getPlayer(context, "whoThough"), character);
							context.getSource().sendFeedback(() -> Text.literal(feedback), true);
							return 1;
						})
				)
			);
		}

		return setCharacter;
	}

	private static LiteralArgumentBuilder<ServerCommandSource> marioPowerUpCommand() {
		LiteralArgumentBuilder<ServerCommandSource> setPowerUp = literal("setPowerUp");

		ModMarioQuaMario.LOGGER.info("Populating setPowerUp command.");
		for(PowerUp powerUp : PowerUp.CHILDREN) {
			setPowerUp.then(literal(powerUp.getID().toString())
				.executes(context -> {
					String feedback = ModMarioQuaMario.setPowerUp(context.getSource().getPlayerOrThrow(), powerUp);
					context.getSource().sendFeedback(() -> Text.literal(feedback), true);
					return 1;
				})
				.then(argument("whoThough", EntityArgumentType.player())
						.executes(context -> {
							String feedback = ModMarioQuaMario.setPowerUp(EntityArgumentType.getPlayer(context, "whoThough"), powerUp);
							context.getSource().sendFeedback(() -> Text.literal(feedback), true);
							return 1;
						})
				)
			);
		}

		return setPowerUp;
	}

	public static void registerMarioCommand() {
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			dispatcher.register(literal("mario")
				.then(literal("setEnabled")
					.then(argument("value", BoolArgumentType.bool())
						.executes(context -> {
							String feedback = MarioCommand.setMarioCommand(context, false);
							context.getSource().sendFeedback(() -> Text.literal(feedback), true);
							return 1;
						})
						.then(argument("whoThough", EntityArgumentType.player())
							.executes(context -> {
								String feedback = MarioCommand.setMarioCommand(context, true);
								context.getSource().sendFeedback(() -> Text.literal(feedback), true);
								return 1;
							})
						)
					)
				)
				.then(marioCharacterCommand())
				.then(marioPowerUpCommand())
			);
		});
	}
}
