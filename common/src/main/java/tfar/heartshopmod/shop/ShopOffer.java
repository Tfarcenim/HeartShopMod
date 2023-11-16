package tfar.heartshopmod.shop;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class ShopOffer {
    /** The heart cost for this offer. */
    private final int heartCost;

    /** The output of this offer. */


    private final ItemStack result;
    private int uses;
    private final int maxUses;
    private int specialPriceDiff;
    private int demand;
    private float priceMultiplier;
    private int xp = 1;

    public ShopOffer(CompoundTag pCompoundTag) {
        this.heartCost = pCompoundTag.getInt("buy");
        this.result = ItemStack.of(pCompoundTag.getCompound("sell"));
        this.uses = pCompoundTag.getInt("uses");
        if (pCompoundTag.contains("maxUses", 99)) {
            this.maxUses = pCompoundTag.getInt("maxUses");
        } else {
            this.maxUses = 4;
        }

        if (pCompoundTag.contains("xp", 3)) {
            this.xp = pCompoundTag.getInt("xp");
        }

        if (pCompoundTag.contains("priceMultiplier", 5)) {
            this.priceMultiplier = pCompoundTag.getFloat("priceMultiplier");
        }

        this.specialPriceDiff = pCompoundTag.getInt("specialPrice");
        this.demand = pCompoundTag.getInt("demand");
    }

    public ShopOffer(int heartCost, ItemStack pResult, int pUses, int pMaxUses, int pXp, float pPriceMultiplier, int pDemand) {
        this.heartCost = heartCost;
        this.result = pResult;
        this.uses = pUses;
        this.maxUses = pMaxUses;
        this.xp = pXp;
        this.priceMultiplier = pPriceMultiplier;
        this.demand = pDemand;
    }

    public int getCost() {
        return this.heartCost;
    }


    public ItemStack getResult() {
        return this.result;
    }

    /**
     * Calculates the demand with following formula: demand = demand + uses - maxUses - uses
     */
    public void updateDemand() {
        this.demand = this.demand + this.uses - (this.maxUses - this.uses);
    }

    public ItemStack assemble() {
        return this.result.copy();
    }

    public int getUses() {
        return this.uses;
    }

    public void resetUses() {
        this.uses = 0;
    }

    public int getMaxUses() {
        return this.maxUses;
    }

    public void increaseUses() {
        ++this.uses;
    }

    public int getDemand() {
        return this.demand;
    }

    public void addToSpecialPriceDiff(int pAdd) {
        this.specialPriceDiff += pAdd;
    }

    public void resetSpecialPriceDiff() {
        this.specialPriceDiff = 0;
    }

    public int getSpecialPriceDiff() {
        return this.specialPriceDiff;
    }

    public void setSpecialPriceDiff(int pPrice) {
        this.specialPriceDiff = pPrice;
    }

    public float getPriceMultiplier() {
        return this.priceMultiplier;
    }

    public int getXp() {
        return this.xp;
    }

    public boolean isOutOfStock() {
        return this.uses >= this.maxUses;
    }

    public void setToOutOfStock() {
        this.uses = this.maxUses;
    }


    public CompoundTag createTag() {
        CompoundTag compoundtag = new CompoundTag();
        compoundtag.putInt("buy", heartCost);
        compoundtag.put("sell", this.result.save(new CompoundTag()));
        compoundtag.putInt("uses", this.uses);
        compoundtag.putInt("maxUses", this.maxUses);
        compoundtag.putInt("xp", this.xp);
        compoundtag.putFloat("priceMultiplier", this.priceMultiplier);
        compoundtag.putInt("specialPrice", this.specialPriceDiff);
        compoundtag.putInt("demand", this.demand);
        return compoundtag;
    }

    public boolean satisfiedBy(Player player) {
        return true;
    }

    private boolean isRequiredItem(ItemStack pOffer, ItemStack pCost) {
        if (pCost.isEmpty() && pOffer.isEmpty()) {
            return true;
        } else {
            ItemStack itemstack = pOffer.copy();
            if (itemstack.isDamageableItem()) {
                itemstack.setDamageValue(itemstack.getDamageValue());
            }

            return ItemStack.isSameItem(itemstack, pCost) && (!pCost.hasTag() || itemstack.hasTag() && NbtUtils.compareNbt(pCost.getTag(), itemstack.getTag(), false));
        }
    }

    public boolean take(Player player) {
        if (!this.satisfiedBy(player)) {
            return false;
        } else {
            return true;
        }
    }
}
