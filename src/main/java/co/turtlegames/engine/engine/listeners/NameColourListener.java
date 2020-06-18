package co.turtlegames.engine.engine.listeners;

import co.turtlegames.core.scoreboard.CoreNameColourEvent;
import co.turtlegames.engine.engine.GameManager;
import co.turtlegames.engine.engine.game.GameTeam;
import co.turtlegames.engine.engine.game.player.GamePlayer;
import co.turtlegames.engine.engine.game.player.PlayerState;
import co.turtlegames.engine.engine.state.GameState;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class NameColourListener implements Listener {

    private GameManager _gameManager;

    public NameColourListener(GameManager gameManager) {
        _gameManager = gameManager;
    }

    @EventHandler
    public void onColour(CoreNameColourEvent event) {

        GamePlayer gamePlayer = _gameManager.getGamePlayer(event.getPlayerProfile().getOwner(), false);

        if(gamePlayer == null)
            return;

        if(_gameManager.getState() != GameState.IN_GAME)
            return;

        if(gamePlayer.getState() != PlayerState.ALIVE) {
            event.setNameColour(ChatColor.DARK_GRAY);
            return;
        } else {

            GameTeam team = gamePlayer.getTeam();

            if(team == null)
                return;

            event.setNameColour(team.getColour());

        }

    }

}
