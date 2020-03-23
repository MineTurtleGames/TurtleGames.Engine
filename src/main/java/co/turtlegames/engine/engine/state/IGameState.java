package co.turtlegames.engine.engine.state;

import co.turtlegames.core.scoreboard.TurtlePlayerScoreboard;

public interface IGameState {

    public void doInitialTick();
    public void doTick();

    public void updatePlayerScoreboard(TurtlePlayerScoreboard scoreboard);

}
