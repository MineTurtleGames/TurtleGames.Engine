package co.turtlegames.engine.engine.listeners;

import co.turtlegames.core.common.Chat;
import co.turtlegames.engine.engine.GameManager;
import co.turtlegames.engine.engine.map.MapManager;
import co.turtlegames.engine.engine.state.GameState;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinLeaveListener implements Listener {

    private GameManager _gameManager;

    public JoinLeaveListener(GameManager gameManager) {
        _gameManager = gameManager;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

        Player ply = event.getPlayer();

        Location joinPosition;
        String joinMessage;

        if(_gameManager.getState() == GameState.IN_GAME) {

            joinPosition = new Location(Bukkit.getWorld("world"), 0, 0, 0);
            joinMessage = Chat.main("Welcome", "Welcome to " + Chat.elem("TurtleGames") + "! A game is currently in progress");

        } else {

            joinPosition = _gameManager.getModule(MapManager.class).getLobbyTeleportPosition();
            joinMessage = Chat.main("Welcome", "Welcome to " + Chat.elem("TurtleGames") + "! A game will start soon!");

            _gameManager.giveLobbyItems(ply);

        }

        ply.teleport(joinPosition);
        ply.sendMessage(joinMessage);

        ply.playSound(ply.getLocation(), Sound.LEVEL_UP, 1, 1);

    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        _gameManager.purgeGamePlayer(event.getPlayer());
    }

}
