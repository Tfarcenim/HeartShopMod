package tfar.heartshopmod.net.util;

import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import tfar.dankstorage.utils.PacketBufferEX;
import tfar.dankstorage.world.ClientData;

public class S2CContentsForDisplayPacket implements S2CPacketHelper {
    private int size;
    private NonNullList<ItemStack> stacks;

    public S2CContentsForDisplayPacket(NonNullList<ItemStack> stacks) {
        this.stacks = stacks;
    }

    public S2CContentsForDisplayPacket(FriendlyByteBuf buf) {
        stacks = PacketBufferEX.readList(buf);
    }

    @Override
    public void handleClient() {
            ClientData.setList(stacks);
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        PacketBufferEX.writeList(buf, stacks);
    }
}