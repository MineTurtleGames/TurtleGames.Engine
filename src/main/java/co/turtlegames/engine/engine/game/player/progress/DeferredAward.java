package co.turtlegames.engine.engine.game.player.progress;

import co.turtlegames.core.util.UtilString;
import org.bukkit.ChatColor;

public class DeferredAward {

    private String _name;

    private int _eventCount = 1;

    private long _xp;
    private double _coins;

    public DeferredAward(String name, long xp, double coins) {
        _name = name;
        _xp = xp;
        _coins = coins;
    }

    public String getName() {
        return _name;
    }

    public int getEventCount() {
        return _eventCount;
    }

    public void incrementCount(int amount) {
        _eventCount += amount;
    }

    public long getXp() {
        return _xp;
    }

    public double getCoins() {
        return _coins;
    }

    public long calculateXpAward() {
        return this.getEventCount() * this.getXp();
    }

    public double calculateCoinAward() {
        return this.getEventCount() * this.getCoins();
    }

    public String compileMessage() {

        StringBuilder builder = new StringBuilder(ChatColor.GREEN + "");

        if(_eventCount > 1)
            builder.append(_eventCount + "x ");

        builder.append(_name);

        double coinAmount = this.calculateCoinAward();
        long xpAmount = this.calculateXpAward();

        if(xpAmount > 0)
            builder.append(ChatColor.AQUA + " +" + UtilString.formatInteger((int) xpAmount) + " XP");
        if(coinAmount > 0)
            builder.append(ChatColor.GOLD + " +" + UtilString.formatInteger((int) Math.floor(coinAmount)) + " coins");

        return builder.toString();

    }
}
