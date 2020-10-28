package co.turtlegames.engine.engine.game;

import co.turtlegames.core.scoreboard.TurtlePlayerScoreboard;
import co.turtlegames.core.util.UtilDev;
import co.turtlegames.core.world.tworld.TurtleWorldFormat;
import co.turtlegames.engine.engine.GameManager;
import co.turtlegames.engine.engine.damage.DamageToken;
import co.turtlegames.engine.engine.game.player.GamePlayer;
import co.turtlegames.engine.engine.game.player.progress.DeferredAward;
import co.turtlegames.engine.engine.kit.Kit;
import co.turtlegames.engine.engine.state.GameState;
import co.turtlegames.engine.util.GameConstant;
import co.turtlegames.engine.util.TickRate;
import co.turtlegames.engine.util.UtilEntity;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.*;
import java.util.stream.Collectors;

public abstract class AbstractGame implements Listener {

    private GameManager _gameManager;

    protected GameOptions _gameOptions = new GameOptions();

    protected Set<GameTeam> _teams = new HashSet<>();
    protected List<Kit> _kits = new ArrayList<>();

    public AbstractGame(GameManager gameManager) {
        _gameManager = gameManager;
    }

    public abstract void updatePlayerScoreboard(GamePlayer gamePlayer, TurtlePlayerScoreboard playerScoreboard);

    public GameOptions getGameOptions() {
        return _gameOptions;
    }

    public Set<GameTeam> getTeams() {
        return _teams;
    }

    public List<Kit> getKits() {
        return _kits;
    }

    public GameManager getGameManager() {
        return _gameManager;
    }

    public void handleTick(TickRate tickRate) {

        if(tickRate == TickRate.SECOND
                && _gameOptions.hasWaterDamage())
            this.doWaterDamage();

    }

    private void doWaterDamage() {

        for(GamePlayer gPlayer : _gameManager.getGamePlayers()) {

            Player ply = gPlayer.getPlayer();

            Material mat = ply.getLocation().getBlock().getType();

            if(mat == Material.WATER
                    || mat == Material.STATIONARY_WATER) {
                EntityDamageEvent event = UtilEntity.damage(ply, 3);
                gPlayer.registerDamageToken(new DamageToken(System.currentTimeMillis(), event, "Water Damage"));
            }

        }

    }

    private void endGame(IEndMessageProvider messageProvider) {

        _gameManager.stopGameTimer();

        HandlerList.unregisterAll(this);
        for(Kit k : _kits) {

            k.resetStates();
            HandlerList.unregisterAll(k);

        }

        double gameDurationMinutes = (_gameManager.getGameDuration() * 1.0d)/(1000 * 60);
        UtilDev.alert(UtilDev.AlertLevel.LOG, "Game duration: " + gameDurationMinutes + " minutes");
        UtilDev.alert(UtilDev.AlertLevel.LOG, _gameManager.getGameDuration() + "ms");

        long participationXp = (long) Math.ceil(gameDurationMinutes * GameConstant.PART_XP_PER_MINUTE);
        double participationCoins = gameDurationMinutes * GameConstant.PART_COINS_PER_MINUTE;

        for(Player ply : Bukkit.getOnlinePlayers()) {

            GamePlayer gamePlayer = _gameManager.getGamePlayer(ply, false);

            gamePlayer.getDeferredProgress()
                    .addAward(new DeferredAward("Participation", participationXp, participationCoins));

            ply.sendMessage(ChatColor.DARK_GREEN.toString() + ChatColor.BOLD + "-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
            ply.sendMessage("");

            for(String str : messageProvider.get(gamePlayer))
                ply.sendMessage("    " + str);

            ply.sendMessage( "");
            ply.sendMessage(ChatColor.DARK_GREEN.toString() + ChatColor.BOLD + "-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");

        }

        if(_gameManager.getState() != GameState.IN_GAME)
            return;

        _gameManager.switchState(GameState.POST_GAME);

    }

    public void endGame(String reason) {
        this.endGame((gamePlayer) -> new String[] { reason });
    }

    public void endGameWithTeam(GameTeam team) {

        String victoryTagline = "Well done!";
        String lossTagline = "Better luck next time!";

        String neutralTagline = "You were a spectator";

        for(GamePlayer gamePlayer : _gameManager.getGamePlayers()) {

            if(gamePlayer.getTeam() == team)
                gamePlayer.getDeferredProgress().addAward(new DeferredAward("Victory", 1000, 100));

        }

        this.endGame((gamePlayer) -> {

            if(team == null) {

                return new String[] {
                    ChatColor.YELLOW + "" + ChatColor.BOLD + "Stalemate!",
                        lossTagline
                };

            }

            String tagline;
            if(gamePlayer != null && gamePlayer.getTeam() != null) {

                if(gamePlayer.getTeam() == team)
                    tagline = victoryTagline;
                else
                    tagline = lossTagline;

            } else {
                tagline = neutralTagline;
            }

            return new String[] {
                team.getColour() + "" + ChatColor.BOLD + team.getName() + " won the game!",
                    ChatColor.GRAY + tagline
            };

        });

    }

    public void endGameWithPlayer(GamePlayer player) {

    }

    public void registerEvents() {

        Bukkit.getPluginManager().registerEvents(this, this.getGameManager().getPlugin());

        for(Kit k : _kits)
            Bukkit.getPluginManager().registerEvents(k, this.getGameManager().getPlugin());

    }

    public void handlePreGameStart() {}
    public void handleGameStart() {}

    public void handleMapConfiguration(TurtleWorldFormat tWorld) {}

    public void handleGameEnd() {}

    public Collection<GamePlayer> getParticipants() {

        return _gameManager.getGamePlayers().stream()
                .filter((gamePlayer) -> gamePlayer.getTeam() != null)
                .collect(Collectors.toList());

    }

}
