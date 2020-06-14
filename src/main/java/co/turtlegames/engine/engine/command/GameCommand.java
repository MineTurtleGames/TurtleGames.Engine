package co.turtlegames.engine.engine.command;

import co.turtlegames.core.command.CommandBase;
import co.turtlegames.core.profile.PlayerProfile;
import co.turtlegames.core.profile.Rank;
import co.turtlegames.engine.engine.GameManager;

public class GameCommand extends CommandBase<GameManager> {

    public GameCommand(GameManager module) {
        super(module, Rank.ADMINISTRATOR, "game");

        addSubCommand(new StartCommand(this));
        addSubCommand(new SetCommand(this));
    }

    @Override
    public void executeCommand(PlayerProfile profile, String[] args) {

    }

}
