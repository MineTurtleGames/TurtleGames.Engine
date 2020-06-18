package co.turtlegames.engine.engine.game.player.progress;

import co.turtlegames.engine.engine.game.player.GamePlayer;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class DeferredPlayerProgress {

    private GamePlayer _player;

    private Map<GameStat, Long> _gameStats = new HashMap<>();
    private Map<String, DeferredAward> _awards = new HashMap<>();

    public DeferredPlayerProgress(GamePlayer player) {
        _player = player;
    }

    public void addAward(DeferredAward award) {

        if(_awards.containsKey(award.getName())) {

            _awards.get(award.getName()).incrementCount(1);
            return;

        }

        _awards.put(award.getName(), award);

    }

    public void incrementAward(DeferredAward award, int amount) {

        if(_awards.containsKey(award.getName()))
            _awards.get(award.getName()).incrementCount(amount);
        else {

            award.incrementCount(amount);
            _awards.put(award.getName(), award);
        }

    }

    public void pushStat(GameStat stat, long amount) {
        _gameStats.merge(stat, amount, Long::sum);
    }

    public Collection<DeferredAward> getAwards() {
        return _awards.values();
    }

}
