package co.turtlegames.engine.engine.state.inst;

import co.turtlegames.core.scoreboard.TurtlePlayerScoreboard;
import co.turtlegames.engine.engine.GameManager;
import co.turtlegames.engine.engine.state.IGameState;
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

        /*scoreboard.setTitle(ChatColor.DARK_GREEN.toString() + ChatColor.BOLD + "Turtle" + ChatColor.GREEN + ChatColor.BOLD + "Games");

        scoreboard.setLine(0, ChatColor.GREEN + ChatColor.BOLD.toString() + "Game");
        scoreboard.setLine(1, "Game name");

        scoreboard.setLine(3, ChatColor.RED + ChatColor.BOLD.toString() + "Kit");
        scoreboard.setLine(4, "Kit name");

        scoreboard.setLine(15, ChatColor.RED + ChatColor.BOLD.toString() + "The game will start");*/

    }

}
