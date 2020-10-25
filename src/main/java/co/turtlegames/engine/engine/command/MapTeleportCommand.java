package co.turtlegames.engine.engine.command;

import co.turtlegames.core.command.CommandBase;
import co.turtlegames.core.command.sub.SubCommandBase;
import co.turtlegames.core.profile.PlayerProfile;
import co.turtlegames.core.profile.Rank;
import co.turtlegames.engine.engine.GameManager;
import co.turtlegames.engine.engine.map.MapManager;

public class MapTeleportCommand extends SubCommandBase<GameManager> {

    public MapTeleportCommand(CommandBase<GameManager> command) {
        super(command, Rank.ADMINISTRATOR, "maptp");
    }

    @Override
    public void executeCommand(PlayerProfile profile, String[] args) {

        profile.getOwner().teleport(this.getModule().getModule(MapManager.class).getActiveWorld().getSpawnLocation());
        profile.getOwner().sendMessage("hi");

    }

}
