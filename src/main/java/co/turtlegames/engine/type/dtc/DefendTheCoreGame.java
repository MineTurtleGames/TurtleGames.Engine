package co.turtlegames.engine.type.dtc;

import co.turtlegames.core.scoreboard.TurtlePlayerScoreboard;
import co.turtlegames.engine.engine.GameManager;
import co.turtlegames.engine.engine.game.AbstractGame;
import co.turtlegames.engine.engine.game.player.GamePlayer;
import co.turtlegames.engine.engine.game.GameTeam;
import co.turtlegames.engine.type.dtc.kit.FighterKit;
import org.bukkit.ChatColor;

public class DefendTheCoreGame extends AbstractGame {

    public DefendTheCoreGame(GameManager gameManager) {
        super(gameManager);

        _gameOptions.setWaterDamage(true);

        _gameOptions.setMinPlayers(2);
        _gameOptions.setMaxPlayers(32);

        _gameOptions.setBlockBreakAllowed(true);
        _gameOptions.setBlockPlaceAllowed(true);

        _gameOptions.enableAllDamage();

        _teams.add(new GameTeam(1, "Red", ChatColor.RED));
        _teams.add(new GameTeam(2, "Blue", ChatColor.BLUE));

        _kits.add(new FighterKit());

    }

    @Override
    public void updatePlayerScoreboard(GamePlayer gamePlayer, TurtlePlayerScoreboard playerScoreboard) {

        playerScoreboard.setLine(1, "Defend the core");
        playerScoreboard.setLine(2, "Defend the core");
        playerScoreboard.setLine(3, "Defend the core");
        playerScoreboard.setLine(4, "Defend the core");
        playerScoreboard.setLine(5, "Defend the core");
        playerScoreboard.setLine(6, "Defend the core");
        playerScoreboard.setLine(7, "Defend the core");
        playerScoreboard.setLine(8, "Defend the core");

    }

}
