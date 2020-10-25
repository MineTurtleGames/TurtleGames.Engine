package co.turtlegames.engine.engine.command;

import co.turtlegames.core.command.CommandBase;
import co.turtlegames.core.command.sub.SubCommandBase;
import co.turtlegames.core.common.Chat;
import co.turtlegames.core.profile.PlayerProfile;
import co.turtlegames.core.profile.Rank;
import co.turtlegames.engine.engine.GameManager;
import co.turtlegames.engine.engine.game.GameType;
import co.turtlegames.engine.engine.state.GameState;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.stream.Collectors;

public class StopCommand extends SubCommandBase<GameManager> {

    public StopCommand(CommandBase<GameManager> manager) {
        super(manager, Rank.ADMINISTRATOR, "stop", "pause");
    }

    @Override
    public void executeCommand(PlayerProfile profile, String[] args) {

        Player ply = profile.getOwner();

        if(this.getModule().getState() == GameState.LOBBY) {
            this.getModule().switchState(GameState.INACTIVE);
        } else if(this.getModule().getState() == GameState.IN_GAME) {

            this.getModule().getGameInstance().endGame("Game ended by administrator");

        }

        ply.sendMessage(Chat.main("Game", "The game was stopped"));

    }
}
