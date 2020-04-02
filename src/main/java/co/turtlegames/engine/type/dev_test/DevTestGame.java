package co.turtlegames.engine.type.dev_test;

import co.turtlegames.core.scoreboard.TurtlePlayerScoreboard;
import co.turtlegames.engine.engine.GameManager;
import co.turtlegames.engine.engine.game.AbstractGame;
import co.turtlegames.engine.engine.game.player.GamePlayer;
import co.turtlegames.engine.engine.game.GameTeam;
import org.bukkit.ChatColor;

public class DevTestGame extends AbstractGame {

    public DevTestGame(GameManager gameManager) {
        super(gameManager);

        _gameOptions.setMaxPlayers(16);
        _gameOptions.setMinPlayers(1);

        _teams.add(new GameTeam(1, "Players", ChatColor.YELLOW));

    }

    @Override
    public void updatePlayerScoreboard(GamePlayer gamePlayer, TurtlePlayerScoreboard playerScoreboard) {

    }

}
