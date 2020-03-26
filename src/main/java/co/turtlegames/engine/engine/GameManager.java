package co.turtlegames.engine.engine;

import co.turtlegames.core.TurtleModule;
import co.turtlegames.core.common.Chat;
import co.turtlegames.core.scoreboard.TurtleScoreboardManager;
import co.turtlegames.engine.engine.game.GameType;
import co.turtlegames.engine.engine.listeners.JoinListener;
import co.turtlegames.engine.engine.listeners.LobbyEventListener;
import co.turtlegames.engine.engine.scoreboard.EngineScoreboardView;
import co.turtlegames.engine.engine.state.GameState;
import co.turtlegames.engine.engine.state.IGameState;
import co.turtlegames.engine.engine.state.inst.InactiveGameState;
import co.turtlegames.engine.engine.state.inst.LobbyGameState;
import co.turtlegames.engine.engine.state.inst.ResetGameState;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class GameManager extends TurtleModule {

    private GameState _currentState = GameState.INACTIVE;
    private GameType _gameType = null;

    private Map<GameState, IGameState> _stateHandlers = new HashMap<>();

    public GameManager(JavaPlugin pluginInstance) {

        super(pluginInstance,"Game Manager");

    }

    @Override
    public void initializeModule() {

        Bukkit.getScheduler()
                .scheduleSyncRepeatingTask(this.getPlugin(), this::doTick, 1, 1);

        this.registerStateProvider(GameState.RESET, new ResetGameState(this));
        this.registerStateProvider(GameState.LOBBY, new LobbyGameState(this));
        this.registerStateProvider(GameState.INACTIVE, new InactiveGameState(this));

        this.registerListener(new JoinListener(this));
        this.registerListener(new LobbyEventListener(this));

        this.getModule(TurtleScoreboardManager.class)
                .updateScoreboardView(new EngineScoreboardView(this));

        this.setGame(GameType.DEV_TEST);

    }

    private void setGame(GameType gameType) {

        _gameType = gameType;
        Bukkit.broadcastMessage(Chat.main("Game", "The game was set to " + Chat.elem(gameType.getName())));

        this.switchState(GameState.RESET);

    }

    public void registerStateProvider(GameState state, IGameState provider) {
        _stateHandlers.put(state, provider);
    }

    public void doTick() {

        IGameState gameState = _stateHandlers.get(_currentState);

        if(gameState == null)
            return;

        gameState.doTick();

    }

    public void switchState(GameState gameState) {

        _currentState = gameState;

        _stateHandlers.get(gameState)
                .doInitialTick();

    }

    public IGameState getStateHandle() {
        return _stateHandlers.get(_currentState);
    }

    public GameState getState() {
        return _currentState;
    }

    public GameType getGameType() {
        return _gameType;
    }

}
