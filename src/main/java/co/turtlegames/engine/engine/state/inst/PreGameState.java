package co.turtlegames.engine.engine.state.inst;

import co.turtlegames.core.scoreboard.TurtlePlayerScoreboard;
import co.turtlegames.engine.engine.GameManager;
import co.turtlegames.engine.engine.game.player.GamePlayer;
import co.turtlegames.engine.engine.prevention.PreventionSet;
import co.turtlegames.engine.engine.state.AbstractStateProvider;
import co.turtlegames.engine.util.TickRate;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

public class PreGameState extends AbstractStateProvider {

    private PreventionSet _preventionSet = new PreventionSet();
    private static final int STATE_TICKS = 10 * 20;

    private int _tickTimer;

    private GameManager _gameManager;

    public PreGameState(GameManager gameManager) {
        _gameManager = gameManager;
    }

    @Override
    public void doInitialTick() {
        _tickTimer = STATE_TICKS;
    }

    @Override
    public void doTick(TickRate tickRate) {

        for (Player ply : Bukkit.getOnlinePlayers())
            ply.setGameMode(GameMode.SURVIVAL);

        _tickTimer--;

        if(_tickTimer <= 0) {

            for(Player ply : Bukkit.getOnlinePlayers())
                ply.playSound(ply.getLocation(), Sound.LEVEL_UP, 1, 1);

            _gameManager.startGame();

        } else if(_tickTimer % 20 == 0) {

            for(Player ply : Bukkit.getOnlinePlayers())
                ply.playSound(ply.getLocation(), Sound.NOTE_STICKS, 1, 1);

        }

    }

    @Override
    public void updatePlayerScoreboard(GamePlayer gamePlayer, TurtlePlayerScoreboard scoreboard) {

    }

    @Override
    public PreventionSet getPreventionSet() {
        return null;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event)
    {

        Location from = event.getFrom();
        Location to = event.getTo();

        to.setX(from.getX());
        to.setY(from.getY());
        to.setZ(from.getZ());

        event.setTo(to);

    }

}
