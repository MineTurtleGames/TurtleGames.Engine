package co.turtlegames.engine.util;

import org.bukkit.Material;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UtilGameString {

    private static String enumVanity(Enum en) {
        return Stream.of(en.toString().split("_"))
                .map((String word) -> word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase())
                .collect(Collectors.joining(" "));
    }

    public static String vanity(EntityDamageEvent.DamageCause cause) {
        return UtilGameString.enumVanity(cause);
    }

    public static String vanity(ItemStack itemInHand) {

        if(itemInHand == null
            || itemInHand.getType() == Material.AIR)
                return "Fists";

        if(itemInHand.hasItemMeta()
            && itemInHand.getItemMeta().hasDisplayName())
                return itemInHand.getItemMeta().getDisplayName();

        return UtilGameString.enumVanity(itemInHand.getType());

    }

}
