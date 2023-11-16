package tfar.heartshopmod.item;

import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import tfar.heartshopmod.Init;
import tfar.heartshopmod.shop.Shop;
import tfar.heartshopmod.shop.ShopOffer;
import tfar.heartshopmod.shop.ShopOffers;

public class DiamondHeartItem extends Item {
    public DiamondHeartItem(Properties $$0) {
        super($$0);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {

        if (level.isClientSide) {
            return InteractionResultHolder.success(player.getItemInHand(hand));
        } else {
            HeartShop heartShop = new HeartShop();
            heartShop.setTradingPlayer(player);
            heartShop.openTradingScreen(player,CONTAINER_TITLE,0);
            return InteractionResultHolder.consume(player.getItemInHand(hand));
        }
    }

    public static class HeartShop implements Shop {

        private Player player;

        @Override
        public void setTradingPlayer(@Nullable Player player) {
            this.player = player;
        }

        @Nullable
        @Override
        public Player getTradingPlayer() {
            return player;
        }

        @Override
        public ShopOffers getOffers() {
            return createOffers();
        }

        @Override
        public void overrideOffers(ShopOffers merchantOffers) {

        }

        @Override
        public void notifyTrade(ShopOffer merchantOffer) {

        }

        @Override
        public void notifyTradeUpdated(ItemStack itemStack) {

        }

        @Override
        public int getVillagerXp() {
            return 0;
        }

        @Override
        public void overrideXp(int i) {

        }

        @Override
        public boolean showProgressBar() {
            return false;
        }

        @Override
        public SoundEvent getNotifyTradeSound() {
            return SoundEvents.VILLAGER_TRADE;
        }

        @Override
        public boolean isClientSide() {
            return false;
        }
    }

    public static ShopOffers createOffers() {
        ShopOffers shopOffers = new ShopOffers();
        ShopOffer shopOffer = new ShopOffer(1, Init.HEART_GRENADE.getDefaultInstance(), 0,0,0);
        shopOffers.add(shopOffer);
        return shopOffers;
    }

    private static final Component CONTAINER_TITLE = Component.translatable("container.heartshop");
}
