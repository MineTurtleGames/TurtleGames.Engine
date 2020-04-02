package co.turtlegames.engine.engine.game;

import co.turtlegames.engine.engine.prevention.PreventionSet;

public class GameOptions extends PreventionSet {

    private int _minPlayers = 2;
    private int _maxPlayers = 16;

    private long _deathTime = 5000;

    private boolean _waterDamage = false;

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

    public boolean hasWaterDamage() {
        return _waterDamage;
    }

    public void setWaterDamage(boolean b) {
        _waterDamage = b;
    }

    public void enableAllDamage() {

        this.setDamageEnabled(true);

        this.setPveEnabled(true);
        this.setPvpEnabled(true);
        this.setEvpEnabled(true);

    }

    public long getDeathTime() {
        return _deathTime;
    }

}
