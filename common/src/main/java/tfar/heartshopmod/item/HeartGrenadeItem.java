package tfar.heartshopmod.item;

import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;
import tfar.heartshopmod.entity.HeartGrenadeEntity;

import java.util.List;

public class HeartGrenadeItem extends Item {
    public HeartGrenadeItem(Properties $$0) {
        super($$0);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        if (!pLevel.isClientSide) {
            HeartGrenadeEntity heartGrenade = new HeartGrenadeEntity(pLevel, pPlayer);

            ItemStack dummyPotion = getDefaultInstance();
            PotionUtils.setCustomEffects(dummyPotion, List.of(new MobEffectInstance(MobEffects.HARM)));

            heartGrenade.setItem(dummyPotion);
            heartGrenade.shootFromRotation(pPlayer, pPlayer.getXRot(), pPlayer.getYRot(), -20.0F, 0.5F, 1.0F);
            pLevel.addFreshEntity(heartGrenade);
        }

        pPlayer.awardStat(Stats.ITEM_USED.get(this));
        if (!pPlayer.getAbilities().instabuild) {
            itemstack.shrink(1);
        }

        return InteractionResultHolder.sidedSuccess(itemstack, pLevel.isClientSide());
    }

}
