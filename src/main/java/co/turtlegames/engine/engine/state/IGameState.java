package co.turtlegames.engine.engine.state;

import co.turtlegames.core.scoreboard.TurtlePlayerScoreboard;
import co.turtlegames.engine.engine.game.player.GamePlayer;
import co.turtlegames.engine.engine.prevention.PreventionSet;
import co.turtlegames.engine.util.TickRate;

public interface IGameState {

    void doInitialTick();
    void doTick(TickRate tickRate);

    void updatePlayerScoreboard(GamePlayer gamePlayer, TurtlePlayerScoreboard scoreboard);

    PreventionSet getPreventionSet();


}
