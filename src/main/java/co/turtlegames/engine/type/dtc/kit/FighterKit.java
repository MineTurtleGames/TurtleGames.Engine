package co.turtlegames.engine.type.dtc.kit;

import co.turtlegames.engine.engine.kit.Kit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class DTCPlayerKit extends Kit {

    public DTCPlayerKit() {
        super("Player", new String[]{"this is a lore"}, 0);
    }

    @Override
    public void handleApply(Player player) {
        player.getInventory().addItem(new ItemStack(Material.STONE_SWORD));
    }
}
