package co.turtlegames.engine.engine.visiblity.listener;

import co.turtlegames.engine.engine.visiblity.VisibilityManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class VisibilityListener implements Listener {

    private VisibilityManager _manager;

    public VisibilityListener(VisibilityManager manager) {
        _manager = manager;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

        Player ply = event.getPlayer();

        for(UUID uuid : _manager.getHiddenPlayers()) {

            Player hiddenPlayer = Bukkit.getPlayer(uuid);

            if(ply == null)
                continue;

            ply.hidePlayer(hiddenPlayer);

        }

    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        _manager.purgeFromSet(event.getPlayer());
    }

}
