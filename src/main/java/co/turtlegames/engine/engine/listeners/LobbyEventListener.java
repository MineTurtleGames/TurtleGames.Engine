package co.turtlegames.engine.engine.listeners;

import co.turtlegames.core.achievement.AchievementData;
import co.turtlegames.core.achievement.AchievementManager;
import co.turtlegames.core.common.Chat;
import co.turtlegames.core.profile.PlayerProfile;
import co.turtlegames.core.profile.ProfileManager;
import co.turtlegames.engine.engine.GameManager;
import co.turtlegames.engine.engine.state.GameState;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class LobbyEventListener implements Listener {

    private GameManager _gameManager;

    public LobbyEventListener(GameManager gameManager) {
        _gameManager = gameManager;
    }

    @EventHandler
    public void onAction(PlayerInteractEvent event) {

        Player ply = event.getPlayer();

        if(event.getAction() != Action.PHYSICAL)
            return;

        if(_gameManager.getState() != GameState.LOBBY
            && _gameManager.getState() != GameState.INACTIVE)
                return;

        Block clickedBlock = event.getClickedBlock();

        if(clickedBlock == null)
            return;

        if(clickedBlock.getType() == Material.SOIL
                || clickedBlock.getType() == Material.CROPS) {

            this.grantWheatAchievement(ply);

            event.setCancelled(true);
            return;

        }

    }

    @EventHandler
    public void onItemClick(PlayerInteractEvent event) {

        Player ply = event.getPlayer();

        if(event.getAction() == Action.PHYSICAL)
            return;

        if(_gameManager.getState() != GameState.LOBBY
                && _gameManager.getState() != GameState.INACTIVE)
            return;

        ItemStack item = event.getItem();

        if(item == null)
            return;

        if(item.getType() == Material.COMPASS)
            ply.chat("/kit");

    }

    private void grantWheatAchievement(Player ply) {

        AchievementManager achievementManager = _gameManager.getModule(AchievementManager.class);

        PlayerProfile profile = _gameManager.getModule(ProfileManager.class)
                .fetchProfile(ply.getUniqueId())
                .getNow(null);

        if(profile == null)
            return;

        AchievementData data = profile.fetchAchievementData()
                                    .getNow(null);

        if(data == null)
            return;

        data.getAchievementStatus(achievementManager.getAchievementById(4))
                .incrementProgress(1);

    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {

        Player ply = event.getPlayer();

        Block toBlock = event.getTo().getBlock();
        Block fromBlock = event.getFrom().getBlock();

        if(toBlock.getType() == Material.STONE_PLATE
                && fromBlock.getType() != Material.STONE_PLATE) {

            Vector velo = ply.getLocation().getDirection()
                    .multiply(2)
                    .setY(1.0F);

            ply.setVelocity(velo);
            ply.getWorld().playSound(ply.getLocation(), Sound.BAT_TAKEOFF, 1, 1);


        }

    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {

        if(_gameManager.getState() != GameState.LOBBY
                && _gameManager.getState() != GameState.INACTIVE)
            return;

        event.setCancelled(true);

    }

    @EventHandler
    public void onDropItem(PlayerDropItemEvent event) {

        if(_gameManager.getState() != GameState.LOBBY
                && _gameManager.getState() != GameState.INACTIVE)
            return;

        event.setCancelled(true);

    }

    @EventHandler
    public void onHunger(FoodLevelChangeEvent event) {
        event.setFoodLevel(20);
    }

}
