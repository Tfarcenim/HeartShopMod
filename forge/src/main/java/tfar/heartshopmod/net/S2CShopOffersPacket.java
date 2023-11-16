package tfar.heartshopmod.net;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MerchantMenu;
import net.minecraft.world.item.trading.MerchantOffers;
import tfar.heartshopmod.CustomShopMenu;
import tfar.heartshopmod.net.util.S2CPacketHelper;
import tfar.heartshopmod.shop.ShopOffers;

public class S2CShopOffersPacket implements S2CPacketHelper {
    private final int containerId;
    private final ShopOffers offers;
    private final int villagerLevel;
    private final int villagerXp;
    private final boolean showProgress;
    private final boolean canRestock;

    public S2CShopOffersPacket(int pContainerId, ShopOffers pOffers, int pVillagerLevel, int pVillagerXp, boolean pShowProgress, boolean pCanRestock) {
        this.containerId = pContainerId;
        this.offers = pOffers;
        this.villagerLevel = pVillagerLevel;
        this.villagerXp = pVillagerXp;
        this.showProgress = pShowProgress;
        this.canRestock = pCanRestock;
    }

    public S2CShopOffersPacket(FriendlyByteBuf pBuffer) {
        this.containerId = pBuffer.readVarInt();
        this.offers = ShopOffers.createFromStream(pBuffer);
        this.villagerLevel = pBuffer.readVarInt();
        this.villagerXp = pBuffer.readVarInt();
        this.showProgress = pBuffer.readBoolean();
        this.canRestock = pBuffer.readBoolean();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void encode(FriendlyByteBuf pBuffer) {
        pBuffer.writeVarInt(this.containerId);
        this.offers.writeToStream(pBuffer);
        pBuffer.writeVarInt(this.villagerLevel);
        pBuffer.writeVarInt(this.villagerXp);
        pBuffer.writeBoolean(this.showProgress);
        pBuffer.writeBoolean(this.canRestock);
    }

    @Override
    public void handleClient() {
        Minecraft minecraft = Minecraft.getInstance();
        AbstractContainerMenu abstractcontainermenu = minecraft.player.containerMenu;
        if (containerId == abstractcontainermenu.containerId && abstractcontainermenu instanceof CustomShopMenu shopMenu) {
            shopMenu.setOffers(new ShopOffers(offers.createTag()));
        }
    }
}