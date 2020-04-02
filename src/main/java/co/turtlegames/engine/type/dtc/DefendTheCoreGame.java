package co.turtlegames.engine.type.dtc;

import co.turtlegames.core.scoreboard.TurtlePlayerScoreboard;
import co.turtlegames.engine.engine.GameManager;
import co.turtlegames.engine.engine.game.AbstractGame;
import co.turtlegames.engine.engine.game.player.GamePlayer;
import co.turtlegames.engine.engine.game.GameTeam;
import co.turtlegames.engine.type.dtc.kit.DTCPlayerKit;
import org.bukkit.ChatColor;

public class DefendTheCoreGame extends AbstractGame {

    public DefendTheCoreGame(GameManager gameManager) {
        super(gameManager);

        _gameOptions.setWaterDamage(true);

        _gameOptions.setMinPlayers(2);
        _gameOptions.setMaxPlayers(32);

        _gameOptions.enableAllDamage();

        _teams.add(new GameTeam(1, "Red", ChatColor.RED));
        _teams.add(new GameTeam(2, "Blue", ChatColor.BLUE));

        _kits.add(new DTCPlayerKit());

    }

    @Override
    public void updatePlayerScoreboard(GamePlayer gamePlayer, TurtlePlayerScoreboard playerScoreboard) {

    }

}
