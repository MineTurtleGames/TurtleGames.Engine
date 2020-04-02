package co.turtlegames.engine.engine.kit;

import co.turtlegames.engine.engine.game.GameType;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.ArrayList;

public abstract class Kit implements Listener {

    private String _name;
    private String[] _description;
    private int _coinPrice;

    private ArrayList<Player> _players = new ArrayList<>();

    public Kit(String name, String[] description, int coinPrice) {
        _name = name;
        _description = description;
        _coinPrice = coinPrice;
    }

    public void apply(Player player) {

        _players.add(player);
        handleApply(player);

    }

    public abstract void handleApply(Player player);

    public String getName() {
        return _name;
    }

    public int getCoinPrice() {
        return _coinPrice;
    }

    public ArrayList<Player> getPlayers() {
        return _players;
    }
}
