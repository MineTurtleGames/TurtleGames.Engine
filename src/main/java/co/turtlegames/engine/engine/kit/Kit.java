package co.turtlegames.engine.engine.kit;

import co.turtlegames.core.util.ItemBuilder;
import co.turtlegames.engine.engine.game.GameType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class Kit implements Listener {

    private String _name;
    private String[] _description;
    private int _coinPrice;

    private Material _icon = Material.STONE_SWORD;

    private ArrayList<Player> _players = new ArrayList<>();

    public Kit(String name, String[] description, int coinPrice) {
        _name = name;
        _description = description;
        _coinPrice = coinPrice;
    }

    public void apply(Player player) {

        player.getInventory().clear();

        _players.add(player);
        handleApply(player);

    }

    public abstract void handleApply(Player player);

    public String getName() {
        return _name;
    }

    public int getCoinPrice() {
        return _coinPrice;
    }

    public ArrayList<Player> getPlayers() {
        return _players;
    }

    public ItemStack createIcon() {

        ItemBuilder builder = new ItemBuilder(_icon, ChatColor.DARK_GREEN + _name);

        builder.setLore(ChatColor.GRAY + "Unlock with " + _coinPrice + " coins",
                            "");
        builder.addLore(Arrays.asList(_description));

        return builder.build();

    }

}
