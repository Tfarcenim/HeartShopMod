package tfar.heartshopmod.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tfar.heartshopmod.PlayerDuck;

@Mixin(Player.class)
abstract class PlayerMixin extends LivingEntity implements PlayerDuck {

    @SuppressWarnings("all") //shush
    private static final EntityDataAccessor<Integer> HEART_CURRENCY = SynchedEntityData.defineId(Player.class, EntityDataSerializers.INT);

    protected PlayerMixin(EntityType<? extends LivingEntity> $$0, Level $$1) {
        super($$0, $$1);
    }

    @Inject(method = "addAdditionalSaveData",at = @At("RETURN"))
    private void addExtra(CompoundTag tag, CallbackInfo ci) {
        tag.putInt("heart_currency", getHeartCurrency());
    }

    @Inject(method = "readAdditionalSaveData",at = @At("RETURN"))
    private void readExtra(CompoundTag tag, CallbackInfo ci) {
        setHeartCurrency(tag.getInt("heart_currency"));
    }


    @Inject(method = "defineSynchedData",at = @At("RETURN"))
    private void registerCustom(CallbackInfo ci) {
        this.entityData.define(HEART_CURRENCY, 0);
    }

    @Override
    public int getHeartCurrency() {
        return entityData.get(HEART_CURRENCY);
    }

    @Override
    public void setHeartCurrency(int hearts) {
        entityData.set(HEART_CURRENCY,hearts);
    }
}