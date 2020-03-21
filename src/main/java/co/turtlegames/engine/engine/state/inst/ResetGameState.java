package co.turtlegames.engine.engine.state.inst;

import co.turtlegames.engine.engine.GameManager;
import co.turtlegames.engine.engine.state.GameState;
import co.turtlegames.engine.engine.state.IGameState;

public class ResetGameState implements IGameState {

    private GameManager _gameManager;

    public ResetGameState(GameManager gameManager) {

        _gameManager = gameManager;

    }

    @Override
    public void doInitialTick() {}

    @Override
    public void doTick() {
        _gameManager.switchState(GameState.LOBBY);
    }

}
