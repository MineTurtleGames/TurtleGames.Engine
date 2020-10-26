package co.turtlegames.engine.engine.command.engine;

import co.turtlegames.core.command.CommandBase;
import co.turtlegames.core.profile.PlayerProfile;
import co.turtlegames.core.profile.Rank;
import co.turtlegames.engine.engine.GameManager;
import co.turtlegames.engine.engine.command.engine.sub.LobbyReloadCommand;

public class EngineCommand extends CommandBase<GameManager> {

    public EngineCommand(GameManager module) {
        super(module, Rank.ADMINISTRATOR, "engine");

        this.addSubCommand(new LobbyReloadCommand(this));

    }

    @Override
    public void executeCommand(PlayerProfile profile, String[] args) {

    }

}
