package tfar.heartshopmod.platform;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.trading.MerchantOffers;
import tfar.heartshopmod.net.C2SSelectTradePacket;
import tfar.heartshopmod.net.PacketHandler;
import tfar.heartshopmod.platform.services.IPlatformHelper;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;
import tfar.heartshopmod.shop.ShopOffers;

public class ForgePlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {

        return "Forge";
    }

    @Override
    public boolean isModLoaded(String modId) {

        return ModList.get().isLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {

        return !FMLLoader.isProduction();
    }

    @Override
    public void sendSelectedTrade(int trade) {
        PacketHandler.sendToServer(new C2SSelectTradePacket(trade));
    }

    @Override
    public void sendShopOffers(ServerPlayer player, int pContainerId, ShopOffers pOffers, int pVillagerLevel,boolean pShowProgress, boolean pCanRestock) {
        PacketHandler.sendContentsForDisplay(player, pContainerId, pOffers, pVillagerLevel,pShowProgress, pCanRestock);
    }
}