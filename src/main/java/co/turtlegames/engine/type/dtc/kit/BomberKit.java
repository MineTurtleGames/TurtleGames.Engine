package co.turtlegames.engine.type.dtc.kit;

import co.turtlegames.engine.engine.game.player.GamePlayer;
import co.turtlegames.engine.engine.kit.Kit;
import co.turtlegames.engine.type.dtc.DefendTheCoreGame;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.util.Vector;

public class BomberKit extends Kit {

    private DefendTheCoreGame _gameInstance;

    public BomberKit(DefendTheCoreGame gameInstance) {
        super("Bomber", new String[] { "like mohammed" }, Material.TNT);

        _gameInstance = gameInstance;

    }

    @Override
    public void handleApply(GamePlayer gamePlayer) {

        Player player = gamePlayer.getPlayer();
        PlayerInventory inv = player.getInventory();

        inv.addItem(new ItemStack(Material.STONE_SWORD));
        inv.addItem(new ItemStack(Material.WOOD, 64));

        inv.addItem(new ItemStack(Material.TNT, 3));

        inv.setArmorContents(new ItemStack[] {
                new ItemStack(Material.LEATHER_BOOTS),
                new ItemStack(Material.LEATHER_LEGGINGS),
                new ItemStack(Material.IRON_CHESTPLATE),
                new ItemStack(Material.LEATHER_HELMET)
        });


    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {

        Player ply = event.getPlayer();

        if(event.getAction() == Action.PHYSICAL)
            return;

        if(event.getItem() == null)
            return;

        if(event.getItem().getType() != Material.TNT)
            return;

        GamePlayer gamePlayer = _gameInstance.getGameManager().getGamePlayer(ply, false);

        if(gamePlayer == null)
            return;

        Vector direction = ply.getEyeLocation().getDirection().normalize();

        TNTPrimed tnt = (TNTPrimed) ply.getWorld().spawnEntity(ply.getEyeLocation(), EntityType.PRIMED_TNT);
        tnt.setVelocity(direction.multiply(1.1f));

        ItemStack stack = ply.getItemInHand();
        if(stack.getAmount() > 1)
            stack.setAmount(stack.getAmount() - 1);
        else
            ply.getInventory().remove(stack);

    }

}
