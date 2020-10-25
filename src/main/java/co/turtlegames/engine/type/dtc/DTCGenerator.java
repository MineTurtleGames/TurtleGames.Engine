package co.turtlegames.engine.type.dtc;

import co.turtlegames.core.common.Chat;
import co.turtlegames.core.util.TRegion;
import co.turtlegames.engine.engine.game.GameTeam;
import co.turtlegames.engine.engine.game.player.GamePlayer;
import co.turtlegames.engine.engine.map.MapManager;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.material.MaterialData;
import org.bukkit.util.Vector;

import java.util.Collections;
import java.util.Set;
import java.util.Stack;

public class DTCGenerator {

    private int _ticksPerBlock = 20;

    private DefendTheCoreGame _dtcInstance;

    private TRegion _region;
    private GameTeam _team;

    private int _status;
    private int _ticks;

    private Stack<Block> _blockStack;

    public DTCGenerator(DefendTheCoreGame instance, TRegion region, GameTeam team) {

        _dtcInstance = instance;

        _region = region;
        _team = team;

        _status = region.getSize();
        _ticks = _ticksPerBlock;

        _blockStack = new Stack<>();

        _blockStack.addAll(region.getContents());
        Collections.shuffle(_blockStack);

        this.generate();

    }

    private void generate() {

        _blockStack.forEach((b) -> {

            b.setType(Material.WOOL);

            if(_team.getColour() == ChatColor.RED)
                b.setData((byte) 14);
            if(_team.getColour() == ChatColor.BLUE)
                b.setData((byte) 11);

        });

    }

    public void attemptDamage() {

        if(this.isDestroyed())
            return;

        _ticks--;

        if(_ticks > 0)
            return;

        _ticks = _ticksPerBlock;

        if(_status <= 0) {

            this.destroy();
            return;

        }

        _status--;
        Block toModify = _blockStack.pop();

        toModify.setType(Material.COAL_BLOCK);
        toModify.getWorld().playEffect(toModify.getLocation(), Effect.STEP_SOUND, 35);

    }

    public void destroy() {

        _status = -1;
        _blockStack.clear();

        for(Block blk : _region.getContents())
            blk.setType(Material.GLASS);

        Vector midpoint = _region.getMinimumPosition().midpoint(_region.getMaximumPosition());
        World world = _dtcInstance.getGameManager().getModule(MapManager.class).getActiveWorld();

        world.createExplosion(midpoint.getX(), midpoint.getY(), midpoint.getZ(), 5f, false, false);
        for(Player ply : Bukkit.getOnlinePlayers()) {

            ply.playSound(ply.getLocation(), Sound.ENDERDRAGON_GROWL, 1, 1);

        }

        Bukkit.broadcastMessage(Chat.main("DTC", _team .getColour() + _team.getName() + "'s generator has been destroyed!"));

    }

    public TRegion getRegion() {
        return _region;
    }

    public boolean contains(Block block) {
        return _region.contains(block);
    }

    public boolean isDestroyed() {
        return _status < 0;
    }

    public String getScoreStatus() {

        if(this.isDestroyed())
            return ChatColor.RED + "Destroyed";

        int percentage = (_status * 100)/ _region.getSize();
        return ChatColor.GREEN + "" + percentage + "%";

    }

    public DefendTheCoreGame getDtcInstance() {
        return _dtcInstance;
    }

    public GameTeam getTeam() {
        return _team;
    }

    public int getStatus() {
        return _status;
    }

    public int getTicks() {
        return _ticks;
    }

    public Stack<Block> getBlockStack() {
        return _blockStack;
    }

    public Location getMidpoint(World world) {
        return _region.getMinimumPosition().midpoint(_region.getMaximumPosition()).toLocation(world);
    }

}
