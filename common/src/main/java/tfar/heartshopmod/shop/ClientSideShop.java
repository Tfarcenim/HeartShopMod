package tfar.heartshopmod.shop;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

public class ClientSideShop implements Shop {
    private final Player source;
    private ShopOffers offers = new ShopOffers();
    private int xp;

    public ClientSideShop(Player $$0) {
        this.source = $$0;
    }

    public Player getTradingPlayer() {
        return this.source;
    }

    public void setTradingPlayer(@Nullable Player $$0) {
    }

    public ShopOffers getOffers() {
        return this.offers;
    }

    public void overrideOffers(ShopOffers $$0) {
        this.offers = $$0;
    }

    public void notifyTrade(ShopOffer $$0) {
        $$0.increaseUses();
    }

    public void notifyTradeUpdated(ItemStack $$0) {
    }

    public boolean isClientSide() {
        return this.source.level().isClientSide;
    }

    public int getVillagerXp() {
        return this.xp;
    }

    public void overrideXp(int $$0) {
        this.xp = $$0;
    }

    public boolean showProgressBar() {
        return true;
    }

    public SoundEvent getNotifyTradeSound() {
        return SoundEvents.VILLAGER_YES;
    }
}
