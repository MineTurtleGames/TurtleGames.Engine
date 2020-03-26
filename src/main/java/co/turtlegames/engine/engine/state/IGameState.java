package co.turtlegames.engine.engine.state;

import co.turtlegames.core.scoreboard.TurtlePlayerScoreboard;
import co.turtlegames.engine.engine.game.GamePlayer;
import co.turtlegames.engine.engine.prevention.PreventionSet;

public interface IGameState {

    void doInitialTick();
    void doTick();

    void updatePlayerScoreboard(GamePlayer gamePlayer, TurtlePlayerScoreboard scoreboard);

    PreventionSet getPreventionSet();


}
