package tfar.heartshopmod;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import tfar.heartshopmod.shop.Shop;
import tfar.heartshopmod.shop.ShopOffer;

public class CustomShopSlot extends Slot {
    private final ShopContainer slots;
    private final Player player;
    private int removeCount;
    private final Shop merchant;

    public CustomShopSlot(Player $$0, Shop $$1, ShopContainer $$2, int $$3, int $$4, int $$5) {
        super($$2, $$3, $$4, $$5);
        this.player = $$0;
        this.merchant = $$1;
        this.slots = $$2;
    }

    public boolean mayPlace(ItemStack $$0) {
        return false;
    }

    public ItemStack remove(int $$0) {
        if (this.hasItem()) {
            this.removeCount += Math.min($$0, this.getItem().getCount());
        }

        return super.remove($$0);
    }

    protected void onQuickCraft(ItemStack $$0, int $$1) {
        this.removeCount += $$1;
        this.checkTakeAchievements($$0);
    }

    @Override
    public boolean mayPickup(Player player) {
        PlayerDuck playerDuck = (PlayerDuck) player;
        ShopOffer shopOffer = slots.getActiveOffer();
        if (shopOffer != null) {
            return shopOffer.getCost() <= playerDuck.getHeartCurrency();
        }
        return false;
    }

    protected void checkTakeAchievements(ItemStack $$0) {
        $$0.onCraftedBy(this.player.level(), this.player, this.removeCount);
        this.removeCount = 0;
    }

    public void onTake(Player player, ItemStack $$1) {
        ShopOffer shopOffer = slots.getActiveOffer();
        if (shopOffer != null && !player.level().isClientSide) {
            PlayerDuck playerDuck = (PlayerDuck) player;
            int heartCost = $$1.getCount() * shopOffer.getCost();
            playerDuck.addHeartCurrency(-heartCost);
        }
    }
}
