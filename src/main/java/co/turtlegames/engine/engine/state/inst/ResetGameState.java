package co.turtlegames.engine.engine.state.inst;

import co.turtlegames.core.common.Chat;
import co.turtlegames.core.currency.CurrencyData;
import co.turtlegames.core.currency.CurrencyType;
import co.turtlegames.core.profile.PlayerProfile;
import co.turtlegames.core.profile.ProfileManager;
import co.turtlegames.core.scoreboard.TurtlePlayerScoreboard;
import co.turtlegames.core.util.UtilString;
import co.turtlegames.core.util.UtilXp;
import co.turtlegames.engine.engine.GameManager;
import co.turtlegames.engine.engine.game.AbstractGame;
import co.turtlegames.engine.engine.game.player.GamePlayer;
import co.turtlegames.engine.engine.game.player.progress.DeferredAward;
import co.turtlegames.engine.engine.game.player.progress.DeferredPlayerProgress;
import co.turtlegames.engine.engine.map.MapManager;
import co.turtlegames.engine.engine.map.MapToken;
import co.turtlegames.engine.engine.prevention.PreventionSet;
import co.turtlegames.engine.engine.state.AbstractStateProvider;
import co.turtlegames.engine.engine.state.GameState;
import co.turtlegames.engine.util.TickRate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import java.util.Collection;

public class ResetGameState extends AbstractStateProvider {

    private PreventionSet _preventionSet;

    private GameManager _gameManager;

    public ResetGameState(GameManager gameManager) {

        _gameManager = gameManager;

        _preventionSet = new PreventionSet();

    }

    @Override
    public PreventionSet getPreventionSet() {
        return _preventionSet;
    }

    @Override
    public void doInitialTick() {

        _gameManager.removePlayerRestraints();

        MapManager mapManager = _gameManager.getModule(MapManager.class);
        MapToken token = mapManager.selectNewMap(_gameManager.getGameType());

        if(token == null) {

            Bukkit.broadcastMessage(Chat.main("Map", "No suitable maps were found for the running game."));
            _gameManager.switchState(GameState.INACTIVE);

            return;

        }

        if(!mapManager.loadWorld()) {

            _gameManager.switchState(GameState.RESET);
            return;

        }

        Bukkit.broadcastMessage(Chat.main("Map", "The map was set to " + Chat.elem(token.getName()) + " by" + Chat.elem("TurtleGames")));

        try {

            AbstractGame gameInstance = _gameManager.getGameInstance();
            gameInstance.handleMapConfiguration(token.getTurtleWorld());

        } catch(Exception ex) {

            ex.printStackTrace();
            Bukkit.broadcastMessage(Chat.main("Error", "Failed to provide sufficient detail to game from map:\n" + ex.getMessage()));

            _gameManager.switchState(GameState.INACTIVE);
            return;

        }

        for(Player ply : Bukkit.getOnlinePlayers()) {

            ply.teleport(GameManager.LOBBY_POS);
            _gameManager.giveLobbyItems(ply);

            GamePlayer gamePlayer = _gameManager.getGamePlayer(ply, false);

            if(gamePlayer == null)
                continue;

            ply.sendMessage(ChatColor.DARK_GREEN.toString() + ChatColor.BOLD + "-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
            ply.sendMessage("");
            ply.sendMessage("    " + ChatColor.DARK_GREEN + ChatColor.BOLD + "REWARDS");

            DeferredPlayerProgress progress = gamePlayer.getDeferredProgress();

            if(progress != null && progress.getAwards().size() > 0) {

                long totalXp = 0;
                double totalCoins = 0;

                Collection<DeferredAward> awards = progress.getAwards();
                for(DeferredAward award : awards) {

                    ply.sendMessage("    " + award.compileMessage());

                    totalCoins += award.calculateCoinAward();
                    totalXp += award.calculateXpAward();

                }

                PlayerProfile playerProfile = gamePlayer.getPlayerProfile();
                int xpRequired = (int) ((int) UtilXp.getXpRequired(UtilXp.getLevel(playerProfile.getXp()) + 1) - (playerProfile.getXp() + totalXp));

                playerProfile.addXp(totalXp);

                playerProfile.fetchCurrencyData().thenAccept((CurrencyData currencyData) -> {
                    // TODO why not atomic?!?!?!?!?!?
                    currencyData.setBalance(CurrencyType.COINS, currencyData.getBalance(CurrencyType.COINS));
                });

                ply.sendMessage("\n    " + ChatColor.AQUA + "+" + UtilString.formatInteger((int) totalXp) + " XP"
                        + ChatColor.GOLD + " +" + UtilString.formatInteger((int) totalCoins) + " coins");

                ply.sendMessage("\n    " + ChatColor.AQUA + UtilString.formatInteger(xpRequired) + " XP to next level");

            } else {
                ply.sendMessage("    " + ChatColor.GRAY + "... no rewards");
            }

            ply.sendMessage("");
            ply.sendMessage(ChatColor.DARK_GREEN.toString() + ChatColor.BOLD + "-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");

        }

        _gameManager.switchState(GameState.LOBBY);


    }

    @Override
    public void doTick(TickRate tickRate) { }

    @Override
    public void updatePlayerScoreboard(GamePlayer gamePlayer, TurtlePlayerScoreboard scoreboard) {

    }

}
