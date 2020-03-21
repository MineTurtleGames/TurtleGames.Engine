package co.turtlegames.engine.engine;

import co.turtlegames.core.TurtleModule;
import co.turtlegames.engine.engine.state.GameState;
import co.turtlegames.engine.engine.state.IGameState;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class GameManager extends TurtleModule {

    private GameState _currentState = GameState.RESET;
    private Map<GameState, IGameState> _stateProviders = new HashMap<>();

    public GameManager(JavaPlugin ply) {

        super(ply,"Game Manager");

    }

    @Override
    public void initializeModule() {

        Bukkit.getScheduler()
                .scheduleSyncRepeatingTask(this.getPlugin(), this::doTick, 1, 1);

    }

    public void registerStateProvider(GameState state, IGameState provider) {
        _stateProviders.put(state, provider);
    }

    public void doTick() {

        IGameState gameState = _stateProviders.get(_currentState);
        gameState.doTick();

    }

    public void switchState(GameState gameState) {

        _currentState = gameState;

        _stateProviders.get(gameState)
                .doInitialTick();

    }

}
