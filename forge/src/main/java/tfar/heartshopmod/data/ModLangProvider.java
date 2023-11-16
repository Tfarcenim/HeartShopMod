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
        add("container.heart_shop","Heart Shop");
    }
}
