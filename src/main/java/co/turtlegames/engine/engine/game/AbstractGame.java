package co.turtlegames.engine.engine.game;

import co.turtlegames.core.scoreboard.TurtlePlayerScoreboard;
import co.turtlegames.engine.engine.GameManager;

public abstract class AbstractGame {

    private GameManager _gameManager;

    protected GameOptions _gameOptions = new GameOptions();

    public AbstractGame(GameManager gameManager) {
        _gameManager = gameManager;
    }

    public abstract void updatePlayerScoreboard(TurtlePlayerScoreboard playerScoreboard);

}
