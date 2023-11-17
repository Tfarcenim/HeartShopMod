package tfar.heartshopmod.data;

import com.google.gson.JsonElement;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.resources.ResourceLocation;
import tfar.heartshopmod.HeartShopMod;
import tfar.heartshopmod.Init;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class ModItemModelProvider extends ItemModelGenerators {

    public ModItemModelProvider(BiConsumer<ResourceLocation, Supplier<JsonElement>> pOutput) {
        super(pOutput);
    }

    @Override
    public void run() {
        generateFlatItem(Init.DIAMOND_HEART, ModelTemplates.FLAT_ITEM);
        generateFlatItem(Init.END_TELEPORTER, ModelTemplates.FLAT_ITEM);
        generateFlatItem(Init.HEART_SUMMONER_1, ModelTemplates.FLAT_ITEM);
        generateFlatItem(Init.HEART_SUMMONER_2, ModelTemplates.FLAT_ITEM);
    }
}
