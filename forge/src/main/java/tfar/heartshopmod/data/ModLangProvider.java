package tfar.heartshopmod.data;

import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;
import tfar.heartshopmod.HeartShopMod;
import tfar.heartshopmod.Init;


public class ModLangProvider extends LanguageProvider {
    public ModLangProvider(PackOutput output) {
        super(output, HeartShopMod.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        add(Init.DIAMOND_HEART,"Diamond Heart");
        add(Init.HEART_GRENADE,"Heart Grenade");
        add(Init.HEART_SUMMONER_1,"Heart Summoner 1");
        add(Init.HEART_SUMMONER_2,"Heart Summoner 2");
        add(Init.HEART_BAZOOKA,"Heart Bazooka");
        add(Init.END_TELEPORTER,"End Teleporter");
        add(Init.HEART_SWORD,"Heart Sword");

        add("container.heart_shop","Heart Shop");
    }
}
