package co.turtlegames.engine.type.dtc.kit;

import co.turtlegames.core.recharge.RechargeManager;
import co.turtlegames.core.util.ItemBuilder;
import co.turtlegames.engine.engine.game.player.GamePlayer;
import co.turtlegames.engine.engine.kit.Kit;
import co.turtlegames.engine.type.dtc.DefendTheCoreGame;
import co.turtlegames.engine.util.UtilItemMeta;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.UUID;

public class WarperKit extends Kit {

    private DefendTheCoreGame _gameInstance;
    private HashMap<UUID, Location> _markMap = new HashMap<>();

    public WarperKit(DefendTheCoreGame gameInstance) {
        super("Shade", new String[] { "teleport around" }, Material.EYE_OF_ENDER);

        _gameInstance = gameInstance;

    }

    @Override
    public void resetStates() {
        _markMap = new HashMap<>();
    }

    @Override
    public void handleApply(GamePlayer gamePlayer) {

        Player player = gamePlayer.getPlayer();
        PlayerInventory inv = player.getInventory();

        inv.addItem(new ItemStack(Material.WOOD_SWORD));
        inv.addItem(new ItemStack(Material.WOOD, 64));

        inv.addItem(new ItemBuilder(Material.ENDER_PEARL, ChatColor.LIGHT_PURPLE + "Mark").build());

        inv.setArmorContents(new ItemStack[] {
                new ItemStack(Material.LEATHER_BOOTS),
                new ItemStack(Material.LEATHER_LEGGINGS),
                new ItemStack(Material.LEATHER_CHESTPLATE),
                new ItemStack(Material.LEATHER_HELMET)
        });

    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {

        Player ply = event.getPlayer();

        if (event.getAction() == Action.PHYSICAL)
            return;

        if (event.getItem() == null)
            return;

        if (event.getItem().getType() != Material.EYE_OF_ENDER && event.getItem().getType() != Material.ENDER_PEARL)
            return;

        ItemStack inHand = event.getItem();
        GamePlayer gamePlayer = _gameInstance.getGameManager().getGamePlayer(ply, false);

        if (gamePlayer == null || gamePlayer.getKit() != this)
            return;

        event.setCancelled(true);
        ply.updateInventory();

        RechargeManager recharge = _gameInstance.getGameManager().getModule(RechargeManager.class);

        World world = ply.getWorld();
        Location loc = ply.getLocation();

        if(inHand.getType() == Material.ENDER_PEARL) {

            if (!recharge.canUse(ply, "Warp",true))
                return;

            recharge.startCooldown(ply, "Warp", 30);

            ItemStack newItem = new ItemBuilder(Material.EYE_OF_ENDER, ChatColor.DARK_PURPLE + "Warp")
                                        .glow()
                                            .build();

           ply.setItemInHand(newItem);
           ply.updateInventory();

           _markMap.put(ply.getUniqueId(), loc);

            world.playSound(loc, Sound.FIZZ,  1, 1);

        } else {

            ItemStack newItem = new ItemBuilder(Material.ENDER_PEARL, ChatColor.LIGHT_PURPLE + "Mark")
                    .build();

            ply.setItemInHand(newItem);

            Location warpPos = _markMap.get(ply.getUniqueId());
            _markMap.remove(ply.getUniqueId());

            if(warpPos == null)
                return;

            world.playSound(loc, Sound.ENDERMAN_TELEPORT, 1, 1);
            world.playSound(warpPos, Sound.ENDERMAN_TELEPORT, 1, 1);

            world.playEffect(loc, Effect.ENDER_SIGNAL, 1, 1);
            world.playEffect(warpPos, Effect.ENDER_SIGNAL, 1, 1);

            ply.teleport(warpPos);
            ply.updateInventory();

        }


    }

}
