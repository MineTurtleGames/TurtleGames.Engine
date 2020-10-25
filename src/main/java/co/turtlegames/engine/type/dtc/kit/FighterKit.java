package co.turtlegames.engine.type.dtc.kit;

import co.turtlegames.engine.engine.game.player.GamePlayer;
import co.turtlegames.engine.engine.kit.Kit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class FighterKit extends Kit {

    public FighterKit() {
        super("Fighter", new String[]{"I am a fighter"}, Material.STONE_SWORD);
    }

    @Override
    public void handleApply(GamePlayer gamePlayer) {

        Player player = gamePlayer.getPlayer();
        PlayerInventory inv = player.getInventory();

        inv.addItem(new ItemStack(Material.STONE_SWORD));
        inv.addItem(new ItemStack(Material.WOOD, 64));

        inv.setArmorContents(new ItemStack[] {
                new ItemStack(Material.LEATHER_BOOTS),
                new ItemStack(Material.LEATHER_LEGGINGS),
                new ItemStack(Material.IRON_CHESTPLATE),
                new ItemStack(Material.LEATHER_HELMET)
        });

    }
}
