package co.turtlegames.engine.engine.command.game;

import co.turtlegames.core.command.CommandBase;
import co.turtlegames.core.profile.PlayerProfile;
import co.turtlegames.core.profile.Rank;
import co.turtlegames.engine.engine.GameManager;

public class GameCommand extends CommandBase<GameManager> {

    public GameCommand(GameManager module) {
        super(module, Rank.ADMINISTRATOR, "game");

        this.addSubCommand(new StartCommand(this));
        this.addSubCommand(new SetCommand(this));
        this.addSubCommand(new StopCommand(this));
        this.addSubCommand(new MapTeleportCommand(this));

    }

    @Override
    public void executeCommand(PlayerProfile profile, String[] args) {

    }

}
