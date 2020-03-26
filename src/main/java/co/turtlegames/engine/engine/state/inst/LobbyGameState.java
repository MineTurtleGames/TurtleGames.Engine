package co.turtlegames.engine.engine.state.inst;

import co.turtlegames.core.currency.CurrencyType;
import co.turtlegames.core.profile.PlayerProfile;
import co.turtlegames.core.profile.ProfileManager;
import co.turtlegames.core.scoreboard.TurtlePlayerScoreboard;
import co.turtlegames.core.util.UtilString;
import co.turtlegames.core.util.UtilXp;
import co.turtlegames.engine.engine.GameManager;
import co.turtlegames.engine.engine.game.GamePlayer;
import co.turtlegames.engine.engine.prevention.PreventionSet;
import co.turtlegames.engine.engine.scoreboard.ScoreboardTitleAnimation;
import co.turtlegames.engine.engine.state.IGameState;
import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

public class LobbyGameState implements IGameState {

    private PreventionSet _preventionSet;
    private static final int STATE_TICKS = 60 * 20;

    private GameManager _gameManager;

    private int _tickTimer;

    public LobbyGameState(GameManager gameManager) {

        _gameManager = gameManager;
        _preventionSet = new PreventionSet();

    }

    @Override
    public PreventionSet getPreventionSet() {
        return _preventionSet;
    }

    @Override
    public void doInitialTick() {

        _tickTimer = STATE_TICKS;

        BukkitTask actionBarTask;

        actionBarTask = Bukkit.getScheduler().runTaskTimer(_gameManager.getPlugin(), () -> {

            PacketPlayOutChat packet = new PacketPlayOutChat(new ChatComponentText(ChatColor.GOLD + "►►   Season 1   ◄◄"), (byte) 2);

            for (Player player : Bukkit.getOnlinePlayers())
                ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);

        }, 0L, 20L);

    }

    @Override
    public void doTick() {

        _tickTimer--;

    }

    @Override
    public void updatePlayerScoreboard(GamePlayer player, TurtlePlayerScoreboard scoreboard) {

        PlayerProfile profile = player.getPlayerProfile();

        scoreboard.setTitle(ChatColor.DARK_GREEN.toString() + ChatColor.BOLD + "Turtle"
                                + ChatColor.GREEN + ChatColor.BOLD + "Games");

        scoreboard.setLine(1, "");

        scoreboard.setLine(2, ChatColor.WHITE + "Game: " + ChatColor.YELLOW + _gameManager.getGameType().getName());
        scoreboard.setLine(3, ChatColor.WHITE + "Players: " + ChatColor.YELLOW + Bukkit.getOnlinePlayers().size() + "/8");

        scoreboard.setLine(4, "");

        int coinBalance = profile.getCurrencyData().getBalance(CurrencyType.COINS);
        scoreboard.setLine(5, ChatColor.WHITE + "Balance: " + ChatColor.YELLOW + UtilString.formatInteger(coinBalance) + " coins");
        scoreboard.setLine(6, ChatColor.WHITE + "Rank: " + ChatColor.YELLOW + profile.getRank().getName());

        scoreboard.setLine(7, "");

        scoreboard.setLine(8, "Server: " + ChatColor.YELLOW + "Dev-1");

        scoreboard.setLine(9, "");

        scoreboard.setLine(10, ChatColor.WHITE + "Level: " + ChatColor.AQUA + UtilXp.getLevel(profile.getXp()));
        scoreboard.setLine(11,  UtilXp.drawXpBar(profile.getXp(), 20));

        scoreboard.setLine(12, "");
        scoreboard.setLine(13, ChatColor.GREEN + ChatColor.BOLD.toString() + "Starting in 15 seconds");

    }

}
