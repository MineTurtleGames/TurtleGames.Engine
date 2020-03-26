package co.turtlegames.engine.engine.prevention;

import co.turtlegames.core.TurtleModule;
import co.turtlegames.engine.engine.GameManager;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class PreventionManager extends TurtleModule {

    public PreventionManager(JavaPlugin plugin) {
        super(plugin, "Prevention Manager");
    }

    @Override
    public void initializeModule() {
        registerListener(this);
    }

    public PreventionSet getCurrentPreventionSet() {

        GameManager gameManager = this.getModule(GameManager.class);
        PreventionSet set = gameManager.getStateHandle().getPreventionSet();

        if(set == null)
            return new PreventionSet();

        return set;

    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {

        if (!this.getCurrentPreventionSet().isBlockPlaceAllowed())
            event.setCancelled(true);

    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {

        if (!this.getCurrentPreventionSet().isBlockPlaceAllowed())
            event.setCancelled(true);

    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {

        PreventionSet currentSet = this.getCurrentPreventionSet();

        if (!currentSet.isDamageEnabled()) {

            event.setCancelled(true);
            event.setDamage(0.0D);
            return;

        }

    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {

        PreventionSet currentSet = this.getCurrentPreventionSet();

        if (!currentSet.isDamageEnabled())
            return;

        Entity victim = event.getEntity();
        Entity damager = event.getDamager();

        if (!currentSet.isPvpEnabled() && victim.getType().equals(EntityType.PLAYER) && damager.getType().equals(EntityType.PLAYER)) {

            event.setCancelled(true);
            event.setDamage(0.0D);
            return;

        }

        if (!currentSet.isPveEnabled() && !victim.getType().equals(EntityType.PLAYER) && damager.getType().equals(EntityType.PLAYER)) {

            event.setCancelled(true);
            event.setDamage(0.0D);
            return;

        }

        if (!currentSet.isEvpEnabled() && victim.getType().equals(EntityType.PLAYER) && !damager.getType().equals(EntityType.PLAYER)) {

            event.setCancelled(true);
            event.setDamage(0.0D);

        }

    }



}
