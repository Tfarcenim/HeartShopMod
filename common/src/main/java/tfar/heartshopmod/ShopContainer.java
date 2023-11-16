package tfar.heartshopmod;

import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import tfar.heartshopmod.shop.Shop;
import tfar.heartshopmod.shop.ShopOffer;

import javax.annotation.Nullable;
import java.util.Iterator;

public class ShopContainer implements Container {
    private final Shop shop;
    private final NonNullList<ItemStack> itemStacks;
    @Nullable
    private ShopOffer activeOffer;
    private int trade;

    public ShopContainer(Shop $$0) {
        this.itemStacks = NonNullList.withSize(3, ItemStack.EMPTY);
        this.shop = $$0;
    }

    public int getContainerSize() {
        return this.itemStacks.size();
    }

    public boolean isEmpty() {
        Iterator<ItemStack> var1 = this.itemStacks.iterator();

        ItemStack $$0;
        do {
            if (!var1.hasNext()) return true;

            $$0 = var1.next();
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
            return $$3;
        }
    }

    public ItemStack removeItemNoUpdate(int $$0) {
        return ContainerHelper.takeItem(this.itemStacks, $$0);
    }

    public void setItem(int $$0, ItemStack $$1) {
        this.itemStacks.set($$0, $$1);
        if (!$$1.isEmpty() && $$1.getCount() > this.getMaxStackSize()) {
            $$1.setCount(this.getMaxStackSize());
        }
    }

    public boolean stillValid(Player $$0) {
        return this.shop.getTradingPlayer() == $$0;
    }

    public void setChanged() {
        this.updateSellItem();
    }

    public void updateSellItem() {
        this.activeOffer = null;
        ItemStack itemStack = itemStacks.get(trade);
        setItem(0,itemStack);
    }

    @Nullable
    public ShopOffer getActiveOffer() {
        return this.activeOffer;
    }

    public void setSelectionHint(int trade) {
        this.trade = trade;
        this.updateSellItem();
    }

    public void clearContent() {
        this.itemStacks.clear();
    }

}
