package tfar.heartshopmod.shop;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;

public class ShopOffers extends ArrayList<ShopOffer> {
    public ShopOffers() {
    }

    private ShopOffers(int p_220323_) {
        super(p_220323_);
    }

    public ShopOffers(CompoundTag pCompoundTag) {
        ListTag listtag = pCompoundTag.getList("Recipes", 10);

        for(int i = 0; i < listtag.size(); ++i) {
            this.add(new ShopOffer(listtag.getCompound(i)));
        }

    }

    public void writeToStream(FriendlyByteBuf pBuffer) {
        pBuffer.writeCollection(this, (buf, shopOffer) -> {
            buf.writeInt(shopOffer.getCost());
            buf.writeItem(shopOffer.getResult());
            buf.writeInt(shopOffer.getXp());
            buf.writeInt(shopOffer.getSpecialPriceDiff());
            buf.writeFloat(shopOffer.getPriceMultiplier());
            buf.writeInt(shopOffer.getDemand());
        });
    }

    public static ShopOffers createFromStream(FriendlyByteBuf pBuffer) {
        return pBuffer.readCollection(ShopOffers::new, (buf) -> {
            int costA = buf.readInt();
            ItemStack result = buf.readItem();
            int k = buf.readInt();
            int l = buf.readInt();
            float f = buf.readFloat();
            int i1 = buf.readInt();
            ShopOffer merchantoffer = new ShopOffer(costA, result, k, f, i1);

            merchantoffer.setSpecialPriceDiff(l);
            return merchantoffer;
        });
    }

    public CompoundTag createTag() {
        CompoundTag compoundtag = new CompoundTag();
        ListTag listtag = new ListTag();

        for(int i = 0; i < this.size(); ++i) {
            ShopOffer merchantoffer = this.get(i);
            listtag.add(merchantoffer.createTag());
        }

        compoundtag.put("Recipes", listtag);
        return compoundtag;
    }
}