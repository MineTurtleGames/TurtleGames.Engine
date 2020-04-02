package co.turtlegames.engine.engine.visiblity;

import co.turtlegames.core.TurtleModule;
import co.turtlegames.engine.engine.visiblity.listener.VisibilityListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class VisibilityManager extends TurtleModule {

    private Set<UUID> _invisible = new HashSet<>();

    public VisibilityManager(JavaPlugin plugin) {
        super(plugin, "Visibility Manager");
    }

    public void hidePlayer(Player ply) {

        _invisible.add(ply.getUniqueId());

        for(Player otherPlayer : Bukkit.getOnlinePlayers())
            otherPlayer.hidePlayer(ply);

    }

    public void showPlayer(Player ply) {

        _invisible.remove(ply.getUniqueId());

        for(Player otherPlayer : Bukkit.getOnlinePlayers())
            otherPlayer.showPlayer(otherPlayer);

    }

    @Override
    public void initializeModule() {
        this.registerListener(new VisibilityListener(this));
    }

    public Collection<UUID> getHiddenPlayers() {
        return _invisible;
    }

    public void purgeFromSet(Player player) {
        _invisible.remove(player.getUniqueId());
    }

}
