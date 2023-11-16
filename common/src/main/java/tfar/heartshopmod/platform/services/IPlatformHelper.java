package tfar.heartshopmod.platform.services;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.trading.MerchantOffers;
import tfar.heartshopmod.shop.ShopOffers;

public interface IPlatformHelper {

    /**
     * Gets the name of the current platform
     *
     * @return The name of the current platform.
     */
    String getPlatformName();

    /**
     * Checks if a mod with the given id is loaded.
     *
     * @param modId The mod to check if it is loaded.
     * @return True if the mod is loaded, false otherwise.
     */
    boolean isModLoaded(String modId);

    /**
     * Check if the game is currently in a development environment.
     *
     * @return True if in a development environment, false otherwise.
     */
    boolean isDevelopmentEnvironment();

    /**
     * Gets the name of the environment type as a string.
     *
     * @return The name of the environment type.
     */
    default String getEnvironmentName() {

        return isDevelopmentEnvironment() ? "development" : "production";
    }


    void sendShopOffers(ServerPlayer player, int pContainerId, ShopOffers pOffers, int pVillagerLevel, int pVillagerXp, boolean pShowProgress, boolean pCanRestock);

}