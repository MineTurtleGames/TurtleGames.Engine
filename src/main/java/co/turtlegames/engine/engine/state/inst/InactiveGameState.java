package co.turtlegames.engine.engine.state.inst;

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

public class InactiveGameState extends AbstractStateProvider {

    private PreventionSet _preventionSet;
    private GameManager _gameManager;

    private int _tickTimer;

    public InactiveGameState(GameManager gameManager) {

        _gameManager = gameManager;
        _preventionSet = new PreventionSet();

    }

    @Override
    public void doInitialTick() {}

    @Override
    public void doTick(TickRate tickRate) {}

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
        scoreboard.setLine(11,  UtilXp.drawXpBar(profile.getXp(), 20));

        scoreboard.setLine(12, "");
        scoreboard.setLine(13, ChatColor.RED + ChatColor.BOLD.toString() + "Game is paused");

    }

    @Override
    public PreventionSet getPreventionSet() {
        return _preventionSet;
    }

}
