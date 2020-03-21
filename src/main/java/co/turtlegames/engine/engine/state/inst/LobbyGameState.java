package co.turtlegames.engine.engine.state.inst;

import co.turtlegames.engine.engine.GameManager;
import co.turtlegames.engine.engine.state.IGameState;

public class LobbyGameState implements IGameState {

    private static final int STATE_TICKS = 60 * 20;

    private GameManager _gameManager;

    private int _tickTimer;

    public LobbyGameState(GameManager gameManager) {

        _gameManager = gameManager;

    }

    @Override
    public void doInitialTick() {
        _tickTimer = STATE_TICKS;
    }

    @Override
    public void doTick() {

        _tickTimer--;

    }

}
