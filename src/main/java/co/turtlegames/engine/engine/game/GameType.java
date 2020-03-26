package co.turtlegames.engine.engine.game;

public enum GameType {

    DEV_TEST("Dev Test");

    private String _name;

    GameType(String name) {
        _name = name;
    }

    public String getName() {
        return _name;
    }

}
