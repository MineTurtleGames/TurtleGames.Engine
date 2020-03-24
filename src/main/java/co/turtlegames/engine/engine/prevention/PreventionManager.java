package co.turtlegames.engine.engine.prevention;

import co.turtlegames.core.TurtleModule;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class PreventionManager extends TurtleModule {

    private PreventionSet _currentSet;

    public PreventionManager(JavaPlugin plugin) {
        super(plugin, "Prevention Manager");

        _currentSet = new PreventionSet();
    }

    public void setCurrentSet(PreventionSet set) {
        _currentSet = set;
    }

    @Override
    public void initializeModule() {
        registerListener(this);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {

        if (!_currentSet.isBlockPlaceAllowed())
            event.setCancelled(true);

    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {

        if (!_currentSet.isBlockPlaceAllowed())
            event.setCancelled(true);

    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {

        if (!_currentSet.isDamageEnabled()) {

            event.setCancelled(true);
            event.setDamage(0.0D);
            return;

        }

        Entity victim = event.getEntity();
        Entity damager = event.getDamager();

        if (!_currentSet.isPvpEnabled() && victim.getType().equals(EntityType.PLAYER) && damager.getType().equals(EntityType.PLAYER)) {

            event.setCancelled(true);
            event.setDamage(0.0D);
            return;

        }

        if (!_currentSet.isPveEnabled() && !victim.getType().equals(EntityType.PLAYER) && damager.getType().equals(EntityType.PLAYER)) {

            event.setCancelled(true);
            event.setDamage(0.0D);
            return;

        }

        if (!_currentSet.isEvpEnabled() && victim.getType().equals(EntityType.PLAYER) && !damager.getType().equals(EntityType.PLAYER)) {

            event.setCancelled(true);
            event.setDamage(0.0D);

        }

    }

}
