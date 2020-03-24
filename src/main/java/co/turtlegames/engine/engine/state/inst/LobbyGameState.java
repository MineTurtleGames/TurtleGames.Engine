package co.turtlegames.engine.engine.state.inst;

import co.turtlegames.core.scoreboard.TurtlePlayerScoreboard;
import co.turtlegames.engine.engine.GameManager;
import co.turtlegames.engine.engine.scoreboard.ScoreboardTitleAnimation;
import co.turtlegames.engine.engine.state.IGameState;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class LobbyGameState implements IGameState {

    private static final int STATE_TICKS = 60 * 20;

    private GameManager _gameManager;

    private int _tickTimer;

    public LobbyGameState(GameManager gameManager) {

        _gameManager = gameManager;

    }

    @Override
    public void doInitialTick() {
        _tickTimer = STATE_TICKS;
    }

    @Override
    public void doTick() {

        _tickTimer--;

    }

    @Override
    public void updatePlayerScoreboard(TurtlePlayerScoreboard scoreboard) {

        scoreboard.setTitle(ChatColor.DARK_GREEN.toString() + ChatColor.BOLD + "Turtle"
                + ChatColor.GREEN.toString() + ChatColor.BOLD + "Games");

        scoreboard.setLine(1, ChatColor.RED + ChatColor.BOLD.toString() + "Game");
        scoreboard.setLine(2, "Game name");

        scoreboard.setLine(3, "");

        scoreboard.setLine(4, ChatColor.GREEN + ChatColor.BOLD.toString() + "Kit");
        scoreboard.setLine(5, "Kit name");

        scoreboard.setLine(6, "");

        scoreboard.setLine(7, ChatColor.BLUE + ChatColor.BOLD.toString() + "Players");
        scoreboard.setLine(8, Bukkit.getOnlinePlayers().size() + "/" + "8");

        scoreboard.setLine(9, "");

        scoreboard.setLine(15, ChatColor.GREEN + ChatColor.BOLD.toString() + "Game start in 15 seconds");

    }

}
