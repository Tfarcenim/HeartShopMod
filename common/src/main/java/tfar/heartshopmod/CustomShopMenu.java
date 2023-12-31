package tfar.heartshopmod;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import tfar.heartshopmod.shop.Shop;
import tfar.heartshopmod.shop.ClientSideShop;
import tfar.heartshopmod.shop.ShopOffer;
import tfar.heartshopmod.shop.ShopOffers;

public class CustomShopMenu extends AbstractContainerMenu {
    protected static final int PAYMENT1_SLOT = 0;
    protected static final int PAYMENT2_SLOT = 1;
    protected static final int RESULT_SLOT = 2;
    private static final int INV_SLOT_START = 3;
    private static final int INV_SLOT_END = 30;
    private static final int USE_ROW_SLOT_START = 30;
    private static final int USE_ROW_SLOT_END = 39;
    private static final int SELLSLOT1_X = 136;
    private static final int SELLSLOT2_X = 162;
    private static final int BUYSLOT_X = 220;
    private static final int ROW_Y = 37;
    private final Shop trader;
    private final ShopContainer tradeContainer;

    public CustomShopMenu(int pContainerId, Inventory pPlayerInventory) {
        this(pContainerId, pPlayerInventory, new ClientSideShop(pPlayerInventory.player));
    }

    public CustomShopMenu(int pContainerId, Inventory pPlayerInventory, Shop pTrader) {
        super(Init.HEART_SHOP, pContainerId);
        this.trader = pTrader;
        this.tradeContainer = new ShopContainer(pTrader);
        this.addSlot(new CustomShopSlot(pPlayerInventory.player, pTrader, this.tradeContainer, 0, 220, 37));

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(pPlayerInventory, j + i * 9 + 9, 108 + j * 18, 84 + i * 18));
            }
        }

        for (int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(pPlayerInventory, k, 108 + k * 18, 142));
        }

    }

    /**
     * Callback for when the crafting matrix is changed.
     */
    public void slotsChanged(Container pInventory) {
        this.tradeContainer.updateSellItem();
        super.slotsChanged(pInventory);
    }

    public void setSelectionHint(int pCurrentRecipeIndex) {
        this.tradeContainer.setSelectionHint(pCurrentRecipeIndex);
    }

    /**
     * Determines whether supplied player can use this container
     */
    public boolean stillValid(Player pPlayer) {
        return this.trader.getTradingPlayer() == pPlayer;
    }


    /**
     * Called to determine if the current slot is valid for the stack merging (double-click) code. The stack passed in is
     * null for the initial slot that was double-clicked.
     */
    public boolean canTakeItemForPickAll(ItemStack pStack, Slot pSlot) {
        return false;
    }

    /**
     * Handle when the stack in slot {@code index} is shift-clicked. Normally this moves the stack between the player
     * inventory and the other inventory(s).
     */
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(pIndex);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (pIndex == 0) {
                ShopOffer shopOffer = getTradeContainer().getActiveOffer();

                if (shopOffer != null) {
                    ItemStack purchase = itemstack1.copy();
                    PlayerDuck playerDuck = (PlayerDuck) pPlayer;
                    int maxAffordable = playerDuck.getHeartCurrency() / shopOffer.getCost();

                    int maxCount = Math.min(maxAffordable,purchase.getMaxStackSize());

                    if (maxCount <=0) return ItemStack.EMPTY;
                    purchase.setCount(maxCount);

                    ItemStack purchaseCopy = purchase.copy();

                    //returns if nothing could be transferred
                    if (!this.moveItemStackTo(purchaseCopy, 1, 37, true)) {
                        return ItemStack.EMPTY;
                    }
                    //some purchase is left over

                    if (!pPlayer.level().isClientSide) {
                        int purchased = purchase.getCount() - purchaseCopy.getCount();
                        int heartCost = purchased * shopOffer.getCost();
                        playerDuck.addHeartCurrency(-heartCost);
                    }


                    //slot.onQuickCraft(itemstack1, itemstack);
                    this.playTradeSound();
                }
                return ItemStack.EMPTY;
            } else {
                if (pIndex >= 1 && pIndex < 28) {
                    if (!this.moveItemStackTo(itemstack1, 28, 39-2, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (pIndex >= 30-2 && pIndex < 39-2 && !this.moveItemStackTo(itemstack1, 3-2, 30-2, false)) {
                    return ItemStack.EMPTY;
                }
            }
            if (itemstack1.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(pPlayer, itemstack1);
        }

        return itemstack;
    }

    private void playTradeSound() {
        if (!this.trader.isClientSide()) {
            Entity entity = trader.getTradingPlayer();
            entity.level().playLocalSound(entity.getX(), entity.getY(), entity.getZ(), this.trader.getNotifyTradeSound(), SoundSource.NEUTRAL, 1.0F, 1.0F, false);
        }

    }

    /**
     * Called when the container is closed.
     */
    public void removed(Player pPlayer) {
        super.removed(pPlayer);
        this.trader.setTradingPlayer(null);
    }

    private void moveFromInventoryToPaymentSlot(int pPaymentSlotIndex, ItemStack pPaymentSlot) {
        if (!pPaymentSlot.isEmpty()) {
            for (int i = 3; i < 39; ++i) {
                ItemStack itemstack = this.slots.get(i).getItem();
                if (!itemstack.isEmpty() && ItemStack.isSameItemSameTags(pPaymentSlot, itemstack)) {
                    ItemStack itemstack1 = this.tradeContainer.getItem(pPaymentSlotIndex);
                    int j = itemstack1.isEmpty() ? 0 : itemstack1.getCount();
                    int k = Math.min(pPaymentSlot.getMaxStackSize() - j, itemstack.getCount());
                    ItemStack itemstack2 = itemstack.copy();
                    int l = j + k;
                    itemstack.shrink(k);
                    itemstack2.setCount(l);
                    this.tradeContainer.setItem(pPaymentSlotIndex, itemstack2);
                    if (l >= pPaymentSlot.getMaxStackSize()) {
                        break;
                    }
                }
            }
        }

    }

    /**
     * {@link net.minecraft.client.multiplayer.ClientPacketListener} uses this to set offers for the client side
     * MerchantContainer.
     */
    public void setOffers(ShopOffers pOffers) {
        this.trader.overrideOffers(pOffers);
    }

    public ShopOffers getOffers() {
        return this.trader.getOffers();
    }

    public ShopContainer getTradeContainer() {
        return tradeContainer;
    }

    public int getBalance() {
        Player player = trader.getTradingPlayer();
        if (player != null) {
            return ((PlayerDuck)player).getHeartCurrency();
        }
        return 0;
    }
}
