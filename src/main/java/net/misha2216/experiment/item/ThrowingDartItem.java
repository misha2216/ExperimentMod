package net.misha2216.experiment.item;

import com.google.common.collect.Lists;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.stat.Stats;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.misha2216.experiment.ExperimentMod;
import net.misha2216.experiment.entity.PoisonDartEntity;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static net.minecraft.potion.PotionUtil.buildTooltip;


public class ThrowingDartItem extends Item {
    StatusEffectInstance statusEffectInstance;

    public ThrowingDartItem(Settings settings, StatusEffectInstance statusEffectInstance) {
        super(settings);
        this.statusEffectInstance = statusEffectInstance;
    }

    public static StatusEffectInstance getDartPoisonEffect(Item item) {
        if (item == ExperimentMod.CORRUPTED_POISON_DART) {
            return new StatusEffectInstance(ExperimentMod.CORRUPTED, 1200); // 60s
        }
        else if (item == ExperimentMod.POISON_POISON_DART) {
            return new StatusEffectInstance(StatusEffects.POISON, 1200,0); // 60s
        }
        else if (item == ExperimentMod.SPEED_POISON_DART) {
            return new StatusEffectInstance(StatusEffects.SPEED, 1200,0); // 60s
        }
        else if (item == ExperimentMod.STRENGTH_POISON_DART) {
            return new StatusEffectInstance(StatusEffects.STRENGTH, 1200,0); // 60s
        }
        else if (item == ExperimentMod.NIGHT_VISION_POISON_DART) {
            return new StatusEffectInstance(StatusEffects.NIGHT_VISION, 1200,0); // 60s
        }
        else if (item == ExperimentMod.JUMP_BOOST_POISON_DART) {
            return new StatusEffectInstance(StatusEffects.JUMP_BOOST, 1200,0); // 60s
        }
        else if (item == ExperimentMod.REGENERATION_POISON_DART) {
            return new StatusEffectInstance(StatusEffects.REGENERATION, 1200,0); // 60s
        }
        else if (item == ExperimentMod.FIRE_RESISTANCE_POISON_DART) {
            return new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 1200,0); // 60s
        }
        else if (item == ExperimentMod.ABSORPTION_POISON_DART) {
            return new StatusEffectInstance(StatusEffects.ABSORPTION, 1200,0); // 60s
        }
        else if (item == ExperimentMod.DOLPHIN_POISON_DART) {
            return new StatusEffectInstance(StatusEffects.DOLPHINS_GRACE, 1200,0); // 60s
        }
        else if (item == ExperimentMod.SLOW_FALL_POISON_DART) {
            return new StatusEffectInstance(StatusEffects.SLOW_FALLING, 1200,0); // 60s
        }
        else if (item == ExperimentMod.SLOWNESS_POISON_DART) {
            return new StatusEffectInstance(StatusEffects.SLOWNESS, 1200,0); // 60s
        }
        else if (item == ExperimentMod.MINING_FATIQUE_POISON_DART) {
            return new StatusEffectInstance(StatusEffects.MINING_FATIGUE, 1200,0); // 60s
        }
        else if (item == ExperimentMod.WEAKNESS_POISON_DART) {
            return new StatusEffectInstance(StatusEffects.WEAKNESS, 1200,0); // 60s
        }
        else if (item == ExperimentMod.BLINDNESS_POISON_DART) {
            return new StatusEffectInstance(StatusEffects.BLINDNESS, 1200,0); // 60s
        }
        else if (item == ExperimentMod.LEVITATION_POISON_DART) {
            return new StatusEffectInstance(StatusEffects.LEVITATION, 1200,0); // 60s
        }
        else if (item == ExperimentMod.GLOWING_POISON_DART) {
            return new StatusEffectInstance(StatusEffects.GLOWING, 1200,0); // 60s
        }
        else if (item == ExperimentMod.WITHER_POISON_DART) {
            return new StatusEffectInstance(StatusEffects.WITHER, 1200,0); // 60s
        }
        else if (item == ExperimentMod.DARKNESS_POISON_DART) {
            return new StatusEffectInstance(StatusEffects.DARKNESS, 1200,0); // 60s
        }
        else if (item == ExperimentMod.HUNGER_POISON_DART) {
            return new StatusEffectInstance(StatusEffects.HUNGER, 1200,0); // 60s
        }
        else if (item == ExperimentMod.NAUSEA_POISON_DART) {
            return new StatusEffectInstance(StatusEffects.NAUSEA, 1200,0); // 60s
        }
        else if (item == ExperimentMod.WATER_BREATHING_POISON_DART) {
            return new StatusEffectInstance(StatusEffects.WATER_BREATHING, 1200,0); // 60s
        }
        return null;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        world.playSound(null, user.getBlockPos(), ExperimentMod.ITEM_POISON_DART_THROW, SoundCategory.NEUTRAL, 0.5f, 1.0f);

        if (!world.isClient) {
            PoisonDartEntity throwingDart = new PoisonDartEntity(world, user);
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

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        buildTooltip(tooltip, 1.0f);
    }

    public void buildTooltip(List<Text> list, float durationMultiplier) {
        ArrayList<Pair<EntityAttribute, EntityAttributeModifier>> list3 = Lists.newArrayList();
        if (this.statusEffectInstance == null) {
            list.add(Text.translatable("effect.none").formatted(Formatting.GRAY));
        } else {
            MutableText mutableText = Text.translatable(statusEffectInstance.getTranslationKey());
            StatusEffect statusEffect = statusEffectInstance.getEffectType();
            Map<EntityAttribute, EntityAttributeModifier> map = statusEffect.getAttributeModifiers();
            if (!map.isEmpty()) {
                for (Map.Entry<EntityAttribute, EntityAttributeModifier> entry : map.entrySet()) {
                    EntityAttributeModifier entityAttributeModifier = entry.getValue();
                    EntityAttributeModifier entityAttributeModifier2 = new EntityAttributeModifier(entityAttributeModifier.getName(), statusEffect.adjustModifierAmount(statusEffectInstance.getAmplifier(), entityAttributeModifier), entityAttributeModifier.getOperation());
                    list3.add(new Pair<EntityAttribute, EntityAttributeModifier>(entry.getKey(), entityAttributeModifier2));
                }
            }
            if (statusEffectInstance.getAmplifier() > 0) {
                mutableText = Text.translatable("potion.withAmplifier", mutableText, Text.translatable("potion.potency." + statusEffectInstance.getAmplifier()));
            }
            if (statusEffectInstance.getDuration() > 20) {
                mutableText = Text.translatable("potion.withDuration", mutableText, StatusEffectUtil.getDurationText(statusEffectInstance, durationMultiplier));
            }
            list.add(mutableText.formatted(statusEffect.getCategory().getFormatting()));
        }
    }
}