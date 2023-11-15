package tfar.heartshopmod;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.RegisterEvent;
import tfar.heartshopmod.data.Datagen;

@Mod(HeartShopMod.MOD_ID)
public class HeartShopModForge {
    
    public HeartShopModForge() {
    
        // This method is invoked by the Forge mod loader when it is ready
        // to load your mod. You can access Forge and Common code in this
        // project.
    
        // Use Forge to bootstrap the Common mod.
        HeartShopMod.init();
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::register);
        bus.addListener(Datagen::gather);
        if (FMLEnvironment.dist.isClient()) {
            bus.addListener(Client::renderer);
        }
    }

    private void register(RegisterEvent event) {
        event.register(Registries.ITEM,new ResourceLocation(HeartShopMod.MOD_ID,"diamond_heart"),() -> Init.DIAMOND_HEART);
        event.register(Registries.ITEM,new ResourceLocation(HeartShopMod.MOD_ID,"heart_grenade"),() -> Init.HEART_GRENADE);

        event.register(Registries.ENTITY_TYPE,new ResourceLocation(HeartShopMod.MOD_ID,"heart_grenade"),() -> Init.HEART_GRENADE_E);

    }
}