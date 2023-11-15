package tfar.heartshopmod;

import net.minecraft.client.gui.screens.MenuScreens;

public class HeartShopModClient {
    public static void setup() {
        MenuScreens.register(Init.HEART_SHOP,CustomShopScreen::new);
    }
}
