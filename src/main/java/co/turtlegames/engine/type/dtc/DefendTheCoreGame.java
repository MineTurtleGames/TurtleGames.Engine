package co.turtlegames.engine.type.dtc;

import co.turtlegames.core.common.Chat;
import co.turtlegames.core.scoreboard.TurtlePlayerScoreboard;
import co.turtlegames.core.util.TRegion;
import co.turtlegames.core.util.UtilString;
import co.turtlegames.core.world.tworld.TurtleWorldFormat;
import co.turtlegames.core.world.tworld.TurtleWorldMetaPoint;
import co.turtlegames.engine.engine.GameManager;
import co.turtlegames.engine.engine.game.AbstractGame;
import co.turtlegames.engine.engine.game.player.GamePlayer;
import co.turtlegames.engine.engine.game.GameTeam;
import co.turtlegames.engine.engine.game.player.PlayerState;
import co.turtlegames.engine.engine.map.MapManager;
import co.turtlegames.engine.type.dtc.kit.ArcherKit;
import co.turtlegames.engine.type.dtc.kit.BomberKit;
import co.turtlegames.engine.type.dtc.kit.FighterKit;
import co.turtlegames.engine.util.TickRate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class DefendTheCoreGame extends AbstractGame {

    protected final GameTeam RED_TEAM = new GameTeam(1, "Red", ChatColor.RED);
    protected final GameTeam BLUE_TEAM = new GameTeam(2, "Blue", ChatColor.BLUE);

    private DTCCore _redCore;
    private DTCGenerator[] _redGenerators;

    private DTCCore _blueCore;
    private DTCGenerator[] _blueGenerators;

    private int _ticksRemaining = 15 * 20 * 60;

    public DefendTheCoreGame(GameManager gameManager) {
        super(gameManager);

        _gameOptions.setWaterDamage(true);

        _gameOptions.setMinPlayers(2);
        _gameOptions.setMaxPlayers(32);

        _gameOptions.setBlockBreakAllowed(true);
        _gameOptions.setBlockPlaceAllowed(true);

        _gameOptions.enableAllDamage();

        _teams.add(RED_TEAM);
        _teams.add(BLUE_TEAM);

        _kits.add(new FighterKit());
        _kits.add(new ArcherKit());
        _kits.add(new BomberKit(this));

    }

    @Override
    public void updatePlayerScoreboard(GamePlayer gamePlayer, TurtlePlayerScoreboard playerScoreboard) {

        playerScoreboard.setTitle(ChatColor.GOLD + "" + ChatColor.BOLD + "DEFEND THE CORE");
        playerScoreboard.setLine(1, ChatColor.GRAY + "turtlegames.co - a0.1");
        playerScoreboard.setLine(2, "");

        playerScoreboard.setLine(3, ChatColor.RED + "" + ChatColor.BOLD + "Red Status");

        playerScoreboard.setLine(4, "Generator 1: " + _redGenerators[0].getScoreStatus());
        playerScoreboard.setLine(5, "Generator 2: " + _redGenerators[1].getScoreStatus());

        String redCoreStatus;
        if(this.isShielded(RED_TEAM))
            redCoreStatus = ChatColor.AQUA + "Shielded";
        else
            redCoreStatus = _redCore.getScoreStatus();

        playerScoreboard.setLine(6, "Core: " + ChatColor.GOLD + redCoreStatus);

        playerScoreboard.setLine(7, "");

        playerScoreboard.setLine(8, ChatColor.BLUE + "" + ChatColor.BOLD + "Blue Status");
        playerScoreboard.setLine(9, "Generator 1: " + _blueGenerators[0].getScoreStatus());
        playerScoreboard.setLine(10, "Generator 2: " + _blueGenerators[1].getScoreStatus());

        String blueCoreStatus;
        if(this.isShielded(BLUE_TEAM))
            blueCoreStatus = ChatColor.AQUA + "Shielded";
        else
            blueCoreStatus = _blueCore.getScoreStatus();

        playerScoreboard.setLine(11, "Core: " + blueCoreStatus);

        playerScoreboard.setLine(12, "");
        playerScoreboard.setLine(13, "Time left: " + ChatColor.YELLOW + UtilString.formatTime((_ticksRemaining / 20) * 1000));

    }

    @Override
    public void handleMapConfiguration(TurtleWorldFormat tWorld) {

        for(TurtleWorldMetaPoint b : tWorld.getMetaPoints().values())
            System.out.println(b);

        World world = this.getGameManager().getModule(MapManager.class)
                            .getActiveWorld();

        TurtleWorldMetaPoint redCore = tWorld.getMetaPoints().get((byte) 66)
                                            .iterator().next();
        _redCore = new DTCCore(this, new TRegion(world, redCore), RED_TEAM);

        TurtleWorldMetaPoint blueCore = tWorld.getMetaPoints().get((byte) 68)
                .iterator().next();
        _blueCore = new DTCCore(this, new TRegion(world, blueCore), BLUE_TEAM);

        Collection<TurtleWorldMetaPoint> redGenerators = tWorld.getMetaPoints().get((byte) 65);
        _redGenerators = redGenerators.stream().map((TurtleWorldMetaPoint point) -> new DTCGenerator(this, new TRegion(world, point), RED_TEAM)).toArray(DTCGenerator[]::new);

        Collection<TurtleWorldMetaPoint> blueGenerators = tWorld.getMetaPoints().get((byte) 67);
        _blueGenerators = blueGenerators.stream().map((TurtleWorldMetaPoint point) -> new DTCGenerator(this, new TRegion(world, point), BLUE_TEAM)).toArray(DTCGenerator[]::new);

    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {

        Block blk = event.getBlock();
        Player ply = event.getPlayer();

        if(blk.getType() == Material.WOOD
            || blk.getType() == Material.STONE)
                return;

        event.setCancelled(true);
        ply.sendMessage(Chat.main("Game", "You can only break blocks placed by players"));

    }

    @EventHandler
    public void onCraft(CraftItemEvent event) {
        event.setCancelled(true); // Nice try achievement??
    }

    @EventHandler
    public void onExplosion(EntityExplodeEvent event) {
        event.blockList().removeIf((block) -> block.getType() != Material.WOOD && block.getType() != Material.STONE);
    }

    public boolean isShielded(GameTeam team) {

        DTCGenerator[] targetGenerators;
        if(team.getColour() == ChatColor.RED)
            targetGenerators = _redGenerators;
        else
            targetGenerators = _blueGenerators;

        for(DTCGenerator gen : targetGenerators) {

            if(!gen.isDestroyed())
                return true;

        }

        return false;

    }

    @Override
    public void handleTick(TickRate tickRate) {

        super.handleTick(tickRate);

        _ticksRemaining--;

        if(_ticksRemaining == 0) {

            World world = this.getGameManager().getModule(MapManager.class)
                    .getActiveWorld();

            for(DTCGenerator redGen : _redGenerators)
                world.strikeLightningEffect(redGen.getMidpoint(world));

            for(DTCGenerator blueGen : _blueGenerators)
                world.strikeLightningEffect(blueGen.getMidpoint(world));

            Bukkit.broadcastMessage(Chat.main("Game", "The generators begin to lose their charge..."));

        } else if(_ticksRemaining < 0) {

            for(DTCGenerator redGen : _redGenerators) {
                if(Math.random() < 0.25)
                    redGen.attemptDamage();
            }

            for(DTCGenerator blueGen : _blueGenerators) {
                if(Math.random() < 0.25)
                    blueGen.attemptDamage();
            }

        }

        for(GamePlayer gamePlayer : this.getParticipants()) {

            Player ply = gamePlayer.getPlayer();

            if(!ply.isBlocking())
                continue;

            if(gamePlayer.getState() != PlayerState.ALIVE)
                continue;

            Block block = ply.getTargetBlock((Set<Material>) null, 4);

            if(block == null)
                continue;

            if(block.getType() != Material.WOOL
                    && block.getType() != Material.COAL_BLOCK)
                        continue;

            DTCCore targetCore = gamePlayer.getTeam() == RED_TEAM ? _blueCore : _redCore;

            if(targetCore.contains(block)) {

                targetCore.attemptDamage();
                continue;

            }

            DTCGenerator[] targetGenerators;
            if(gamePlayer.getTeam().getColour() == ChatColor.RED)
                targetGenerators = _blueGenerators;
            else
                targetGenerators = _redGenerators;

            for(DTCGenerator gen : targetGenerators) {

                if(!gen.contains(block))
                    continue;

                gen.attemptDamage();

            }

        }

    }

}
