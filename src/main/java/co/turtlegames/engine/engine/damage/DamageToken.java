package co.turtlegames.engine.engine.damage;

import co.turtlegames.engine.engine.game.player.GamePlayer;
import org.bukkit.event.entity.EntityDamageEvent;

public class DamageToken {

    private long _damageTime;
    private EntityDamageEvent _event;
    private String _customCause;

    public DamageToken(long damageTime, EntityDamageEvent event) {
        _damageTime = damageTime;
        _event = event;
    }

    public DamageToken(long damageTime, EntityDamageEvent event, String customCause) {
        _damageTime = damageTime;
        _event = event;
        _customCause = customCause;
    }

    public long getDamageTime() {
        return _damageTime;
    }

    public EntityDamageEvent getEvent() {
        return _event;
    }

    public void setCustomCause(String customCause) {
        _customCause = customCause;
    }

    public String getCustomCause() {
        return _customCause;
    }

}
