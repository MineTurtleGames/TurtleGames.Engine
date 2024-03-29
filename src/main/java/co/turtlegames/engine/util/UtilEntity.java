package co.turtlegames.engine.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageEvent;

public class UtilEntity {

    public static EntityDamageEvent damage(Damageable entity, double damage) {

        EntityDamageEvent event = new EntityDamageEvent(entity, EntityDamageEvent.DamageCause.CUSTOM, damage);
        Bukkit.getPluginManager().callEvent(event);

        if(event.isCancelled())
            return event;

        entity.damage(damage);

        return event;

    }

}
