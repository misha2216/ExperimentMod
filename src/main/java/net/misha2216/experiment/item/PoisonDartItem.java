package net.misha2216.experiment.item;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TippedArrowItem;
import net.minecraft.sound.SoundCategory;
import net.minecraft.stat.Stats;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.misha2216.experiment.ExperimentMod;
import net.misha2216.experiment.entity.DartEntity;

import static net.minecraft.potion.PotionUtil.buildTooltip;


public class DartItem extends TippedArrowItem {
    StatusEffectInstance statusEffectInstance;

    public DartItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        world.playSound(null, user.getBlockPos(), ExperimentMod.POISON_DART_THROW, SoundCategory.NEUTRAL, 0.5f, 1.0f);

        if (!world.isClient) {
            DartEntity throwingDart = new DartEntity(world, user);
            throwingDart.setVelocity(user, user.getPitch(), user.getYaw(), 0.0f, 3.0f, 1.0f);
            throwingDart.setDamage(throwingDart.getDamage());
            throwingDart.setItem(itemStack);
            if (this.statusEffectInstance != null) {
                StatusEffectInstance potion = new StatusEffectInstance(statusEffectInstance);
                throwingDart.addEffect(potion);
                throwingDart.setColor(potion.getEffectType().getColor());
            }
            world.spawnEntity(throwingDart);
        }

        user.incrementStat(Stats.USED.getOrCreateStat(this));
        if (!user.getAbilities().creativeMode) {
            itemStack.decrement(1);
        }

        return TypedActionResult.success(itemStack, world.isClient());
    }

    public StatusEffectInstance getStatusEffectInstance() {
        return statusEffectInstance;
    }

//    public void buildTooltip(List<Text> list, float durationMultiplier) {
//        ArrayList<Pair<EntityAttribute, EntityAttributeModifier>> list3 = Lists.newArrayList();
//        if (this.statusEffectInstance == null) {
//            list.add(Text.translatable("effect.none").formatted(Formatting.GRAY));
//        } else {
//            MutableText mutableText = Text.translatable(statusEffectInstance.getTranslationKey());
//            StatusEffect statusEffect = statusEffectInstance.getEffectType();
//            Map<EntityAttribute, EntityAttributeModifier> map = statusEffect.getAttributeModifiers();
//            if (!map.isEmpty()) {
//                for (Map.Entry<EntityAttribute, EntityAttributeModifier> entry : map.entrySet()) {
//                    EntityAttributeModifier entityAttributeModifier = entry.getValue();
//                    EntityAttributeModifier entityAttributeModifier2 = new EntityAttributeModifier(entityAttributeModifier.getName(), statusEffect.adjustModifierAmount(statusEffectInstance.getAmplifier(), entityAttributeModifier), entityAttributeModifier.getOperation());
//                    list3.add(new Pair<>(entry.getKey(), entityAttributeModifier2));
//                }
//            }
//            if (statusEffectInstance.getAmplifier() > 0) {
//                mutableText = Text.translatable("potion.withAmplifier", mutableText, Text.translatable("potion.potency." + statusEffectInstance.getAmplifier()));
//            }
//            if (statusEffectInstance.getDuration() > 20) {
//                mutableText = Text.translatable("potion.withDuration", mutableText, StatusEffectUtil.getDurationText(statusEffectInstance, durationMultiplier));
//            }
//            list.add(mutableText.formatted(statusEffect.getCategory().getFormatting()));
//        }
//    }
}