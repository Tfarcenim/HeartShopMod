package tfar.heartshopmod;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.RegisterEvent;
import tfar.heartshopmod.data.Datagen;
import tfar.heartshopmod.net.PacketHandler;

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
        bus.addListener(this::setup);
        bus.addListener(Datagen::gather);
        if (FMLEnvironment.dist.isClient()) {
            bus.addListener(Client::renderer);
            bus.addListener(this::client);
            bus.addListener(Client::registerBar);
        }

        MinecraftForge.EVENT_BUS.addListener(this::onDeath);
        MinecraftForge.EVENT_BUS.addListener(this::clonePlayer);
        MinecraftForge.EVENT_BUS.addListener(this::commands);
    }

    private void commands(RegisterCommandsEvent event) {
        ModCommands.register(event.getDispatcher());
    }

    private void clonePlayer(PlayerEvent.Clone event) {
        Player original = event.getOriginal();
        Player player = event.getEntity();
        ((PlayerDuck)player).setHeartCurrency(((PlayerDuck)original).getHeartCurrency());
    }

    private void setup(FMLCommonSetupEvent event) {
        PacketHandler.registerMessages();
    }

    private void client(FMLClientSetupEvent event) {
        HeartShopModClient.setup();
        MinecraftForge.EVENT_BUS.addListener(Client::disableVanillaBar);
    }

    private void onDeath(LivingDeathEvent event) {
        Entity attacker = event.getSource().getEntity();
        if (attacker instanceof Player player) {
            PlayerDuck playerDuck = (PlayerDuck)player;
            LivingEntity target = event.getEntity();
            int heartsToAward = (int) (target.getMaxHealth() / 2);

            if (player.getMainHandItem().is(Init.HEART_SWORD)) heartsToAward *=2;

            playerDuck.addHeartCurrency(heartsToAward);
        }
    }

    private void register(RegisterEvent event) {
        event.register(Registries.ITEM,new ResourceLocation(HeartShopMod.MOD_ID,"diamond_heart"),() -> Init.DIAMOND_HEART);
        event.register(Registries.ITEM,new ResourceLocation(HeartShopMod.MOD_ID,"heart_grenade"),() -> Init.HEART_GRENADE);
        event.register(Registries.ITEM,new ResourceLocation(HeartShopMod.MOD_ID,"heart_summoner_1"),() -> Init.HEART_SUMMONER_1);
        event.register(Registries.ITEM,new ResourceLocation(HeartShopMod.MOD_ID,"heart_summoner_2"),() -> Init.HEART_SUMMONER_2);
        event.register(Registries.ITEM,new ResourceLocation(HeartShopMod.MOD_ID,"heart_sword"),() -> Init.HEART_SWORD);
        event.register(Registries.ITEM,new ResourceLocation(HeartShopMod.MOD_ID,"heart_bazooka"),() -> Init.HEART_BAZOOKA);
        event.register(Registries.ITEM,new ResourceLocation(HeartShopMod.MOD_ID,"end_teleporter"),() -> Init.END_TELEPORTER);

        event.register(Registries.ENTITY_TYPE,new ResourceLocation(HeartShopMod.MOD_ID,"heart_grenade"),() -> Init.HEART_GRENADE_E);
        event.register(Registries.ENTITY_TYPE,new ResourceLocation(HeartShopMod.MOD_ID,"heart_fireball"),() -> Init.HEART_FIREBALL);

        event.register(Registries.MENU,new ResourceLocation(HeartShopMod.MOD_ID,"heart_shop"),() -> Init.HEART_SHOP);

    }
}