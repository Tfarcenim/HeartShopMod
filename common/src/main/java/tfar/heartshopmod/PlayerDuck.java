package tfar.heartshopmod;

import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

import java.util.UUID;

public interface PlayerDuck {

    UUID uuid = UUID.fromString("547f2d4a-8238-40ff-8cc1-b9d69883bf9b");

    default int getHeartCurrency() {
        AttributeModifier modifier = getPlayer().getAttribute(Attributes.MAX_HEALTH).getModifier(uuid);
        return modifier != null ? (int) (modifier.getAmount() / 2) : 0;
    }

    default void setHeartCurrency(int hearts) {
        getPlayer().getAttribute(Attributes.MAX_HEALTH).removePermanentModifier(uuid);
        getPlayer().getAttribute(Attributes.MAX_HEALTH).addPermanentModifier(create(hearts * 2));
    }

    default void reset() {
        setHeartCurrency(0);
    }

    default void addHeartCurrency(int hearts) {
        setHeartCurrency(getHeartCurrency() + hearts);
    }

    default Player getPlayer() {
        return (Player) this;
    }

    static AttributeModifier create(double health) {
        return new AttributeModifier(uuid,"HeartShopMod",health, AttributeModifier.Operation.ADDITION);
    }
}
