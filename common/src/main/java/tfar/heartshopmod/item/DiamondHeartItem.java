package tfar.heartshopmod.item;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.stats.Stats;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import tfar.heartshopmod.Init;

public class DiamondHeartItem extends Item {
    public DiamondHeartItem(Properties $$0) {
        super($$0);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {

        if (level.isClientSide) {
            return InteractionResultHolder.success(player.getItemInHand(hand));
        } else {
            player.openMenu(getMenuProvider(player.level(),player.blockPosition()));
            player.awardStat(Stats.INTERACT_WITH_CRAFTING_TABLE);
            return InteractionResultHolder.consume(player.getItemInHand(hand));
        }
    }



    private static final Component CONTAINER_TITLE = Component.translatable("container.heartshop");

    public MenuProvider getMenuProvider(Level pLevel, BlockPos pPos) {
        return new SimpleMenuProvider((i, p_52230_, player) -> new ChestMenu(MenuType.GENERIC_9x3,i, p_52230_,createInventory() ,3), CONTAINER_TITLE);
    }

    public Container createInventory() {
        SimpleContainer simpleContainer = new SimpleContainer(27);
        simpleContainer.addItem(Init.HEART_GRENADE.getDefaultInstance());
        return simpleContainer;
    }
}
