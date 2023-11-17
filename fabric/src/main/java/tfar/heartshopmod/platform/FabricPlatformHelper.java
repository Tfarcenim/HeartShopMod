package tfar.heartshopmod.platform;

import net.minecraft.server.level.ServerPlayer;
import tfar.heartshopmod.platform.services.IPlatformHelper;
import net.fabricmc.loader.api.FabricLoader;
import tfar.heartshopmod.shop.ShopOffers;

public class FabricPlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {
        return "Fabric";
    }

    @Override
    public boolean isModLoaded(String modId) {

        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {

        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    @Override
    public void sendSelectedTrade(int trade) {

    }

    @Override
    public void sendShopOffers(ServerPlayer player, int pContainerId, ShopOffers pOffers, int pVillagerLevel, boolean pShowProgress, boolean pCanRestock) {

    }
}
