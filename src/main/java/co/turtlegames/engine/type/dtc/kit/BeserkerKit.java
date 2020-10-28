package co.turtlegames.engine.type.dtc.kit;

import co.turtlegames.core.recharge.RechargeManager;
import co.turtlegames.engine.engine.game.player.GamePlayer;
import co.turtlegames.engine.engine.kit.Kit;
import co.turtlegames.engine.type.dtc.DefendTheCoreGame;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class BeserkerKit extends Kit {

    private DefendTheCoreGame _gameInstance;

    public BeserkerKit(DefendTheCoreGame gameInstance) {
        super("Beserker", new String[] { "Beserker kit add description" }, Material.GOLD_AXE);

        _gameInstance = gameInstance;

    }

    @Override
    public void handleApply(GamePlayer gamePlayer) {

        Player player = gamePlayer.getPlayer();
        PlayerInventory inv = player.getInventory();

        inv.addItem(new ItemStack(Material.STONE_AXE));
        inv.addItem(new ItemStack(Material.WOOD, 64));

        inv.setArmorContents(new ItemStack[] {
                new ItemStack(Material.AIR),
                new ItemStack(Material.AIR),
                new ItemStack(Material.IRON_CHESTPLATE),
                new ItemStack(Material.AIR)
        });

    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {

        Player ply = event.getPlayer();

        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;

        if (event.getItem() == null)
            return;

        if (event.getItem().getType() != Material.STONE_AXE)
            return;

        GamePlayer gamePlayer = _gameInstance.getGameManager().getGamePlayer(ply, false);

        if (gamePlayer == null || gamePlayer.getKit() != this)
            return;

        RechargeManager recharge = _gameInstance.getGameManager().getModule(RechargeManager.class);

        if(!recharge.canUse(ply, "Beserk", true))
            return;
        recharge.startCooldown(ply, "Beserk", 25);

        World world = ply.getWorld();
        world.playSound(ply.getLocation(), Sound.BLAZE_DEATH, 1, 0);

        ply.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 3 * 20, 3));
        ply.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 3 * 20, 1));
        ply.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 3 * 20, 2));

    }

}
