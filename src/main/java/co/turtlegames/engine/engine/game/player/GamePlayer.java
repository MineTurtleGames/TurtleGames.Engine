package co.turtlegames.engine.engine.game.player;

import co.turtlegames.core.profile.PlayerProfile;
import co.turtlegames.core.stats.PlayerStat;
import co.turtlegames.engine.engine.GameManager;
import co.turtlegames.engine.engine.damage.DamageToken;
import co.turtlegames.engine.engine.game.GameTeam;
import co.turtlegames.engine.engine.game.player.progress.DeferredPlayerProgress;
import co.turtlegames.engine.engine.kit.Kit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class GamePlayer {

    private PlayerProfile _profile;

    private GameTeam _team;
    private Kit _kit;

    private DeferredPlayerProgress _deferredProgress;

    private PlayerState _playerState = PlayerState.SPECTATOR;

    private List<DamageToken> _damageTokens = new ArrayList<>();

    public GamePlayer(PlayerProfile playerProfile) {

        _profile = playerProfile;
        _deferredProgress = new DeferredPlayerProgress(this);

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

    public List<DamageToken> getDamageTokens() {
        return _damageTokens;
    }

    public void registerDamageToken(DamageToken token) {
        _damageTokens.add(token);
    }

    public DeferredPlayerProgress getDeferredProgress() {
        return _deferredProgress;
    }

    public void resetDeferredProgress() {
        _deferredProgress = new DeferredPlayerProgress(this);
    }

}
