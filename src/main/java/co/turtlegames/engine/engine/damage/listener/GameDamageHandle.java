package co.turtlegames.engine.engine.damage.listener;

import co.turtlegames.engine.engine.GameManager;
import co.turtlegames.engine.engine.damage.DamageManager;
import co.turtlegames.engine.engine.damage.DamageToken;
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

    private DamageManager _damageManager;

    public GameDamageHandle(DamageManager damageManager) {
        _damageManager = damageManager;
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {

        GameManager gameManager = _damageManager.getGameManager();

        if (gameManager.getState() != GameState.IN_GAME)
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

        if(!DamageValidator.canDamage(_damageManager.getGameManager(), event)) {

            event.setCancelled(true);
            return;

        }

        if (event.getCause() != EntityDamageEvent.DamageCause.CUSTOM) {
            DamageToken damageToken = new DamageToken(System.currentTimeMillis(), event);
            gamePlayer.registerDamageToken(damageToken);
        }

        if(ply.getHealth() - event.getFinalDamage() > 0)
            return;

        event.setCancelled(true);
        event.setDamage(0);

        ply.setHealth(20);

        Bukkit.broadcastMessage(_damageManager.generateDeathMessage(event));
        _damageManager.killPlayer(gameManager.getGamePlayer(ply, true));

    }

}
