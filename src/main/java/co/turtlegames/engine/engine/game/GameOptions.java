package co.turtlegames.engine.engine.game;

import co.turtlegames.engine.engine.prevention.PreventionSet;

public class GameOptions extends PreventionSet {

    private int _minPlayers = 2;
    private int _maxPlayers = 16;

    public int getMinPlayers() {
        return _minPlayers;
    }

    public int getMaxPlayers() {
        return _maxPlayers;
    }

    public void setMinPlayers(int minPlayers) {
        _minPlayers = minPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        _maxPlayers = maxPlayers;
    }

}
