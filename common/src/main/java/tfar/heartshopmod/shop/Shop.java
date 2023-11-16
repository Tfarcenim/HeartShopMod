package tfar.heartshopmod.shop;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MerchantMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffers;
import tfar.heartshopmod.CustomShopMenu;
import tfar.heartshopmod.platform.Services;
import tfar.heartshopmod.shop.ShopOffer;
import tfar.heartshopmod.shop.ShopOffers;

import java.util.OptionalInt;

public interface Shop {
    void setTradingPlayer(@javax.annotation.Nullable Player var1);

    @javax.annotation.Nullable
    Player getTradingPlayer();

    ShopOffers getOffers();

    void overrideOffers(ShopOffers var1);

    void notifyTrade(ShopOffer var1);

    void notifyTradeUpdated(ItemStack var1);



    boolean showProgressBar();

    SoundEvent getNotifyTradeSound();

    default boolean canRestock() {
        return false;
    }

    default void openTradingScreen(Player pPlayer, Component pDisplayName, int pLevel) {
        OptionalInt optionalint = pPlayer.openMenu(new SimpleMenuProvider((p_45298_, p_45299_, p_45300_) -> new CustomShopMenu(p_45298_, p_45299_, this), pDisplayName));
        if (optionalint.isPresent()) {
            ShopOffers shopOffers = this.getOffers();
            if (!shopOffers.isEmpty()) {
                Services.PLATFORM.sendShopOffers((ServerPlayer) pPlayer,optionalint.getAsInt(), shopOffers, pLevel,this.showProgressBar(), this.canRestock());
            }
        }

    }


    boolean isClientSide();

}
