package co.turtlegames.engine.engine.scoreboard;

import co.turtlegames.core.profile.PlayerProfile;
import co.turtlegames.core.profile.ProfileManager;
import co.turtlegames.core.scoreboard.ScoreboardView;
import co.turtlegames.core.scoreboard.TurtlePlayerScoreboard;
import co.turtlegames.engine.engine.GameManager;
import co.turtlegames.engine.engine.game.player.GamePlayer;

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

        PlayerProfile profile = _gameManager.getModule(ProfileManager.class)
                                    .fetchProfile(scoreboard.getOwner().getUniqueId())
                                        .getNow(null);

        if(profile == null)
            return;

        if(_gameManager.getStateHandle() == null)
            return;

        _gameManager.getStateHandle()
                .updatePlayerScoreboard(new GamePlayer(profile), scoreboard);

    }

}
