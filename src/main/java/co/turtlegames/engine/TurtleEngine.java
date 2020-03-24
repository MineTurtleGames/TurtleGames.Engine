package co.turtlegames.engine;

import co.turtlegames.core.TurtleCore;
import co.turtlegames.core.TurtlePlugin;
import co.turtlegames.engine.engine.GameManager;
import co.turtlegames.engine.engine.prevention.PreventionManager;
import co.turtlegames.engine.world.virtual.VirtualWorldManager;

public class TurtleEngine extends TurtlePlugin {

    public TurtleEngine() {
        super("Game Engine");
    }

    @Override
    public void onEnable() {

        TurtleCore core = this.getCoreInstance();

        core.registerModule(new VirtualWorldManager(this));
        core.registerModule(new GameManager(this));
        core.registerModule(new PreventionManager(this));

        this.getCoreInstance().<VirtualWorldManager>getModule(VirtualWorldManager.class).initializeModule();
        this.getCoreInstance().<GameManager>getModule(GameManager.class).initializeModule();

    }

}
