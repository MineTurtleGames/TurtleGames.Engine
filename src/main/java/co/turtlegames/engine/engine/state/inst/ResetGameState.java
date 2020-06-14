package co.turtlegames.engine.engine.state.inst;

import co.turtlegames.core.common.Chat;
import co.turtlegames.core.scoreboard.TurtlePlayerScoreboard;
import co.turtlegames.engine.engine.GameManager;
import co.turtlegames.engine.engine.game.player.GamePlayer;
import co.turtlegames.engine.engine.map.MapManager;
import co.turtlegames.engine.engine.map.MapToken;
import co.turtlegames.engine.engine.prevention.PreventionSet;
import co.turtlegames.engine.engine.state.AbstractStateProvider;
import co.turtlegames.engine.engine.state.GameState;
import co.turtlegames.engine.util.TickRate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ResetGameState extends AbstractStateProvider {

    private PreventionSet _preventionSet;

    private GameManager _gameManager;

    public ResetGameState(GameManager gameManager) {

        _gameManager = gameManager;

        _preventionSet = new PreventionSet();

    }

    @Override
    public PreventionSet getPreventionSet() {
        return _preventionSet;
    }

    @Override
    public void doInitialTick() {

        _gameManager.removePlayerRestraints();

        MapManager mapManager = _gameManager.getModule(MapManager.class);
        MapToken token = mapManager.selectNewMap(_gameManager.getGameType());

        if(token == null) {

            Bukkit.broadcastMessage(Chat.main("Map", "No suitable maps were found for the running game."));
            _gameManager.switchState(GameState.INACTIVE);

            return;

        }

        if(!mapManager.loadWorld()) {

            _gameManager.switchState(GameState.RESET);
            return;

        }

        Bukkit.broadcastMessage(Chat.main("Map", "The map was set to " + Chat.elem(token.getName()) + " by" + Chat.elem("TurtleGames")));

        for(Player ply : Bukkit.getOnlinePlayers()) {

            ply.teleport(GameManager.LOBBY_POS);
            _gameManager.giveLobbyItems(ply);

        }


        _gameManager.switchState(GameState.LOBBY);


    }

    @Override
    public void doTick(TickRate tickRate) { }

    @Override
    public void updatePlayerScoreboard(GamePlayer gamePlayer, TurtlePlayerScoreboard scoreboard) {

    }

}
