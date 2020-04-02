package co.turtlegames.engine.engine.game;

import co.turtlegames.engine.type.dev_test.DevTestGame;
import co.turtlegames.engine.type.dtc.DefendTheCoreGame;

public enum GameType {

    DEV_TEST(DevTestGame.class,"Dev Test"),
    DTC(DefendTheCoreGame.class, "Defend the Core");

    private Class<? extends AbstractGame> _gameClass;
    private String _name;

    GameType(Class<? extends AbstractGame> gameClass, String name) {

        _gameClass = gameClass;
        _name = name;

    }

    public String getName() {
        return _name;
    }

    public Class<? extends AbstractGame>  getGameClass() {
        return _gameClass;
    }

}
