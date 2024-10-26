package net.misha2216.experiment.entity;

import com.google.common.collect.Sets;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.potion.PotionUtil;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Util;
import net.minecraft.world.World;
import net.misha2216.experiment.ExperimentMod;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PoisonDartEntity extends PersistentProjectileEntity {
    private static final TrackedData<ItemStack> ITEM = DataTracker.registerData(PoisonDartEntity.class, TrackedDataHandlerRegistry.ITEM_STACK);
    private final List<StatusEffectInstance> effects = new ArrayList<>();

    public PoisonDartEntity(EntityType<? extends PoisonDartEntity> entityType, World world) {
        super(entityType, world);
    }

    public PoisonDartEntity(World world, double x, double y, double z) {
        super(ExperimentMod.POISON_DART_ENTITY, x, y, z, world);
    }

    public PoisonDartEntity(World world, LivingEntity owner) {
        super(ExperimentMod.POISON_DART_ENTITY, owner, world);
    }

    protected ItemStack getItem() {
        return this.getDataTracker().get(ITEM);
    }

    public void setItem(ItemStack item) {
        this.getDataTracker().set(ITEM, Util.make(item.copy(), stack -> stack.setCount(1)));
    }

    @Override
    public double getDamage() {
        return 0.1f;
    }

    @Override
    public int getPunch() {
        return 0;
    }

    public void addEffects(List<StatusEffectInstance> effects) {
        this.effects.addAll(effects);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.getDataTracker().startTracking(ITEM, ItemStack.EMPTY);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.getWorld().isClient) {
            if (this.inGround) {
                if (this.inGroundTime % 5 == 0) {
                    this.spawnParticles(1);
                }
            } else {
                this.spawnParticles(2);
            }
        } else if (this.inGround && this.inGroundTime != 0 && !this.effects.isEmpty() && this.inGroundTime >= 600) {
            this.getWorld().sendEntityStatus(this, (byte) 0);
            //this.effects.clear();
        }
    }

    @Override
    protected boolean tryPickup(PlayerEntity player) {
        switch (this.pickupType) {
            case ALLOWED: {
                if (player.isCreative()) {
                    return true;
                } else {
                    return player.getInventory().insertStack(this.asItemStack());
                }
            }
            case CREATIVE_ONLY: {
                return player.getAbilities().creativeMode;
            }
        }
        return false;
    }

    private void spawnParticles(int amount) {
        if (!this.effects.isEmpty())
        {
            int i = this.effects.get(0).getEffectType().getColor();
            if (i == -1 || amount <= 0) {
                return;
            }
            double d = (double) (i >> 16 & 0xFF) / 255.0;
            double e = (double) (i >> 8 & 0xFF) / 255.0;
            double f = (double) (i >> 0 & 0xFF) / 255.0;
            for (int j = 0; j < amount; ++j) {
                this.getWorld().addParticle(ParticleTypes.ENTITY_EFFECT, this.getParticleX(0.5), this.getRandomBodyY(), this.getParticleZ(0.5), d, e, f);
            }
        }
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);

        if (!this.effects.isEmpty()) {
            NbtList nbtList = new NbtList();
            for (StatusEffectInstance statusEffectInstance : this.effects) {
                nbtList.add(statusEffectInstance.writeNbt(new NbtCompound()));
            }
            nbt.put("CustomPotionEffects", nbtList);
        }

        if (!this.getItem().isEmpty()) {
            nbt.put("Item", this.getItem().writeNbt(new NbtCompound()));
        }
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);

        List<StatusEffectInstance> effects = PotionUtil.getCustomPotionEffects(nbt);
        this.addEffects(effects);
        this.setItem(ItemStack.fromNbt(nbt.getCompound("Item")));
    }

    @Override
    protected void onHit(LivingEntity target) {
        super.onHit(target);
        Entity entity = this.getEffectCause();

        if (!this.effects.isEmpty()) {
            for (StatusEffectInstance statusEffectInstance : this.effects) {
                target.addStatusEffect(statusEffectInstance, entity);
            }
        }
    }

    @Override
    protected ItemStack asItemStack() {
        return this.getItem();
    }

    @Override
    public void handleStatus(byte status) {
        if (status == 0 && !this.effects.isEmpty()) {
            int i = this.effects.get(0).getEffectType().getColor();
            if (i != -1) {
                double d = (double) (i >> 16 & 0xFF) / 255.0;
                double e = (double) (i >> 8 & 0xFF) / 255.0;
                double f = (double) (i >> 0 & 0xFF) / 255.0;
                for (int j = 0; j < 20; ++j) {
                    this.getWorld().addParticle(ParticleTypes.ENTITY_EFFECT, this.getParticleX(0.5), this.getRandomBodyY(), this.getParticleZ(0.5), d, e, f);
                }
            }
        } else {
            super.handleStatus(status);
        }
    }

    @Override
    protected SoundEvent getHitSound() {
        return ExperimentMod.POISON_DART_ENTITY_HIT;
    }
}
