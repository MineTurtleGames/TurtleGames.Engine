package co.turtlegames.engine.engine.death.listener;

import co.turtlegames.core.stats.PlayerStat;
import co.turtlegames.engine.engine.GameManager;
import co.turtlegames.engine.engine.death.DeathManager;
import co.turtlegames.engine.engine.game.player.GamePlayer;
import co.turtlegames.engine.engine.game.player.PlayerState;
import co.turtlegames.engine.engine.state.GameState;
import co.turtlegames.engine.util.DamageValidator;
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
        GamePlayer gamePlayer = gameManager.getGamePlayer(ply, false);

        if(gamePlayer == null
            || gamePlayer.getState() != PlayerState.ALIVE) {

            event.setCancelled(true);
            return;

        }

        if(!DamageValidator.canDamage(_deathManager.getGameManager(), event)) {

            event.setCancelled(true);
            return;

        }

        if(ply.getHealth() - event.getFinalDamage() > 0)
            return;

        event.setCancelled(true);
        event.setDamage(0);

        ply.setHealth(20);

        Bukkit.broadcastMessage(_deathManager.generateDeathMessage(event));

        _deathManager.killPlayer(gameManager.getGamePlayer(ply, true));

    }

}
