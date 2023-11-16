package tfar.heartshopmod;

import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import tfar.heartshopmod.shop.Shop;
import tfar.heartshopmod.shop.ShopOffer;
import tfar.heartshopmod.shop.ShopOffers;

import javax.annotation.Nullable;
import java.util.Iterator;

public class ShopContainer implements Container {
    private final Shop merchant;
    private final NonNullList<ItemStack> itemStacks;
    @Nullable
    private ShopOffer activeOffer;
    private int selectionHint;
    private int futureXp;

    public ShopContainer(Shop $$0) {
        this.itemStacks = NonNullList.withSize(3, ItemStack.EMPTY);
        this.merchant = $$0;
    }

    public int getContainerSize() {
        return this.itemStacks.size();
    }

    public boolean isEmpty() {
        Iterator var1 = this.itemStacks.iterator();

        ItemStack $$0;
        do {
            if (!var1.hasNext()) {
                return true;
            }

            $$0 = (ItemStack)var1.next();
        } while($$0.isEmpty());

        return false;
    }

    public ItemStack getItem(int $$0) {
        return this.itemStacks.get($$0);
    }

    public ItemStack removeItem(int $$0, int $$1) {
        ItemStack $$2 = this.itemStacks.get($$0);
        if ($$0 == 2 && !$$2.isEmpty()) {
            return ContainerHelper.removeItem(this.itemStacks, $$0, $$2.getCount());
        } else {
            ItemStack $$3 = ContainerHelper.removeItem(this.itemStacks, $$0, $$1);
            if (!$$3.isEmpty() && this.isPaymentSlot($$0)) {
                this.updateSellItem();
            }

            return $$3;
        }
    }

    private boolean isPaymentSlot(int $$0) {
        return $$0 == 0 || $$0 == 1;
    }

    public ItemStack removeItemNoUpdate(int $$0) {
        return ContainerHelper.takeItem(this.itemStacks, $$0);
    }

    public void setItem(int $$0, ItemStack $$1) {
        this.itemStacks.set($$0, $$1);
        if (!$$1.isEmpty() && $$1.getCount() > this.getMaxStackSize()) {
            $$1.setCount(this.getMaxStackSize());
        }

        if (this.isPaymentSlot($$0)) {
            this.updateSellItem();
        }

    }

    public boolean stillValid(Player $$0) {
        return this.merchant.getTradingPlayer() == $$0;
    }

    public void setChanged() {
        this.updateSellItem();
    }

    public void updateSellItem() {
        this.activeOffer = null;
        ItemStack $$2;
        ItemStack $$3;
        if (this.itemStacks.get(0).isEmpty()) {
            $$2 = this.itemStacks.get(1);
            $$3 = ItemStack.EMPTY;
        } else {
            $$2 = this.itemStacks.get(0);
            $$3 = this.itemStacks.get(1);
        }

        if ($$2.isEmpty()) {
            this.setItem(2, ItemStack.EMPTY);
            this.futureXp = 0;
        } else {
            ShopOffers shopOffers = this.merchant.getOffers();
            if (!shopOffers.isEmpty()) {
                ShopOffer $$5 = null;//shopOffers.getRecipeFor($$2, $$3, this.selectionHint);
                if ($$5 == null) {
                    this.activeOffer = $$5;
                    $$5 = null;//shopOffers.getRecipeFor($$3, $$2, this.selectionHint);
                }

                if ($$5 != null) {
                    this.activeOffer = $$5;
                    this.setItem(2, $$5.assemble());
                    this.futureXp = $$5.getXp();
                } else {
                    this.setItem(2, ItemStack.EMPTY);
                    this.futureXp = 0;
                }
            }

            this.merchant.notifyTradeUpdated(this.getItem(2));
        }
    }

    @Nullable
    public ShopOffer getActiveOffer() {
        return this.activeOffer;
    }

    public void setSelectionHint(int $$0) {
        this.selectionHint = $$0;
        this.updateSellItem();
    }

    public void clearContent() {
        this.itemStacks.clear();
    }

    public int getFutureXp() {
        return this.futureXp;
    }
}
