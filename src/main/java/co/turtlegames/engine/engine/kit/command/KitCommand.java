package co.turtlegames.engine.engine.kit.command;

import co.turtlegames.core.TurtleModule;
import co.turtlegames.core.command.CommandBase;
import co.turtlegames.core.common.Chat;
import co.turtlegames.core.profile.PlayerProfile;
import co.turtlegames.core.profile.Rank;
import co.turtlegames.engine.engine.GameManager;
import co.turtlegames.engine.engine.kit.Kit;
import co.turtlegames.engine.engine.kit.menu.KitMenu;
import co.turtlegames.engine.engine.state.GameState;
import org.bukkit.entity.Player;

public class KitCommand extends CommandBase<GameManager> {

    public KitCommand(GameManager module) {
        super(module, Rank.PLAYER, "kit", "k");
    }

    @Override
    public void executeCommand(PlayerProfile profile, String[] args) {

        Player ply = profile.getOwner();

        if(this.getModule().getState() != GameState.LOBBY) {

            ply.sendMessage(Chat.main("Error", "You can not change your kit at this time"));
            return;

        }

        KitMenu kitMenu = new KitMenu(this.getModule(), profile.getOwner());
        kitMenu.open();

    }

}
