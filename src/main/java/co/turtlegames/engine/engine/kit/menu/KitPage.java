package co.turtlegames.engine.engine.kit.menu;

import co.turtlegames.core.common.Chat;
import co.turtlegames.core.menu.Page;
import co.turtlegames.core.util.ItemBuilder;
import co.turtlegames.engine.engine.GameManager;
import co.turtlegames.engine.engine.game.player.GamePlayer;
import co.turtlegames.engine.engine.kit.Kit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Objects;

public class KitPage extends Page<KitMenu> {

    public KitPage(KitMenu menu) {

        super(menu, 3);

        GameManager gameManager = this.getMenu().getModule();
        Collection<Kit> kits = gameManager.getGameInstance().getKits();

        int slot = 10;
        for(Kit kit : kits) {

            this.addButton(slot, kit.createIcon(), (page, event) -> {

                Player ply = Objects.requireNonNull(this.getMenu().getOwner().get());

                GamePlayer gamePlayer = gameManager.getGamePlayer(ply,
                                                            false);

                gamePlayer.setKit(kit);
                ply.sendMessage(Chat.main("Kit", "You have equipped " + Chat.elem(kit.getName())));

                ply.playSound(ply.getLocation(), Sound.ORB_PICKUP, 1, 1);

            });

        }

    }

}
