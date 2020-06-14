package co.turtlegames.engine.engine.damage;

import co.turtlegames.core.common.Chat;
import co.turtlegames.core.util.UtilString;
import co.turtlegames.engine.engine.GameManager;
import co.turtlegames.engine.engine.damage.listener.GameDamageHandle;
import co.turtlegames.engine.engine.game.player.GamePlayer;
import co.turtlegames.engine.engine.game.player.PlayerState;
import co.turtlegames.engine.engine.visiblity.VisibilityManager;
import co.turtlegames.engine.util.TickRate;
import co.turtlegames.engine.util.UtilGameString;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.*;

public class DamageManager {

    private class DeathToken {

        private GamePlayer _dead;
        private long _deathTime;

        public DeathToken(GamePlayer dead, long deathTime) {
            _dead = dead;
            _deathTime = deathTime;
        }

        public GamePlayer getDead() {
            return _dead;
        }

        public long getDeathTime() {
            return _deathTime;
        }

    }

    private GameManager _gameManager;
    private Set<DeathToken> _deathTokens;

    public DamageManager(GameManager gameManager) {

        _gameManager = gameManager;
        _deathTokens = new HashSet<>();

        Bukkit.getPluginManager().registerEvents(new GameDamageHandle(this), gameManager.getPlugin());

    }

    public void killPlayer(GamePlayer gPlayer) {

        if(gPlayer.getState() != PlayerState.ALIVE)
            return;

        long deathTime = System.currentTimeMillis();

        gPlayer.switchState(PlayerState.DEAD);
        _deathTokens.add(new DeathToken(gPlayer, deathTime));

        List<DamageToken> damageTokens = gPlayer.getDamageTokens();

        Player ply = gPlayer.getPlayer();

        int maxIndex = damageTokens.size() - 1;
        for(int i = 0; i < damageTokens.size(); i++) {

            if(i > 10) {

                ply.sendMessage(ChatColor.GRAY + " ... and " + (maxIndex - i)  + " older events");
                break;

            }
            DamageToken token = damageTokens.get(i);

            String timeSinceDamage = UtilString.formatTime(deathTime - token.getDamageTime());

            String damageCause;

            if (token.getCustomCause() != null) {
                damageCause = token.getCustomCause();
            } else {
                damageCause = UtilGameString.vanity(token.getEvent().getCause());
            }

            ply.sendMessage(ChatColor.DARK_GREEN + ChatColor.BOLD.toString() + "Damage Log: "
                    + ChatColor.GREEN + timeSinceDamage + " - " + damageCause + ": -"
                    + ChatColor.RED + token.getEvent().getDamage()/2 + " ♡");
        }
        damageTokens.clear();

        this.doDeathEffect(ply);

    }

    private void respawnPlayer(DeathToken token) {

        Player ply = token.getDead().getPlayer();

        if(ply == null)
            return;

        token.getDead().switchState(PlayerState.ALIVE);
        token.getDead().getKit().apply(ply);

        ply.setAllowFlight(false);
        ply.setFlying(false);

        ply.setFallDistance(0);
        ply.setFireTicks(0);

        _gameManager.getModule(VisibilityManager.class)
                .showPlayer(ply);

        ply.sendMessage(Chat.main("Dead", "You have respawned!"));
        ply.teleport(_gameManager.findApplicableRespawnPoint(token.getDead()));

    }

    private void doDeathEffect(Player ply) {

        long respawnTime = _gameManager.getGameOptions().getDeathTime();

        ply.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 10, 100));

        ply.setAllowFlight(true);
        ply.setFlying(true);

        ply.setFallDistance(0);
        ply.setFireTicks(0);

        ply.getWorld().playSound(ply.getLocation(), Sound.SUCCESSFUL_HIT, 1, 1);

        ply.teleport(ply.getLocation().add(new Vector(0, 5, 0)));

        ply.sendMessage(ChatColor.DARK_GREEN.toString() + ChatColor.BOLD + "You died. "
                + ChatColor.GREEN + ChatColor.BOLD.toString() + "Don't sweat it - you'll respawn in "
                + ChatColor.GOLD + ChatColor.BOLD.toString() + UtilString.formatTime(respawnTime));

        _gameManager.getModule(VisibilityManager.class)
                .hidePlayer(ply);

    }

    public void doGameTick(TickRate rate) {

        if(rate != TickRate.SECOND)
            return;

        this.validateDeadSet();

    }

    private void validateDeadSet() {

        long curTimeMs = System.currentTimeMillis();
        long deathTime = _gameManager.getGameOptions().getDeathTime();


        Iterator<DeathToken> tokenIterator = _deathTokens.iterator();

        while(tokenIterator.hasNext()) {

            DeathToken token = tokenIterator.next();

            if((curTimeMs - token.getDeathTime()) < deathTime)
                continue;

            this.respawnPlayer(token);
            tokenIterator.remove();

        }

    }

    public GameManager getGameManager() {
        return _gameManager;
    }

    public String generateDeathMessage(EntityDamageEvent event) {

        Player dead = (Player) event.getEntity();
        String deathMessage = Chat.main("Death", Chat.elem(dead.getDisplayName()));

        if(event instanceof EntityDamageByEntityEvent) {

            EntityDamageByEntityEvent dbeEvent = (EntityDamageByEntityEvent) event;

            if(dbeEvent.getEntity() instanceof Player) {

                Player killer = (Player) dbeEvent.getDamager();

                deathMessage += " was killed by " + Chat.elem(killer.getDisplayName());
                deathMessage += " using " + Chat.elem(UtilGameString.vanity(killer.getItemInHand()));

            } else {
                deathMessage += " was killed by " + Chat.elem(dbeEvent.getDamager().getName());
            }

        } else {
            deathMessage += " was killed by " + Chat.elem(UtilGameString.vanity(event.getCause()));
        }

        return deathMessage;

    }

}
