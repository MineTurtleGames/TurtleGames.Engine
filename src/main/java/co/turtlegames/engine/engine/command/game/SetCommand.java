package co.turtlegames.engine.engine.command.game;

import co.turtlegames.core.command.CommandBase;
import co.turtlegames.core.command.sub.SubCommandBase;
import co.turtlegames.core.common.Chat;
import co.turtlegames.core.profile.PlayerProfile;
import co.turtlegames.core.profile.Rank;
import co.turtlegames.engine.engine.GameManager;
import co.turtlegames.engine.engine.game.GameType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.Arrays;
import java.util.stream.Collectors;

public class SetCommand extends SubCommandBase<GameManager> {

    public SetCommand(CommandBase<GameManager> manager) {
        super(manager, Rank.ADMINISTRATOR, "set");
    }

    @Override
    public void executeCommand(PlayerProfile profile, String[] args) {

        if (args.length < 1) {

            String options = Arrays.stream(GameType.values()).map(gameType -> gameType.toString()).collect(Collectors.joining(", "));
            profile.getOwner().sendMessage(Chat.main(getModule().getName(), "Game type options: " + options));

            return;

        }

        GameType type;

        try {
            type = GameType.valueOf(args[0]);
        } catch (IllegalArgumentException exception) {

            profile.getOwner().sendMessage(Chat.main(getModule().getName(), "Invalid game type."));
            return;

        }

        getModule().setGame(type);

        Bukkit.broadcastMessage(ChatColor.GREEN + ChatColor.BOLD.toString() + profile.getOwner().getName() + " set the game to " + type.getName() + ".");

    }

}
