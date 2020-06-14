package co.turtlegames.engine.engine.kit.menu;

import co.turtlegames.core.menu.Menu;
import co.turtlegames.engine.engine.GameManager;
import org.bukkit.entity.Player;

public class KitMenu extends Menu<GameManager> {

    public KitMenu(GameManager module, Player owner) {
        super(module, "Kit", owner);

        this.addPage(new KitPage(this));

    }

}
