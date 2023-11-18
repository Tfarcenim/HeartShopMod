package tfar.heartshopmod;

import net.minecraft.world.entity.player.Player;

public interface PlayerDuck {

    default int getHeartCurrency() {
        return (int) getPlayer().getAbsorptionAmount()/2;
    }

    default void setHeartCurrency(int hearts) {
        if (!getPlayer().level().isClientSide) {
            getPlayer().setAbsorptionAmount(hearts * 2);
        } else {
            HeartShopMod.LOG.warn("Attempted to modify heart count on client");
            new Throwable().printStackTrace();
        }
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

}
