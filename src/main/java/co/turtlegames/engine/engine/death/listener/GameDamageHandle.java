package co.turtlegames.engine.engine.death.listener;

import co.turtlegames.engine.engine.GameManager;
import co.turtlegames.engine.engine.death.DeathManager;
import co.turtlegames.engine.engine.state.GameState;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class GameDamageHandle implements Listener {

    private DeathManager _deathManager;

    public GameDamageHandle(DeathManager deathManager) {
        _deathManager = deathManager;
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {

        GameManager gameManager = _deathManager.getGameManager();

        if(gameManager.getState() != GameState.IN_GAME)
            return;

        if(!(event.getEntity() instanceof Player))
            return;

        Player ply = (Player) event.getEntity();

        if(ply.getHealth() - event.getFinalDamage() > 0)
            return;

        event.setCancelled(true);
        ply.setHealth(20);

        Bukkit.broadcastMessage(_deathManager.generateDeathMessage(event));

        _deathManager.killPlayer(gameManager.getGamePlayer(ply, true));

    }

}
