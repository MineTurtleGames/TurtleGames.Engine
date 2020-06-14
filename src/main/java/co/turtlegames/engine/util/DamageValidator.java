package co.turtlegames.engine.util;

import co.turtlegames.engine.engine.GameManager;
import co.turtlegames.engine.engine.game.player.GamePlayer;
import co.turtlegames.engine.engine.game.player.PlayerState;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class DamageValidator {

    public static boolean canDamage(GameManager gameManager, EntityDamageEvent event) {

        if(event instanceof EntityDamageByEntityEvent) {

            EntityDamageByEntityEvent edbeEvent = (EntityDamageByEntityEvent) event;

            if(DamageValidator.isGhostDamage(gameManager, edbeEvent))
                return false;

        }

        return true;

    }

    public static boolean isGhostDamage(GameManager gameManager, EntityDamageByEntityEvent event) {

        if(!(event.getDamager() instanceof Player))
            return false;

        GamePlayer gamePlayer = gameManager.getGamePlayer((Player) event.getDamager(), false);

        return gamePlayer == null
                    || gamePlayer.getState() != PlayerState.ALIVE;

    }

}
