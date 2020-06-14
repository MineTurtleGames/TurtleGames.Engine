package co.turtlegames.engine.engine;

import co.turtlegames.core.TurtleModule;
import co.turtlegames.core.common.Chat;
import co.turtlegames.core.profile.PlayerProfile;
import co.turtlegames.core.profile.ProfileManager;
import co.turtlegames.core.scoreboard.TurtleScoreboardManager;
import co.turtlegames.core.util.ItemBuilder;
import co.turtlegames.core.world.tworld.TurtleWorldFormat;
import co.turtlegames.core.world.tworld.TurtleWorldMetaPoint;
import co.turtlegames.engine.engine.command.GameCommand;
import co.turtlegames.engine.engine.damage.DamageManager;
import co.turtlegames.engine.engine.game.*;
import co.turtlegames.engine.engine.game.player.GamePlayer;
import co.turtlegames.engine.engine.game.player.PlayerState;
import co.turtlegames.engine.engine.kit.command.KitCommand;
import co.turtlegames.engine.engine.listeners.JoinLeaveListener;
import co.turtlegames.engine.engine.listeners.LobbyEventListener;
import co.turtlegames.engine.engine.map.MapManager;
import co.turtlegames.engine.engine.map.MapToken;
import co.turtlegames.engine.engine.scoreboard.EngineScoreboardView;
import co.turtlegames.engine.engine.state.AbstractStateProvider;
import co.turtlegames.engine.engine.state.GameState;
import co.turtlegames.engine.engine.state.IGameState;
import co.turtlegames.engine.engine.state.inst.*;
import co.turtlegames.engine.util.TickRate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.collect.Multimap;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import sun.plugin.dom.exception.InvalidStateException;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class GameManager extends TurtleModule {

    public static final Location LOBBY_POS = new Location(Bukkit.getWorld("world"), -115.5, 130, -107.5);;

    private GameState _currentState = GameState.INACTIVE;
    private GameType _gameType = null;

    private DamageManager _damageManager;

    private AbstractGame _gameInstance = null;

    private Map<GameState, AbstractStateProvider> _stateHandlers = new HashMap<>();
    private Map<UUID, GamePlayer> _gamePlayers = new HashMap<>();

    private boolean _forceStart = false;

    private int _i = 0;

    public GameManager(JavaPlugin pluginInstance) {

        super(pluginInstance,"Game Manager");
        _damageManager = new DamageManager(this);

    }

    @Override
    public void initializeModule() {

        Bukkit.getScheduler()
                .scheduleSyncRepeatingTask(this.getPlugin(), this::doTick, 1, 1);

        this.registerStateProvider(GameState.RESET, new ResetGameState(this));
        this.registerStateProvider(GameState.LOBBY, new LobbyGameState(this));
        this.registerStateProvider(GameState.INACTIVE, new InactiveGameState(this));
        this.registerStateProvider(GameState.PRE_GAME, new PreGameState(this));
        this.registerStateProvider(GameState.IN_GAME, new InGameState(this));

        this.registerListener(new JoinLeaveListener(this));
        this.registerListener(new LobbyEventListener(this));

        this.registerCommand(new GameCommand(this));
        this.registerCommand(new KitCommand(this));

        this.getModule(TurtleScoreboardManager.class)
                .updateScoreboardView(new EngineScoreboardView(this));

        this.setGame(GameType.DTC);

    }

    public void setGame(GameType gameType) {

        if (_currentState != GameState.INACTIVE && _currentState != GameState.LOBBY)
            throw new InvalidStateException("Attempting to change game while game in progress.");

        _gameType = gameType;
        try {
            _gameInstance = gameType.getGameClass().getConstructor(GameManager.class)
                                .newInstance(this);
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {

            ex.printStackTrace();
            this.switchState(GameState.INACTIVE);

            return;

        }

        Bukkit.broadcastMessage(Chat.main("Game", "The game was set to " + Chat.elem(gameType.getName())));

        this.switchState(GameState.RESET);

    }

    public void registerStateProvider(GameState state, AbstractStateProvider provider) {
        _stateHandlers.put(state, provider);
    }

    public void doTick() {

        IGameState gameState = _stateHandlers.get(_currentState);

        if(gameState == null)
            return;

        TickRate tickRate = TickRate.TICK;
        _i++;

        if(_i % (20 * 60) == 0) {
            tickRate = TickRate.MINUTE;
            _i = 0;
        } else if(_i % 20 == 0)
            tickRate = TickRate.SECOND;

        gameState.doTick(tickRate);

    }

    public void switchState(GameState gameState) {

        HandlerList.unregisterAll(_stateHandlers.get(_currentState));
        _currentState = gameState;

        getPlugin().getServer().getPluginManager().registerEvents(_stateHandlers.get(_currentState), getPlugin());

        _stateHandlers.get(gameState)
                .doInitialTick();

        this.getModule(TurtleScoreboardManager.class)
                .updateAll(true);

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

    public GameOptions getGameOptions() {
        return _gameInstance.getGameOptions();
    }

    public boolean canStart() {

        if(_gameInstance == null)
            return false;

        return Bukkit.getOnlinePlayers().size() >= this.getGameOptions().getMinPlayers() || _forceStart;

    }

    public GamePlayer getGamePlayer(Player ply, boolean forcedValue) {

        if(_gamePlayers.containsKey(ply.getUniqueId()))
            return _gamePlayers.get(ply.getUniqueId());

        CompletableFuture<PlayerProfile> profileFuture =
                this.getModule(ProfileManager.class)
                        .fetchProfile(ply.getUniqueId());

        PlayerProfile profile;

        if(!profileFuture.isDone()) {

            if(!forcedValue)
                return null;

            try {
                profile = profileFuture.get();
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
                return null;
            }

        } else {
            profile = profileFuture.getNow(null);
        }

        GamePlayer gamePlayer = new GamePlayer(profile);

        _gamePlayers.put(ply.getUniqueId(), gamePlayer);
        return gamePlayer;

    }

    private void preparePlayers() {

        Iterator<GameTeam> teamIterator = Iterables.cycle(_gameInstance.getTeams())
                                                .iterator();

        for(Player ply : Bukkit.getOnlinePlayers()) {

            GamePlayer gamePlayer = this.getGamePlayer(ply, true);

            gamePlayer.setTeam(teamIterator.next());
            gamePlayer.switchState(PlayerState.ALIVE);

            if(gamePlayer.getKit() == null)
                gamePlayer.setKit(_gameInstance.getKits().get(0));
            gamePlayer.getKit().apply(ply);

            ply.setWalkSpeed(0);
            ply.setHealth(20);
            ply.setGameMode(GameMode.SURVIVAL);

            ply.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 250, false, false));

            ply.sendMessage(ChatColor.DARK_GREEN.toString() + ChatColor.BOLD + "-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
            ply.sendMessage("");
            ply.sendMessage("    " + ChatColor.GREEN + ChatColor.BOLD.toString() + _gameType.getName());
            ply.sendMessage("    " + ChatColor.GRAY + "This is the description");
            ply.sendMessage("    " + ChatColor.GRAY + "Description line 2");

            ply.sendMessage(" ");

            ply.sendMessage("    " + ChatColor.GRAY + "Map: " + ChatColor.GOLD + "Jeff");

            ply.sendMessage(" ");

            ply.sendMessage("    " + ChatColor.GRAY + "Kit: " + ChatColor.GOLD + "Jeff");
            ply.sendMessage("    " + ChatColor.GRAY + "Team: " + ChatColor.RED + "Red");
            ply.sendMessage("");
            ply.sendMessage(ChatColor.DARK_GREEN.toString() + ChatColor.BOLD + "-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");

        }

    }

    private void warpPlayersToWorld() {

        MapManager mapManager = this.getModule(MapManager.class);
        MapToken token = mapManager.getActiveMap();

        TurtleWorldFormat tWorld = token.getTurtleWorld();

        Multimap<Byte, TurtleWorldMetaPoint> metaPoints = tWorld.getMetaPoints();
        Map<GameTeam, Iterator<TurtleWorldMetaPoint>> spawnPoints = new HashMap<>();

        for(GameTeam team : _gameInstance.getTeams())
            spawnPoints.put(team, Iterators.cycle(metaPoints.get(team.getId())));

        for(GamePlayer gamePlayer : _gamePlayers.values()) {

            TurtleWorldMetaPoint point = spawnPoints.get(gamePlayer.getTeam())
                                            .next();

            Player ply = gamePlayer.getPlayerProfile().getOwner();

            ply.teleport(point.getPrimaryPosition().toLocation(mapManager.getActiveWorld()));

        }

    }

    public void startPreGame() {

        this.preparePlayers();
        this.warpPlayersToWorld();

        this.switchState(GameState.PRE_GAME);

    }

    public void startGame() {

        this.removePlayerRestraints();
        this.switchState(GameState.IN_GAME);

    }

    public void removePlayerRestraints() {

        for(Player ply : Bukkit.getOnlinePlayers()) {

            ply.setWalkSpeed(0.2f);
            ply.removePotionEffect(PotionEffectType.JUMP);

        }

    }

    public Location findApplicableRespawnPoint(GamePlayer gPlayer) {

        MapManager mapManager = this.getModule(MapManager.class);
        MapToken token = mapManager.getActiveMap();

        TurtleWorldFormat tWorld = token.getTurtleWorld();

        return tWorld.getMetaPoints().get(gPlayer.getTeam().getId())
                    .iterator().next().getPrimaryPosition().toLocation(mapManager.getActiveWorld());

    }

    public void giveLobbyItems(Player ply) {

        Inventory inv = ply.getInventory();
        inv.clear();

        inv.setItem(0, new ItemBuilder(Material.COMPASS, ChatColor.GOLD + "Select Kit").build());
        inv.setItem(4, new ItemBuilder(Material.CHEST, ChatColor.LIGHT_PURPLE + "Funbox").build());
        inv.setItem(8, new ItemBuilder(Material.BED, ChatColor.GRAY + "Return to hub").build());

    }

    public AbstractGame getGameInstance() {
        return _gameInstance;
    }

    public Collection<GamePlayer> getGamePlayers() {
        return _gamePlayers.values();
    }

    public void purgeGamePlayer(Player player) {
        _gamePlayers.remove(player.getUniqueId());
    }

    public DamageManager getDamageManager() {
        return _damageManager;
    }

    public void setForceStart(boolean forceStart) {
        _forceStart = forceStart;
    }
}
