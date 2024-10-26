package net.misha2216.experiment.mixin;

import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.potion.PotionUtil;
import net.minecraft.util.Hand;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({AnimalEntity.class})
public abstract class AnimalEntityMixin extends MobEntity {
    protected AnimalEntityMixin(EntityType<? extends MobEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(
            method = {"eat(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/Hand;Lnet/minecraft/item/ItemStack;)V"},
            at = {@At("TAIL")}
    )
    private void injected(PlayerEntity player, Hand hand, ItemStack stack, CallbackInfo ci) {
        if (stack.getOrCreateNbt().getBoolean("effects")) {
            List<StatusEffectInstance> t = PotionUtil.getPotionEffects(stack);
            Iterator var6 = t.iterator();

            while(var6.hasNext()) {
                StatusEffectInstance statusEffectInstance = (StatusEffectInstance)var6.next();
                this.addStatusEffect(statusEffectInstance);
            }
        }

    }
}
