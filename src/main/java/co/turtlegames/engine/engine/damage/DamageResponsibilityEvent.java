package co.turtlegames.engine.engine.damage;

import co.turtlegames.engine.engine.game.player.GamePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageEvent;

public class DamageResponsibilityEvent extends Event {

    private static HandlerList HANDLER_LIST = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    private EntityDamageEvent _damageEvent;
    private GamePlayer _responsible = null;

    public DamageResponsibilityEvent(EntityDamageEvent event) {
        _damageEvent = event;
    }

    public EntityDamageEvent getDamageEvent() {
        return _damageEvent;
    }

    public void setResponsible(GamePlayer responsible) {
        _responsible = responsible;
    }

    public GamePlayer getResponsible() {
        return _responsible;
    }

}
