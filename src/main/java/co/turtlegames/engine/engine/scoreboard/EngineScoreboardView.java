package co.turtlegames.engine.engine.scoreboard;

import co.turtlegames.core.scoreboard.ScoreboardView;
import co.turtlegames.core.scoreboard.TurtlePlayerScoreboard;
import co.turtlegames.engine.engine.GameManager;
import org.bukkit.scoreboard.Score;

public class EngineScoreboardView extends ScoreboardView {

    private GameManager _gameManager;

    public EngineScoreboardView(GameManager gameManager) {

        super("Game Engine");

        _gameManager = gameManager;

    }

    @Override
    public void initializeBoard(TurtlePlayerScoreboard scoreboard) {
        this.updateBoard(scoreboard);
    }

    @Override
    public void updateBoard(TurtlePlayerScoreboard scoreboard) {

        _gameManager.getStateHandle()
                .updatePlayerScoreboard(scoreboard);

    }

}
