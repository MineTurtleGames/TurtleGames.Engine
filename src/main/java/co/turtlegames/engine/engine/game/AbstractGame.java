package co.turtlegames.engine.engine.game;

import co.turtlegames.core.scoreboard.TurtlePlayerScoreboard;
import co.turtlegames.engine.engine.GameManager;
import co.turtlegames.engine.engine.game.player.GamePlayer;
import co.turtlegames.engine.engine.game.player.PlayerState;
import co.turtlegames.engine.engine.kit.Kit;
import co.turtlegames.engine.util.TickRate;
import co.turtlegames.engine.util.UtilEntity;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class AbstractGame {

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
                    || mat == Material.STATIONARY_WATER)
                UtilEntity.damage(ply, 3);

        }

    }

}
