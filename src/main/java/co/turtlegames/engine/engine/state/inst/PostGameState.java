package co.turtlegames.engine.engine.state.inst;

import co.turtlegames.core.scoreboard.TurtlePlayerScoreboard;
import co.turtlegames.engine.engine.GameManager;
import co.turtlegames.engine.engine.game.player.GamePlayer;
import co.turtlegames.engine.engine.prevention.PreventionSet;
import co.turtlegames.engine.engine.state.AbstractStateProvider;
import co.turtlegames.engine.util.TickRate;

public class PostGameState extends AbstractStateProvider {

    private PreventionSet _preventionSet;

    private static final int STATE_TICKS = 10 * 20;
    private int _tickTimer;

    private GameManager _gameManager;

    @Override
    public void doInitialTick() {

    }

    @Override
    public void doTick(TickRate tickRate) {

    }

    @Override
    public void updatePlayerScoreboard(GamePlayer gamePlayer, TurtlePlayerScoreboard scoreboard) {

    }

    @Override
    public PreventionSet getPreventionSet() {
        return _preventionSet;
    }

}
