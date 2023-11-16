package tfar.heartshopmod.net;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MerchantMenu;
import tfar.heartshopmod.CustomShopMenu;
import tfar.heartshopmod.net.util.C2SPacketHelper;

public class C2SSelectTradePacket implements C2SPacketHelper {

    private final int selectedTrade;
    public C2SSelectTradePacket(FriendlyByteBuf pBuffer) {
        this.selectedTrade = pBuffer.readInt();
    }

    public C2SSelectTradePacket(int selectedTrade) {
        this.selectedTrade = selectedTrade;
    }

    @Override
    public void handleServer(ServerPlayer player) {
        AbstractContainerMenu abstractcontainermenu = player.containerMenu;
        if (abstractcontainermenu instanceof CustomShopMenu customShopMenu) {
            if (!customShopMenu.stillValid(player)) {
                return;
            }
            customShopMenu.setSelectionHint(selectedTrade);
        }
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(selectedTrade);
    }
}
