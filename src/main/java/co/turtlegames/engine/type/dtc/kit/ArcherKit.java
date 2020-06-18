package co.turtlegames.engine.type.dtc.kit;

import co.turtlegames.engine.engine.kit.Kit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class ArcherKit extends Kit {

    public ArcherKit() {
        super("Archer", new String[] { "you have a bow" });
    }

    @Override
    public void handleApply(Player player) {

        PlayerInventory inv = player.getInventory();

        inv.addItem(new ItemStack(Material.STONE_SWORD));
        inv.addItem(new ItemStack(Material.WOOD, 64));

        inv.addItem(new ItemStack(Material.BOW, 1));
        inv.addItem(new ItemStack(Material.ARROW, 64));

        inv.setArmorContents(new ItemStack[] {
                new ItemStack(Material.LEATHER_BOOTS),
                new ItemStack(Material.LEATHER_LEGGINGS),
                new ItemStack(Material.LEATHER_CHESTPLATE),
                new ItemStack(Material.LEATHER_HELMET)
        });

    }

}
