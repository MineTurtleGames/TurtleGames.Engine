package co.turtlegames.engine.type.dtc.kit;

import co.turtlegames.engine.engine.kit.Kit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class FighterKit extends Kit {

    public FighterKit() {
        super("Fighter", new String[]{"I am a fighter"}, 0);
    }

    @Override
    public void handleApply(Player player) {

        player.getInventory().addItem(new ItemStack(Material.STONE_SWORD));
        player.getInventory().addItem(new ItemStack(Material.WOOD, 64));

    }
}
