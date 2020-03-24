package co.turtlegames.engine.engine.state.inst;

import co.turtlegames.core.scoreboard.TurtlePlayerScoreboard;
import co.turtlegames.engine.engine.GameManager;
import co.turtlegames.engine.engine.prevention.PreventionSet;
import co.turtlegames.engine.engine.state.GameState;
import co.turtlegames.engine.engine.state.IGameState;

public class ResetGameState implements IGameState {

    private PreventionSet _preventionSet;

    private GameManager _gameManager;

    public ResetGameState(GameManager gameManager) {

        _gameManager = gameManager;

        _preventionSet = new PreventionSet();

    }

    @Override
    public PreventionSet getPreventionSet() {
        return _preventionSet;
    }

    @Override
    public void doInitialTick() {}

    @Override
    public void doTick() {
        _gameManager.switchState(GameState.LOBBY);
    }

    @Override
    public void updatePlayerScoreboard(TurtlePlayerScoreboard scoreboard) {
        scoreboard.setLine(0, "Reset in progress");
    }

}
