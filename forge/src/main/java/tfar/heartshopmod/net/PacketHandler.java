package tfar.heartshopmod.net;

import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.IndexedMessageCodec;
import net.minecraftforge.network.simple.SimpleChannel;
import tfar.heartshopmod.HeartShopMod;
import tfar.heartshopmod.net.util.PacketHelper;
import tfar.heartshopmod.shop.ShopOffers;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class PacketHandler {

    public static SimpleChannel INSTANCE;

    public static void registerMessages() {

        INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(HeartShopMod.MOD_ID,HeartShopMod.MOD_ID), () -> "1.0", s -> true, s -> true);

        int i = 0;

        INSTANCE.registerMessage(i++,
                S2CShopOffersPacket.class,
                S2CShopOffersPacket::encode,
                S2CShopOffersPacket::new,
                S2CShopOffersPacket::handle);
    }

    public static void sendContentsForDisplay(ServerPlayer player, int pContainerId, ShopOffers pOffers, int pVillagerLevel, int pVillagerXp, boolean pShowProgress, boolean pCanRestock) {
        sendToClient(new S2CShopOffersPacket(pContainerId, pOffers, pVillagerLevel, pVillagerXp, pShowProgress, pCanRestock),player);
    }

    public static <MSG> void sendToClient(MSG packet, ServerPlayer player) {
        INSTANCE.sendTo(packet, player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }

    public static <MSG> void sendToServer(MSG packet) {
        INSTANCE.sendToServer(packet);
    }
}
