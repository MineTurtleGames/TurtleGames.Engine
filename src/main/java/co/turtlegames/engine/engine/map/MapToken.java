package co.turtlegames.engine.engine.map;

import co.turtlegames.core.file.FileClusterManager;
import co.turtlegames.core.world.tworld.TurtleWorldFormat;
import co.turtlegames.core.world.tworld.io.TurtleInputStream;
import co.turtlegames.engine.engine.game.GameType;
import org.bukkit.World;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;

public class MapToken {

    private int _id;

    private String _name;
    private String[] _description;

    private GameType _game;
    private String _fileName;

    private WeakReference<TurtleWorldFormat> _turtleWorld;

    public MapToken(int id, String name, String[] description, GameType game, String fileName) {

        _id = id;
        _name = name;
        _description = description;
        _game = game;
        _fileName = fileName;

    }

    public int getId() {
        return _id;
    }

    public String getName() {
        return _name;
    }

    public String[] getDescription() {
        return _description;
    }

    public GameType getGame() {
        return _game;
    }

    public TurtleWorldFormat getTurtleWorld() {
        return _turtleWorld.get();
    }

    public boolean valid(FileClusterManager clusterManager) {

        try {
            return clusterManager.doesFileExist("map", _fileName);
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }

    }

    public TurtleWorldFormat resolveTurtleWorld(FileClusterManager clusterManager) throws IOException {

        try (InputStream stream = clusterManager.grabFileStream("map", _fileName)) {

            TurtleWorldFormat format = TurtleWorldFormat.loadFromStream(new TurtleInputStream(stream));
            _turtleWorld = new WeakReference<>(format);

            return format;

        }

    }

}
