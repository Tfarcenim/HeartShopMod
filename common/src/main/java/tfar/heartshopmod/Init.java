package tfar.heartshopmod;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import tfar.heartshopmod.entity.HeartFireball;
import tfar.heartshopmod.entity.HeartGrenadeEntity;
import tfar.heartshopmod.item.*;

public class Init {

    public static final Item DIAMOND_HEART = new DiamondHeartItem(new Item.Properties());
    public static final Item HEART_GRENADE = new HeartGrenadeItem(new Item.Properties());
    public static final Item HEART_SUMMONER_1 = new HeartSummonerItem(new Item.Properties(),1);
    public static final Item HEART_SUMMONER_2 = new HeartSummonerItem(new Item.Properties(),2);
    public static final Item HEART_BAZOOKA = new HeartBazookaItem(new Item.Properties());
    public static final Item HEART_SWORD = new SwordItem(Tiers.NETHERITE, 3, -2.4F, new Item.Properties().fireResistant());
    public static final Item END_TELEPORTER = new EndTeleporterItem(new Item.Properties());

    public static final EntityType<HeartGrenadeEntity> HEART_GRENADE_E = EntityType.Builder.<HeartGrenadeEntity>of(HeartGrenadeEntity::new, MobCategory.MISC)
            .sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10).build("heart_grenade");

    public static final EntityType<HeartFireball> HEART_FIREBALL = EntityType.Builder.<HeartFireball>of(HeartFireball::new,
            MobCategory.MISC).sized(1.0F, 1.0F).clientTrackingRange(4).updateInterval(10).build("heart_fireball");


    public static final MenuType<CustomShopMenu> HEART_SHOP = new MenuType<>(CustomShopMenu::new, FeatureFlags.VANILLA_SET);


}
