package co.turtlegames.engine.engine.state.inst;

import co.turtlegames.core.common.Chat;
import co.turtlegames.core.currency.CurrencyData;
import co.turtlegames.core.currency.CurrencyType;
import co.turtlegames.core.profile.PlayerProfile;
import co.turtlegames.core.scoreboard.TurtlePlayerScoreboard;
import co.turtlegames.core.util.UtilString;
import co.turtlegames.core.util.UtilXp;
import co.turtlegames.engine.engine.GameManager;
import co.turtlegames.engine.engine.game.player.GamePlayer;
import co.turtlegames.engine.engine.prevention.PreventionSet;
import co.turtlegames.engine.engine.state.AbstractStateProvider;
import co.turtlegames.engine.util.TickRate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

public class LobbyGameState extends AbstractStateProvider {

    private PreventionSet _preventionSet;
    private static final int STATE_TICKS = 60 * 20;

    private GameManager _gameManager;

    private int _tickTimer;

    public LobbyGameState(GameManager gameManager) {

        _gameManager = gameManager;
        _preventionSet = new PreventionSet();

    }

    @Override
    public PreventionSet getPreventionSet() {
        return _preventionSet;
    }

    @Override
    public void doInitialTick() {

        _tickTimer = STATE_TICKS;

    }

    @Override
    public void doTick(TickRate tickRate) {

        if(!_gameManager.canStart()) {

            _tickTimer = STATE_TICKS;
            return;

        }

        _tickTimer--;

        Sound sound = null;
        boolean flag = false;

        if(_tickTimer == 0) {
            _gameManager.startPreGame();
        } else if(_tickTimer <= 20 * 5 && _tickTimer % 20 == 0) {

            sound = Sound.CLICK;
            flag = true;

        } else if(_tickTimer % (20 * 10) == 0) {

            sound = Sound.ORB_PICKUP;
            flag = true;

        }

        if(!flag)
            return;

        int seconds = (int) Math.ceil(_tickTimer/20f);

        for(Player ply : Bukkit.getOnlinePlayers())
            ply.playSound(ply.getLocation(), sound, 1, 1);

        Bukkit.broadcastMessage(Chat.main("Game", "The game will start in " + Chat.elem(seconds + " seconds")));

    }

    @Override
    public void updatePlayerScoreboard(GamePlayer player, TurtlePlayerScoreboard scoreboard) {

        PlayerProfile profile = player.getPlayerProfile();

        scoreboard.setTitle(ChatColor.DARK_GREEN.toString() + ChatColor.BOLD + "Turtle"
                + ChatColor.GREEN + ChatColor.BOLD + "Games");

        scoreboard.setLine(1, "");

        scoreboard.setLine(2, ChatColor.WHITE + "Game: " + ChatColor.YELLOW + _gameManager.getGameType().getName());
        scoreboard.setLine(3, ChatColor.WHITE + "Players: " + ChatColor.YELLOW + Bukkit.getOnlinePlayers().size() + "/8");

        scoreboard.setLine(4, "");

        CurrencyData cD = profile.getCurrencyData();
        String coinLine = ChatColor.RED + "Not loaded";

        if(cD != null)
            coinLine = UtilString.formatInteger(cD.getBalance(CurrencyType.COINS)) + " coins";

        scoreboard.setLine(5, ChatColor.WHITE + "Balance: " + ChatColor.YELLOW + coinLine);
        //scoreboard.setLine(6, ChatColor.WHITE + "Rank: " + profile.getRank().getColor() + profile.getRank().getName());
        scoreboard.setLine(6, ChatColor.WHITE + "" + profile.hashCode());

        scoreboard.setLine(7, "");

        scoreboard.setLine(8, "Server: " + ChatColor.YELLOW + "Dev-1");

        scoreboard.setLine(9, "");

        scoreboard.setLine(10, ChatColor.WHITE + "Level: " + ChatColor.AQUA + UtilXp.getLevel(profile.getXp()));
        scoreboard.setLine(11,  UtilXp.drawXpBar(profile.getXp(), 14));

        scoreboard.setLine(12, "");
        scoreboard.setLine(13, ChatColor.GREEN + ChatColor.BOLD.toString() + this.getStatusMessage());

    }

    public String getStatusMessage() {

        if(_gameManager.canStart()) {
            return "Game will start in " + (int) Math.ceil(_tickTimer/20f);
        } else {
            return "Waiting for players";
        }

    }


    public void setTimerTicks(int i) {
        _tickTimer = i;
    }

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event) {
        event.setCancelled(true);
    }

}
