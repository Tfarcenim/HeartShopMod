package tfar.heartshopmod;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MerchantContainer;
import net.minecraft.world.inventory.MerchantResultSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.Merchant;

public class CustomShopSlot extends MerchantResultSlot {
    public CustomShopSlot(Player $$0, Merchant $$1, MerchantContainer $$2, int $$3, int $$4, int $$5) {
        super($$0, $$1, $$2, $$3, $$4, $$5);
    }

    public void onTake(Player $$0, ItemStack $$1) {
        this.checkTakeAchievements($$1);
   //     MerchantOffer $$2 = this.slots.getActiveOffer();
     //   if ($$2 != null) {
    //        ItemStack $$3 = this.slots.getItem(0);
    //        ItemStack $$4 = this.slots.getItem(1);
    //        if ($$2.take($$3, $$4) || $$2.take($$4, $$3)) {
    //            this.merchant.notifyTrade($$2);
   //             $$0.awardStat(Stats.TRADED_WITH_VILLAGER);
   //             this.slots.setItem(0, $$3);
   //             this.slots.setItem(1, $$4);
            }

     //       this.merchant.overrideXp(this.merchant.getVillagerXp() + $$2.getXp());
}
