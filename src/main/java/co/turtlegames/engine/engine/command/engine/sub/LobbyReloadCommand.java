package co.turtlegames.engine.engine.command.engine.sub;

import co.turtlegames.core.command.CommandBase;
import co.turtlegames.core.command.sub.SubCommandBase;
import co.turtlegames.core.common.Chat;
import co.turtlegames.core.profile.PlayerProfile;
import co.turtlegames.core.profile.Rank;
import co.turtlegames.core.world.virtual.VirtualWorldManager;
import co.turtlegames.engine.engine.GameManager;
import co.turtlegames.engine.engine.command.engine.EngineCommand;
import co.turtlegames.engine.engine.map.MapManager;
import co.turtlegames.engine.engine.state.GameState;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class LobbyReloadCommand extends SubCommandBase<GameManager> {

    public LobbyReloadCommand(CommandBase command) {
        super(command, Rank.ADMINISTRATOR, "lobbyreload");
    }

    @Override
    public void executeCommand(PlayerProfile profile, String[] args) {

        Player ply = profile.getOwner();

        VirtualWorldManager virtualWorldManager = this.getModule().getModule(VirtualWorldManager.class);
        MapManager mapManager = this.getModule().getModule(MapManager.class);

        World lobbyWorld = mapManager.getLobbyWorld();

        if(lobbyWorld == null) {

            ply.sendMessage(Chat.main("Error", "There is no lobby"));
            return;

        }

        mapManager.unregisterLobbyWorld();
        World newWorld = mapManager.getLobbyWorld();

        for (Player player : lobbyWorld.getPlayers()) {

            Location newPos = player.getLocation().clone();
            newPos.setWorld(newWorld);

            player.teleport(newPos);

        }

        virtualWorldManager.unloadVirtualWorld(lobbyWorld);

        Bukkit.broadcastMessage(ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "The lobby was regenerated");

    }
}
