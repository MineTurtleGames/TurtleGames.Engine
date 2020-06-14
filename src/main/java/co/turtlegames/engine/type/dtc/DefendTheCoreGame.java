package co.turtlegames.engine.type.dtc;

import co.turtlegames.core.scoreboard.TurtlePlayerScoreboard;
import co.turtlegames.core.util.TRegion;
import co.turtlegames.core.world.tworld.TurtleWorldFormat;
import co.turtlegames.core.world.tworld.TurtleWorldMetaPoint;
import co.turtlegames.engine.engine.GameManager;
import co.turtlegames.engine.engine.game.AbstractGame;
import co.turtlegames.engine.engine.game.player.GamePlayer;
import co.turtlegames.engine.engine.game.GameTeam;
import co.turtlegames.engine.engine.map.MapManager;
import co.turtlegames.engine.type.dtc.kit.FighterKit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.Collection;
import java.util.Iterator;

public class DefendTheCoreGame extends AbstractGame {

    private TRegion _redCore;
    private TRegion[] _redGenerators;

    private TRegion _blueCore;
    private TRegion[] _blueGenerators;

    private int[] _redGenStatus;
    private int[] _blueGenStatus;

    private int _redCoreStatus;
    private int _blueCoreStatus;

    public DefendTheCoreGame(GameManager gameManager) {
        super(gameManager);

        _gameOptions.setWaterDamage(true);

        _gameOptions.setMinPlayers(2);
        _gameOptions.setMaxPlayers(32);

        _gameOptions.setBlockBreakAllowed(true);
        _gameOptions.setBlockPlaceAllowed(true);

        _gameOptions.enableAllDamage();

        _teams.add(new GameTeam(1, "Red", ChatColor.RED));
        _teams.add(new GameTeam(2, "Blue", ChatColor.BLUE));

        _kits.add(new FighterKit());

    }

    @Override
    public void updatePlayerScoreboard(GamePlayer gamePlayer, TurtlePlayerScoreboard playerScoreboard) {

        playerScoreboard.setLine(1, "Defend the core");

    }

    @Override
    public void handleMapConfiguration(TurtleWorldFormat tWorld) {

        World world = this.getGameManager().getModule(MapManager.class)
                            .getActiveWorld();

        TurtleWorldMetaPoint redCore = tWorld.getMetaPoints().get((byte) 66)
                                            .iterator().next();
        _redCore = new TRegion(world, redCore);

        TurtleWorldMetaPoint blueCore = tWorld.getMetaPoints().get((byte) 68)
                .iterator().next();
        _blueCore = new TRegion(world, blueCore);

        Collection<TurtleWorldMetaPoint> redGenerators = tWorld.getMetaPoints().get((byte) 65);
        _redGenerators = (TRegion[]) redGenerators.stream().map((TurtleWorldMetaPoint point) -> new TRegion(world, point)).toArray();

        Collection<TurtleWorldMetaPoint> blueGenerators = tWorld.getMetaPoints().get((byte) 67);
        _blueGenerators = (TRegion[]) blueGenerators.stream().map((TurtleWorldMetaPoint point) -> new TRegion(world, point)).toArray();

        // Generate red structures
        for (Block blk : _redCore.getContents()) {

            blk.setType(Material.WOOL);
            blk.setData((byte) 14);

        }

        for(int i = 0; i <= _redGenerators.length; i++) {

            TRegion genRegion = _redGenerators[i];

            for(Block blk : genRegion.getContents()) {

                blk.setType(Material.WOOL);
                blk.setData((byte) 14);

            }

            _redGenStatus[i] = genRegion.getSize();

        }

        _redCoreStatus =_redCore.getSize();

        // Generate blue structures

        for (Block blk : _blueCore.getContents()) {

            blk.setType(Material.WOOL);
            blk.setData((byte) 11);

        }

        for(int i = 0; i <= _blueGenerators.length; i++) {

            TRegion genRegion = _blueGenerators[i];
            for(Block blk : genRegion.getContents()) {

                blk.setType(Material.WOOL);
                blk.setData((byte) 11);

            }

            _blueGenStatus[i] = genRegion.getSize();

        }

    }

}
