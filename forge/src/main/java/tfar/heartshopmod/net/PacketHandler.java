package tfar.heartshopmod.net;

import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import tfar.heartshopmod.HeartShopMod;

public class PacketHandler {

    public static SimpleChannel INSTANCE;

    public static void registerMessages() {

        INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(HeartShopMod.MOD_ID,HeartShopMod.MOD_ID), () -> "1.0", s -> true, s -> true);

        int i = 0;

        INSTANCE.registerMessage(i++,
                S2CContentsForDisplayPacket.class,
                S2CContentsForDisplayPacket::encode,
                S2CContentsForDisplayPacket::new,
                S2CContentsForDisplayPacket::handle);
    }

    public static void sendContentsForDisplay(ServerPlayer player, NonNullList<ItemStack> stacks) {
        sendToClient(new S2CContentsForDisplayPacket(stacks),player);
    }

    public static <MSG> void sendToClient(MSG packet, ServerPlayer player) {
        INSTANCE.sendTo(packet, player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }

    public static <MSG> void sendToServer(MSG packet) {
        INSTANCE.sendToServer(packet);
    }
}
