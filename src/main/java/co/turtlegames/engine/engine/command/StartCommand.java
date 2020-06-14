package co.turtlegames.engine.engine.command;

import co.turtlegames.core.command.CommandBase;
import co.turtlegames.core.command.sub.SubCommandBase;
import co.turtlegames.core.profile.PlayerProfile;
import co.turtlegames.core.profile.Rank;
import co.turtlegames.engine.engine.GameManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class StartCommand extends SubCommandBase<GameManager> {

    public StartCommand(CommandBase<GameManager> manager) {
        super(manager, Rank.ADMINISTRATOR, "start");
    }

    @Override
    public void executeCommand(PlayerProfile profile, String[] args) {

        getModule().setForceStart(true);
        Bukkit.broadcastMessage(ChatColor.GREEN + ChatColor.BOLD.toString() + profile.getOwner().getName() + " started the game.");

    }
}
