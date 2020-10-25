package co.turtlegames.engine.engine.kit;

import co.turtlegames.core.util.ItemBuilder;
import co.turtlegames.engine.engine.game.GameType;
import co.turtlegames.engine.engine.game.player.GamePlayer;
import co.turtlegames.engine.engine.kit.type.IKitPurchaseable;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public abstract class Kit implements Listener {

    private String _name;
    private String[] _description;
    private Material _icon = Material.STONE_SWORD;

    public Kit(String name, String[] description, Material icon) {

        _name = name;
        _description = description;

        _icon = icon;

    }

    public void apply(GamePlayer gamePlayer) {

        gamePlayer.getPlayer().getInventory().clear();
        handleApply(gamePlayer);

    }

    public abstract void handleApply(GamePlayer player);

    public String getName() {
        return _name;
    }

    public ItemStack createIcon() {

        ItemBuilder builder = new ItemBuilder(_icon, ChatColor.DARK_GREEN + _name);

        builder.setLore(ChatColor.GREEN + this.getAccessMethod(),
                            "");
        builder.addLore(Arrays.stream(_description).map(str -> ChatColor.GRAY + str)
                .collect(Collectors.toList()));

        return builder.build();

    }

    public String getAccessMethod() {

        if(this instanceof IKitPurchaseable)
            return "Unlock with " + ((IKitPurchaseable) this).getPrice() + " coins";

        return "Free kit";

    }

}
