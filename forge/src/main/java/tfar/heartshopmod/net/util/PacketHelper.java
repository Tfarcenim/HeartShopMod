package tfar.heartshopmod.net.util;

import net.minecraft.network.FriendlyByteBuf;

public interface PacketHelper {
    void encode(FriendlyByteBuf buf);
}
