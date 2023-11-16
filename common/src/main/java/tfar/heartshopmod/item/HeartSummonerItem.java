package tfar.heartshopmod.item;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;

public class HeartSummonerItem extends Item {
    private final int level;

    public HeartSummonerItem(Properties $$0, int level) {
        super($$0);
        this.level = level;
    }

    int delay = 20;

    /**
     * Called each tick as long the item is in a player's inventory. Used by maps to check if it's in a player's hand and
     * update its contents.
     */@Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
         if (pIsSelected && !pLevel.isClientSide) {
             if (pLevel.getGameTime() % delay == 0) {
                 List<EntityType<?>> entityTypes = BuiltInRegistries.ENTITY_TYPE.stream()
                         .filter(entityType -> entityType.getCategory() == MobCategory.CREATURE).toList();
                 EntityType<?> type = entityTypes.get(pLevel.getRandom().nextInt(entityTypes.size()));
                 Entity entity = type.spawn((ServerLevel) pLevel,pEntity.blockPosition(), MobSpawnType.SPAWNER);
             }
         }
    }
}
