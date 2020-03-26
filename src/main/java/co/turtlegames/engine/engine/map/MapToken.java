package co.turtlegames.engine.engine.map;

import co.turtlegames.engine.engine.game.GameType;
import co.turtlegames.engine.world.tworld.TurtleWorldFormat;
import co.turtlegames.engine.world.tworld.io.TurtleInputStream;
import co.turtlegames.engine.world.tworld.loader.TurtleWorldLoader;
import co.turtlegames.engine.world.virtual.VirtualWorldManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;

public class MapToken {

    private int _id;

    private String _name;
    private String[] _description;

    private GameType _game;
    private File _location;

    private WeakReference<TurtleWorldFormat> _turtleWorld;

    public MapToken(int id, String name, String[] description, GameType game, File location) {

        _id = id;
        _name = name;
        _description = description;
        _game = game;
        _location = location;

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

    public File getFileLocation() {
        return _location;
    }

    public boolean valid() {
        return _location.exists()
                && _location.canRead();
    }

    public TurtleWorldFormat resolveTurtleWorld() throws IOException {

        FileInputStream inStream = new FileInputStream(_location);
        TurtleInputStream turtleInStream = new TurtleInputStream(inStream);

        TurtleWorldFormat worldFormat = TurtleWorldFormat.loadFromStream(turtleInStream);
        turtleInStream.close();

        _turtleWorld = new WeakReference<>(worldFormat);
        return worldFormat;

    }


}
