package co.turtlegames.engine.world.tworld.test;

import co.turtlegames.core.command.CommandBase;
import co.turtlegames.core.common.Chat;
import co.turtlegames.core.profile.PlayerProfile;
import co.turtlegames.core.profile.Rank;
import co.turtlegames.engine.world.tworld.TurtleWorldFormat;
import co.turtlegames.engine.world.tworld.io.TurtleInputStream;
import co.turtlegames.engine.world.tworld.io.TurtleOutputStream;
import co.turtlegames.engine.world.tworld.loader.TurtleWorldLoader;
import co.turtlegames.engine.world.virtual.VirtualWorldManager;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class TurtleWorldTestCommand extends CommandBase<TurtleWorldTestModule> {

    public TurtleWorldTestCommand(TurtleWorldTestModule module) {
        super(module, Rank.ADMINISTRATOR, "tworlddebug");
    }

    @Override
    public void executeCommand(PlayerProfile profile, String[] args) {

        Player ply = profile.getOwner();

        File toWriteDir = getModule().getPlugin().getDataFolder();

        if(!toWriteDir.exists())
            toWriteDir.mkdir();

        File toWrite = new File(toWriteDir, "test.tworld");

        TurtleWorldFormat format;
        ply.sendMessage(Chat.main("Test", "Begin test #1 - load from chunks in memory"));

        try {
            format = TurtleWorldFormat.loadFromLoadedChunks(ply.getWorld());
        } catch (Exception ex) {

            ex.printStackTrace();
            ply.sendMessage(Chat.main("Test", "Test #1 - failed: " + ex.getMessage()));
            return;

        }

        ply.sendMessage(Chat.main("Test", "Begin test #2 - save to file"));

        try {
            format.write(new TurtleOutputStream(new FileOutputStream(toWrite)));
        } catch (Exception ex) {

            ex.printStackTrace();
            ply.sendMessage(Chat.main("Test", "Test #2 - failed: " + ex.getMessage()));
            return;

        }

        ply.sendMessage(Chat.main("Test", "Begin test #3 - read from file"));

        TurtleWorldFormat tWorldLoaded;

        try {
            tWorldLoaded = TurtleWorldFormat.loadFromStream(new TurtleInputStream(new FileInputStream(toWrite)));
        } catch (Exception ex) {

            ex.printStackTrace();
            ply.sendMessage(Chat.main("Test", "Test #3 - failed: " + ex.getMessage()));
            return;

        }

        VirtualWorldManager manager = this.getModule()
                                            .getModule(VirtualWorldManager.class);

        World world = manager.createVirtualWorld("test_jeff", new TurtleWorldLoader(tWorldLoaded));
        ply.teleport(world.getSpawnLocation());

    }

}
