package co.turtlegames.engine.engine.state.inst;

import co.turtlegames.core.scoreboard.TurtlePlayerScoreboard;
import co.turtlegames.engine.engine.GameManager;
import co.turtlegames.engine.engine.game.player.GamePlayer;
import co.turtlegames.engine.engine.prevention.PreventionSet;
import co.turtlegames.engine.engine.state.AbstractStateProvider;
import co.turtlegames.engine.util.TickRate;

public class InGameState extends AbstractStateProvider {

    private GameManager _gameManager;

    public InGameState(GameManager gameManager) {
        _gameManager = gameManager;
    }

    @Override
    public void doInitialTick() {}

    @Override
    public void doTick(TickRate tickRate) {

        _gameManager.getDeathManager().doGameTick(tickRate);
        _gameManager.getGameInstance().handleTick(tickRate);

    }

    @Override
    public void updatePlayerScoreboard(GamePlayer gamePlayer, TurtlePlayerScoreboard scoreboard) {
        _gameManager.getGameInstance().updatePlayerScoreboard(gamePlayer, scoreboard);
    }

    @Override
    public PreventionSet getPreventionSet() {
        return _gameManager.getGameOptions();
    }

}
