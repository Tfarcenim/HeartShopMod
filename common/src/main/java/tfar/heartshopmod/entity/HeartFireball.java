package tfar.heartshopmod.entity;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Fireball;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import tfar.heartshopmod.Init;

public class HeartFireball extends Fireball {

    private int explosionPower = 1;

    public HeartFireball(EntityType<? extends HeartFireball> $$0, Level $$1) {
        super($$0, $$1);
    }

    public HeartFireball(Level $$0, LivingEntity $$1, double $$2, double $$3, double $$4, int $$5) {
        super(Init.HEART_FIREBALL, $$1, $$2, $$3, $$4, $$0);
        this.explosionPower = $$5;
    }

    protected void onHit(HitResult $$0) {
        super.onHit($$0);
        if (!this.level().isClientSide) {
            boolean $$1 = this.level().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING);
            this.level().explode(this, this.getX(), this.getY(), this.getZ(), (float)this.explosionPower, $$1, Level.ExplosionInteraction.MOB);
            this.discard();
        }

    }

    protected void onHitEntity(EntityHitResult $$0) {
        super.onHitEntity($$0);
        if (!this.level().isClientSide) {
            Entity $$1 = $$0.getEntity();
            Entity $$2 = this.getOwner();
            $$1.hurt(this.damageSources().fireball(this, $$2), 6.0F);
            if ($$2 instanceof LivingEntity) {
                this.doEnchantDamageEffects((LivingEntity)$$2, $$1);
            }
        }
    }

    @Override
    protected ParticleOptions getTrailParticle() {
        return ParticleTypes.HEART;
    }

    public void addAdditionalSaveData(CompoundTag $$0) {
        super.addAdditionalSaveData($$0);
        $$0.putByte("ExplosionPower", (byte)this.explosionPower);
    }

    public void readAdditionalSaveData(CompoundTag $$0) {
        super.readAdditionalSaveData($$0);
        if ($$0.contains("ExplosionPower", 99)) {
            this.explosionPower = $$0.getByte("ExplosionPower");
        }
    }
}
