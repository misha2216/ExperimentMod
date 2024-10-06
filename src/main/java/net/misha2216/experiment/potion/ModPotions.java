package net.misha2216.experiment.potion;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Items;
import net.minecraft.item.PotionItem;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.recipe.BrewingRecipeRegistry;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.misha2216.experiment.ExperimentMod;
import net.misha2216.experiment.mixin.BrewingRecipeRegistryMixin;


public class ModPotions {
    public static final Potion DARKNESS_POTION;
    public static final Potion LONG_DARKNESS;

    public static final Potion WITHER_POTION;
    public static final Potion WITHER2;

    public static final Potion GLOWING_POTION;
    public static final Potion LONG_GLOWING;

    public static final Potion HUNGER_POTION;
    public static final Potion LONG_HUNGER;
    public static final Potion HUNGER2;

    public static final Potion NAUSEA_POTION;
    public static final Potion LONG_NAUSEA;

    public static final Potion BLINDNESS_POTION;
    public static final Potion LONG_BLINDNESS;

    public static final Potion MINING_FATIGUE_POTION;
    public static final Potion LONG_MINING_FATIGUE;
    public static final Potion MINING_FATIGUE2;

    public static final Potion ABSORPTION_POTION;
    public static final Potion LONG_ABSORPTION;
    public static final Potion ABSORPTION2;

    public static final Potion DOLPHIN_POTION;
    public static final Potion LONG_DOLPHIN;
    public static final Potion DOLPHIN2;


    static {
        DARKNESS_POTION = registerPotion("darkness_potion", new Potion(new StatusEffectInstance(StatusEffects.DARKNESS, 3000, 0)));
        LONG_DARKNESS = registerPotion("long_darkness_potion", new Potion(new StatusEffectInstance(StatusEffects.DARKNESS, 12000, 0)));

        WITHER_POTION = registerPotion("wither_potion", new Potion(new StatusEffectInstance(StatusEffects.WITHER, 3000, 0)));
        WITHER2 = registerPotion("wither_2_potion", new Potion(new StatusEffectInstance(StatusEffects.WITHER, 3000,1)));

        GLOWING_POTION = registerPotion("glowing_potion", new Potion(new StatusEffectInstance(StatusEffects.GLOWING, 3000, 0)));
        LONG_GLOWING = registerPotion("long_glowing_potion", new Potion(new StatusEffectInstance(StatusEffects.GLOWING, 12000, 0)));

        HUNGER_POTION = registerPotion("hunger_potion", new Potion(new StatusEffectInstance(StatusEffects.HUNGER, 3000, 0)));
        LONG_HUNGER = registerPotion("long_hunger_potion", new Potion(new StatusEffectInstance(StatusEffects.HUNGER, 12000, 0)));
        HUNGER2 = registerPotion("hunger_2_potion", new Potion(new StatusEffectInstance(StatusEffects.HUNGER, 3000,1)));

        NAUSEA_POTION = registerPotion("nausea_potion", new Potion(new StatusEffectInstance(StatusEffects.NAUSEA, 3000, 0)));
        LONG_NAUSEA = registerPotion("long_nausea_potion", new Potion(new StatusEffectInstance(StatusEffects.NAUSEA, 12000, 0)));

        BLINDNESS_POTION = registerPotion("blindness_potion", new Potion(new StatusEffectInstance(StatusEffects.BLINDNESS, 3000, 0)));
        LONG_BLINDNESS = registerPotion("long_blindness_potion", new Potion(new StatusEffectInstance(StatusEffects.BLINDNESS, 12000, 0)));

        MINING_FATIGUE_POTION = registerPotion("mining_fatigue_potion", new Potion(new StatusEffectInstance(StatusEffects.MINING_FATIGUE, 3000, 0)));
        LONG_MINING_FATIGUE = registerPotion("long_mining_fatigue_potion", new Potion(new StatusEffectInstance(StatusEffects.MINING_FATIGUE, 12000, 0)));
        MINING_FATIGUE2 = registerPotion("mining_fatigue_2_potion", new Potion(new StatusEffectInstance(StatusEffects.MINING_FATIGUE, 3000,1)));

        ABSORPTION_POTION = registerPotion("absorption_potion", new Potion(new StatusEffectInstance(StatusEffects.ABSORPTION, 3000, 0)));
        LONG_ABSORPTION = registerPotion("long_absorption_potion", new Potion(new StatusEffectInstance(StatusEffects.ABSORPTION, 12000, 0)));
        ABSORPTION2 = registerPotion("absorption_2_potion", new Potion(new StatusEffectInstance(StatusEffects.ABSORPTION, 3000,1)));

        DOLPHIN_POTION = registerPotion("dolphin_potion", new Potion(new StatusEffectInstance(StatusEffects.DOLPHINS_GRACE, 3000, 0)));
        LONG_DOLPHIN = registerPotion("long_dolphin_potion", new Potion(new StatusEffectInstance(StatusEffects.DOLPHINS_GRACE, 12000, 0)));
        DOLPHIN2 = registerPotion("dolphin_2_potion", new Potion(new StatusEffectInstance(StatusEffects.DOLPHINS_GRACE, 3000,1)));

    }

    public static Potion registerPotion(String name, Potion potion) {
        return Registry.register(Registries.POTION, new Identifier(ExperimentMod.MOD_ID, name), potion);
    }


    public static void registerPotionRecipes() {
        BrewingRecipeRegistryMixin.invokeRegisterPotionRecipe(Potions.AWKWARD, Items.ECHO_SHARD,
                ModPotions.DARKNESS_POTION);
        BrewingRecipeRegistryMixin.invokeRegisterPotionRecipe(ModPotions.DARKNESS_POTION, Items.REDSTONE,
                ModPotions.LONG_DARKNESS);

        BrewingRecipeRegistryMixin.invokeRegisterPotionRecipe(Potions.AWKWARD, Items.WITHER_SKELETON_SKULL,
                ModPotions.WITHER_POTION);
        BrewingRecipeRegistryMixin.invokeRegisterPotionRecipe(ModPotions.DARKNESS_POTION, Items.WITHER_ROSE,
                ModPotions.WITHER2);

        BrewingRecipeRegistryMixin.invokeRegisterPotionRecipe(Potions.AWKWARD, Items.GLOWSTONE,
                ModPotions.GLOWING_POTION);
        BrewingRecipeRegistryMixin.invokeRegisterPotionRecipe(ModPotions.GLOWING_POTION, Items.REDSTONE,
                ModPotions.LONG_GLOWING);

        BrewingRecipeRegistryMixin.invokeRegisterPotionRecipe(Potions.AWKWARD, Items.ROTTEN_FLESH,
                ModPotions.HUNGER_POTION);
        BrewingRecipeRegistryMixin.invokeRegisterPotionRecipe(ModPotions.HUNGER_POTION, Items.REDSTONE,
                ModPotions.LONG_HUNGER);
        BrewingRecipeRegistryMixin.invokeRegisterPotionRecipe(ModPotions.HUNGER_POTION, Items.GLOWSTONE_DUST,
                ModPotions.HUNGER2);

        BrewingRecipeRegistryMixin.invokeRegisterPotionRecipe(Potions.WATER_BREATHING, Items.FERMENTED_SPIDER_EYE,
                ModPotions.NAUSEA_POTION);
        BrewingRecipeRegistryMixin.invokeRegisterPotionRecipe(ModPotions.NAUSEA_POTION, Items.REDSTONE,
                ModPotions.LONG_NAUSEA);
        BrewingRecipeRegistryMixin.invokeRegisterPotionRecipe(Potions.LONG_WATER_BREATHING, Items.FERMENTED_SPIDER_EYE,
                ModPotions.LONG_NAUSEA);

        BrewingRecipeRegistryMixin.invokeRegisterPotionRecipe(Potions.AWKWARD, Items.WARPED_FUNGUS,
                ModPotions.BLINDNESS_POTION);
        BrewingRecipeRegistryMixin.invokeRegisterPotionRecipe(ModPotions.NAUSEA_POTION, Items.REDSTONE,
                ModPotions.LONG_BLINDNESS);

        BrewingRecipeRegistryMixin.invokeRegisterPotionRecipe(Potions.STRENGTH, Items.FERMENTED_SPIDER_EYE,
                ModPotions.MINING_FATIGUE_POTION);
        BrewingRecipeRegistryMixin.invokeRegisterPotionRecipe(ModPotions.MINING_FATIGUE_POTION, Items.REDSTONE,
                ModPotions.LONG_MINING_FATIGUE);
        BrewingRecipeRegistryMixin.invokeRegisterPotionRecipe(ModPotions.MINING_FATIGUE_POTION, Items.GLOWSTONE_DUST,
                ModPotions.MINING_FATIGUE2);
        BrewingRecipeRegistryMixin.invokeRegisterPotionRecipe(Potions.LONG_STRENGTH, Items.FERMENTED_SPIDER_EYE,
                ModPotions.LONG_MINING_FATIGUE);
        BrewingRecipeRegistryMixin.invokeRegisterPotionRecipe(Potions.STRONG_STRENGTH, Items.FERMENTED_SPIDER_EYE,
                ModPotions.MINING_FATIGUE2);

        BrewingRecipeRegistryMixin.invokeRegisterPotionRecipe(Potions.AWKWARD, Items.GOLDEN_APPLE,
                ModPotions.ABSORPTION_POTION);
        BrewingRecipeRegistryMixin.invokeRegisterPotionRecipe(ModPotions.ABSORPTION_POTION, Items.REDSTONE,
                ModPotions.LONG_ABSORPTION);
        BrewingRecipeRegistryMixin.invokeRegisterPotionRecipe(ModPotions.ABSORPTION_POTION, Items.GLOWSTONE_DUST,
                ModPotions.ABSORPTION2);

        BrewingRecipeRegistryMixin.invokeRegisterPotionRecipe(Potions.AWKWARD, Items.PRISMARINE_SHARD,
                ModPotions.DOLPHIN_POTION);
        BrewingRecipeRegistryMixin.invokeRegisterPotionRecipe(ModPotions.DOLPHIN_POTION, Items.REDSTONE,
                ModPotions.LONG_DOLPHIN);
        BrewingRecipeRegistryMixin.invokeRegisterPotionRecipe(ModPotions.DOLPHIN_POTION, Items.GLOWSTONE_DUST,
                ModPotions.DOLPHIN2);


    }
}