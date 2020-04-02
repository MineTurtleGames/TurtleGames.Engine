package co.turtlegames.engine.engine.game.player;

import co.turtlegames.core.profile.PlayerProfile;
import co.turtlegames.core.stats.PlayerStat;
import co.turtlegames.engine.engine.GameManager;
import co.turtlegames.engine.engine.game.GameTeam;
import co.turtlegames.engine.engine.kit.Kit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class GamePlayer {

    private PlayerProfile _profile;

    private GameTeam _team;
    private Kit _kit;

    private PlayerState _playerState = PlayerState.SPECTATOR;

    public GamePlayer(PlayerProfile playerProfile) {
        _profile = playerProfile;
    }

    public PlayerProfile getPlayerProfile() {
        return _profile;
    }

    public void setTeam(GameTeam team) {
        _team = team;
    }

    public GameTeam getTeam() {
        return _team;
    }

    public Player getPlayer() {
        return _profile.getOwner();
    }

    public Kit getKit() {
        return _kit;
    }

    public void setKit(Kit kit) {
        _kit = kit;
    }

    private void setState(PlayerState state) {
        _playerState = state;
    }

    public void switchState(PlayerState state) {
        this.setState(state);
    }

    public PlayerState getState() {
        return _playerState;
    }

}
