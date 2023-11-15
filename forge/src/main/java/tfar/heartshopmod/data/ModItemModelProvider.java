package tfar.heartshopmod.data;

import com.google.gson.JsonElement;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.resources.ResourceLocation;
import tfar.heartshopmod.HeartShopMod;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class ModItemModelProvider extends ItemModelGenerators {

    public ModItemModelProvider(BiConsumer<ResourceLocation, Supplier<JsonElement>> pOutput) {
        super(pOutput);
    }

    @Override
    public void run() {
        generateLayeredItem(new ResourceLocation(HeartShopMod.MOD_ID,"item/diamond_heart"),
                new ResourceLocation(HeartShopMod.MOD_ID,"item/heart_background"),new ResourceLocation(HeartShopMod.MOD_ID,"item/red_heart"));

        generateLayeredItem(new ResourceLocation(HeartShopMod.MOD_ID,"item/heart_grenade"),
                new ResourceLocation(HeartShopMod.MOD_ID,"item/heart_background"),new ResourceLocation(HeartShopMod.MOD_ID,"item/red_heart"));
    }
}
