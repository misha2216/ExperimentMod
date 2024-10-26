package net.misha2216.experiment.mixin;

import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtil;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({LivingEntity.class})
public abstract class LivingEntityMixin extends Entity {
    @Shadow public abstract boolean addStatusEffect(StatusEffectInstance effect);

    protected LivingEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(
            method = {"eatFood(Lnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;)Lnet/minecraft/item/ItemStack;"},
            at = {@At(
                    value = "INVOKE",
                    target = "net/minecraft/item/ItemStack.isFood ()Z",
                    shift = Shift.AFTER
            )}
    )
    private void injected(World world, ItemStack stack, CallbackInfoReturnable<ItemStack> ci) {
        if (stack.isFood() && stack.getOrCreateNbt().getBoolean("effects")) {
            List<StatusEffectInstance> t = PotionUtil.getPotionEffects(stack);
            Iterator var5 = t.iterator();

            while(var5.hasNext()) {
                StatusEffectInstance statusEffectInstance = (StatusEffectInstance)var5.next();
                ((LivingEntityMixin)this).addStatusEffect(statusEffectInstance);
            }
        }

    }
}