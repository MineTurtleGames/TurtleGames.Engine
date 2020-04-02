package co.turtlegames.engine.engine.game;

import org.bukkit.ChatColor;

public class GameTeam {

    private byte _id;

    private String _name;
    private ChatColor _colour;

    public GameTeam(byte id, String name, ChatColor colour) {

        _id = id;

        _name = name;
        _colour = colour;

    }

    public GameTeam(int id, String name, ChatColor colour) {
        this((byte) id, name, colour);
    }

    public byte getId() {
        return _id;
    }

    public String getName() {
        return _name;
    }

    public ChatColor getColour() {
        return _colour;
    }

}
