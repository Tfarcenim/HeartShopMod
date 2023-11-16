package tfar.heartshopmod.item;

import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.stats.Stats;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MerchantMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.Merchant;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import tfar.heartshopmod.CustomShopMenu;
import tfar.heartshopmod.Init;

import java.util.OptionalInt;

public class DiamondHeartItem extends Item {
    public DiamondHeartItem(Properties $$0) {
        super($$0);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {

        if (level.isClientSide) {
            return InteractionResultHolder.success(player.getItemInHand(hand));
        } else {
            OptionalInt optionalInt = player.openMenu(new SimpleMenuProvider((p_45298_, p_45299_, p_45300_) -> {

                HeartMerchant heartMerchant = new HeartMerchant();
                heartMerchant.setTradingPlayer(p_45300_);
                return new CustomShopMenu(p_45298_, p_45299_, heartMerchant);
            }, CONTAINER_TITLE));

            return InteractionResultHolder.consume(player.getItemInHand(hand));
        }
    }

    public interface Shop {
        void setTradingPlayer(@javax.annotation.Nullable Player var1);

        @javax.annotation.Nullable
        Player getTradingPlayer();

        MerchantOffers getOffers();

        void overrideOffers(MerchantOffers var1);

        void notifyTrade(MerchantOffer var1);

        void notifyTradeUpdated(ItemStack var1);

        int getVillagerXp();

        void overrideXp(int var1);

        boolean showProgressBar();

        SoundEvent getNotifyTradeSound();

        default boolean canRestock() {
            return false;
        }


        boolean isClientSide();

    }

    public static class HeartMerchant implements Shop {

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
        public MerchantOffers getOffers() {
            return new MerchantOffers();
        }

        @Override
        public void overrideOffers(MerchantOffers merchantOffers) {

        }

        @Override
        public void notifyTrade(MerchantOffer merchantOffer) {

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
            return null;
        }

        @Override
        public boolean isClientSide() {
            return false;
        }
    }



    private static final Component CONTAINER_TITLE = Component.translatable("container.heartshop");

    public static Container createInventory() {
        SimpleContainer simpleContainer = new SimpleContainer(27) {
            @Override
            public boolean canTakeItem(Container $$0, int $$1, ItemStack $$2) {
                return false;
            }
        };

        simpleContainer.addItem(Init.HEART_GRENADE.getDefaultInstance());
        return simpleContainer;
    }
}
