package tfar.heartshopmod;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import tfar.heartshopmod.item.DiamondHeartItem;
import tfar.heartshopmod.item.HeartGrenadeItem;

public class Init {

    public static final Item DIAMOND_HEART = new DiamondHeartItem(new Item.Properties());
    public static final Item HEART_GRENADE = new HeartGrenadeItem(new Item.Properties());

    public static final EntityType<HeartGrenadeEntity> HEART_GRENADE_E = EntityType.Builder.<HeartGrenadeEntity>of(HeartGrenadeEntity::new, MobCategory.MISC)
            .sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10).build("heart_grenade");

    public static final MenuType<CustomShopMenu> HEART_SHOP = new MenuType<>(CustomShopMenu::new, FeatureFlags.VANILLA_SET);


}
