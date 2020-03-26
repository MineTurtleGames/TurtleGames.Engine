package co.turtlegames.engine.engine.game;

import co.turtlegames.core.profile.PlayerProfile;

public class GamePlayer {

    private PlayerProfile _profile;

    public GamePlayer(PlayerProfile playerProfile) {
        _profile = playerProfile;
    }

    public PlayerProfile getPlayerProfile() {
        return _profile;
    }

}
