package co.turtlegames.engine.engine.map;

import co.turtlegames.core.TurtleModule;
import co.turtlegames.core.file.minio.FileClusterManager;
import co.turtlegames.core.world.tworld.TurtleWorldFormat;
import co.turtlegames.core.world.tworld.loader.TurtleWorldLoader;
import co.turtlegames.engine.engine.game.GameType;
import co.turtlegames.engine.engine.map.action.FetchAvailableGameMapsAction;
import co.turtlegames.core.world.gen.BarrierGenerator;
import co.turtlegames.core.world.virtual.VirtualWorldManager;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class MapManager extends TurtleModule {

    private MapToken _activeMap = null;
    private World _activeWorld = null;

    public MapManager(JavaPlugin plugin) {
        super(plugin, "Game Map Manager");
    }

    @Override
    public void initializeModule() {

    }

    public MapToken selectNewMap(GameType type) {

        FetchAvailableGameMapsAction mapFetchAction = new FetchAvailableGameMapsAction(type);
        CompletableFuture<List<MapToken>> dbResponseFuture = this.getDatabaseConnector().executeActionAsync(mapFetchAction, true);

        List<MapToken> availableMaps;

        System.out.println(Thread.currentThread().getName());

        try {
            availableMaps = dbResponseFuture.get(20, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException ex) {
            ex.printStackTrace();
            return null;
        }

        if(availableMaps.size() == 0)
            return null;

        MapToken possibleMap;
        Collections.shuffle(availableMaps);

        int i = 0;
        do {

            if(i >= availableMaps.size())
                return null;

            possibleMap = availableMaps.get(i);
            i++;

        } while(!possibleMap.valid(this.getModule(FileClusterManager.class)));

        _activeMap = possibleMap;
        return _activeMap;

    }

    public boolean loadWorld() {

        try {

            TurtleWorldFormat worldFormat = _activeMap.resolveTurtleWorld(this.getModule(FileClusterManager.class));
            _activeWorld = this.loadTurtleWorld(worldFormat);

            return true;

        } catch (IOException ex) {

            ex.printStackTrace();
            return false;

        }

    }

    private World loadTurtleWorld(TurtleWorldFormat worldFormat) throws IOException {

        VirtualWorldManager vwManager = this.getModule(VirtualWorldManager.class);
        TurtleWorldLoader worldLoader = worldFormat.createChunkLoader();

        World world = vwManager.createVirtualWorld(this.createWorldCreator(), worldLoader);

        world.setSpawnFlags(false, false);
        world.setDifficulty(Difficulty.HARD);

        world.setTime(1000);

        world.setGameRuleValue("doDaylightCycle", "false");
        world.setGameRuleValue("doMobSpawning", "false");

        return world;

    }

    private WorldCreator createWorldCreator() {

        WorldCreator creator = new WorldCreator("map_active");

        creator.type(WorldType.NORMAL);
        creator.generator(new BarrierGenerator());

        return creator;

    }

    public MapToken getActiveMap() {
        return _activeMap;
    }

    public World getActiveWorld() {
        return _activeWorld;
    }

}
