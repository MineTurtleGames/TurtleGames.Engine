package co.turtlegames.engine.world.tworld.test;

import co.turtlegames.core.TurtleModule;
import org.bukkit.plugin.java.JavaPlugin;

public class TurtleWorldTestModule extends TurtleModule {

    public TurtleWorldTestModule(JavaPlugin plugin) {
        super(plugin, "TWorld Test Module");
    }

    @Override
    public void initializeModule() {

        this.registerCommand(new TurtleWorldTestCommand(this));

    }

}
