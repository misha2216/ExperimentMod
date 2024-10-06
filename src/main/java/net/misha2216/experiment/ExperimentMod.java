package net.misha2216.experiment;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.ProjectileDispenserBehavior;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Position;
import net.minecraft.world.World;

import net.misha2216.experiment.entity.PoisonDartEntity;
import net.misha2216.experiment.item.ModItemGroups;
import net.misha2216.experiment.item.ModItems;
import net.misha2216.experiment.item.ThrowingDartItem;
import net.misha2216.experiment.potion.ModPotions;
import net.misha2216.experiment.statuseffect.CorruptedEffect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Stream;

public class ExperimentMod implements ModInitializer {
	public static final String MOD_ID = "experiment";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	//Status effects
	public static final StatusEffect CORRUPTED = registerStatusEffect("corrupted", new CorruptedEffect(StatusEffectCategory.HARMFUL, 0xAA00AA));

	//Entities
	public static final EntityType<PoisonDartEntity> POISON_DART = FabricEntityTypeBuilder.<PoisonDartEntity>create(SpawnGroup.MISC, PoisonDartEntity::new)
			.dimensions(EntityDimensions.changing(0.5f, 0.5f))
			.trackRangeBlocks(4)
			.trackedUpdateRate(20)
			.build();

	//Items
	public static Item THROWING_DART;
	public static Item CORRUPTED_POISON_DART;
	public static Item POISON_POISON_DART;
	public static Item SPEED_POISON_DART;
	public static Item STRENGTH_POISON_DART;
	public static Item NIGHT_VISION_POISON_DART;
	public static Item JUMP_BOOST_POISON_DART;
	public static Item INVISIBILITY_POISON_DART;
	public static Item REGENERATION_POISON_DART;
	public static Item FIRE_RESISTANCE_POISON_DART;
	public static Item ABSORPTION_POISON_DART;
	public static Item DOLPHIN_POISON_DART;
	public static Item SLOW_FALL_POISON_DART;
	public static Item SLOWNESS_POISON_DART;
	public static Item MINING_FATIQUE_POISON_DART;
	public static Item WEAKNESS_POISON_DART;
	public static Item BLINDNESS_POISON_DART;
	public static Item LEVITATION_POISON_DART;
	public static Item GLOWING_POISON_DART;
	public static Item WITHER_POISON_DART;
	public static Item DARKNESS_POISON_DART;
	public static Item HUNGER_POISON_DART;
	public static Item NAUSEA_POISON_DART;
	public static Item WATER_BREATHING_POISON_DART;

	public static Identifier id(String path) {
		return new Identifier(MOD_ID, path);
	}

	//Sounds
	public static final SoundEvent ENTITY_POISON_DART_HIT = SoundEvent.of(id("entity.poison_dart.hit"));
	public static final SoundEvent ITEM_POISON_DART_COAT = SoundEvent.of(id("item.poison_dart.coat"));
	public static final SoundEvent ITEM_POISON_DART_THROW = SoundEvent.of(id("item.poison_dart.throw"));


	private static <T extends Entity> void registerEntity(String name, EntityType<T> entityType) {
		Registry.register(Registries.ENTITY_TYPE, id(name), entityType);
	}

	public static <T extends Item> T registerItem(String name, T item) {
		Registry.register(Registries.ITEM, id(name), item);
		return item;
	}

	public static Item registerDartItem(String name, Item item) {
		registerItem(name, item);

		DispenserBlock.registerBehavior(item, new ProjectileDispenserBehavior() {
			@Override
			protected ProjectileEntity createProjectile(World world, Position position, ItemStack itemStack) {
				PoisonDartEntity throwingDart = new PoisonDartEntity(world, position.getX(), position.getY(), position.getZ());
				throwingDart.setDamage(throwingDart.getDamage());
				throwingDart.setItem(itemStack);
				StatusEffectInstance statusEffectInstance = ((ThrowingDartItem) itemStack.getItem()).getStatusEffectInstance();
				if (statusEffectInstance != null) {
					StatusEffectInstance potion = new StatusEffectInstance(statusEffectInstance);
					throwingDart.addEffect(potion);
					throwingDart.setColor(potion.getEffectType().getColor());
				}

				itemStack.decrement(1);
				return throwingDart;
			}
		});

		return item;
	}

	private static <T extends StatusEffect> T registerStatusEffect(String name, T effect) {
		Registry.register(Registries.STATUS_EFFECT, id(name), effect);
		return effect;
	}

	@Override
	public void onInitialize() {
		ModItemGroups.registerItemGroups();
		ModItems.registerModItems();


		// POTIONS
		ModPotions.registerPotionRecipes();

		// ENTITIES
		registerEntity("poison_dart", POISON_DART);

		// ITEMS
		THROWING_DART = registerDartItem("throwing_dart", new ThrowingDartItem((new Item.Settings()).maxCount(64), null));
		CORRUPTED_POISON_DART = registerDartItem("corrupted_poison_dart", new ThrowingDartItem((new Item.Settings()).maxCount(8), new StatusEffectInstance(ExperimentMod.CORRUPTED, 1200))); // 20s
		POISON_POISON_DART = registerDartItem("poison_poison_dart", new ThrowingDartItem((new Item.Settings()).maxCount(8), new StatusEffectInstance(StatusEffects.POISON, 1200,0))); // 20s
		SPEED_POISON_DART = registerDartItem("speed_poison_dart", new ThrowingDartItem((new Item.Settings()).maxCount(8), new StatusEffectInstance(StatusEffects.SPEED, 1200,0))); // 20s
		STRENGTH_POISON_DART = registerDartItem("strength_poison_dart", new ThrowingDartItem((new Item.Settings()).maxCount(8), new StatusEffectInstance(StatusEffects.STRENGTH, 1200,0))); // 20s
		NIGHT_VISION_POISON_DART = registerDartItem("night_vision_poison_dart", new ThrowingDartItem((new Item.Settings()).maxCount(8), new StatusEffectInstance(StatusEffects.NIGHT_VISION, 1200,0))); // 20s
		JUMP_BOOST_POISON_DART = registerDartItem("jump_boost_poison_dart", new ThrowingDartItem((new Item.Settings()).maxCount(8), new StatusEffectInstance(StatusEffects.JUMP_BOOST, 1200,0))); // 20s
		INVISIBILITY_POISON_DART = registerDartItem("invisibility_poison_dart", new ThrowingDartItem((new Item.Settings()).maxCount(8), new StatusEffectInstance(StatusEffects.INVISIBILITY, 1200,0))); // 20s
		REGENERATION_POISON_DART = registerDartItem("regeneration_poison_dart", new ThrowingDartItem((new Item.Settings()).maxCount(8), new StatusEffectInstance(StatusEffects.REGENERATION, 1200,0))); // 20s
		FIRE_RESISTANCE_POISON_DART = registerDartItem("fire_resistance_poison_dart", new ThrowingDartItem((new Item.Settings()).maxCount(8), new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 1200,0))); // 20s
		ABSORPTION_POISON_DART = registerDartItem("absorption_poison_dart", new ThrowingDartItem((new Item.Settings()).maxCount(8), new StatusEffectInstance(StatusEffects.ABSORPTION, 1200,0))); // 20s
		DOLPHIN_POISON_DART = registerDartItem("dolphin_poison_dart", new ThrowingDartItem((new Item.Settings()).maxCount(8), new StatusEffectInstance(StatusEffects.DOLPHINS_GRACE, 1200,0))); // 20s
		SLOW_FALL_POISON_DART = registerDartItem("slow_fall_poison_dart", new ThrowingDartItem((new Item.Settings()).maxCount(8), new StatusEffectInstance(StatusEffects.SLOW_FALLING, 1200,0))); // 20s
		SLOWNESS_POISON_DART = registerDartItem("slowness_poison_dart", new ThrowingDartItem((new Item.Settings()).maxCount(8), new StatusEffectInstance(StatusEffects.SLOWNESS, 1200,0))); // 20s
		MINING_FATIQUE_POISON_DART = registerDartItem("mining_fatique_poison_dart", new ThrowingDartItem((new Item.Settings()).maxCount(8), new StatusEffectInstance(StatusEffects.MINING_FATIGUE, 1200,0))); // 20s
		WEAKNESS_POISON_DART = registerDartItem("weakness_poison_dart", new ThrowingDartItem((new Item.Settings()).maxCount(8), new StatusEffectInstance(StatusEffects.WEAKNESS, 1200,0))); // 20s
		BLINDNESS_POISON_DART = registerDartItem("blindness_poison_dart", new ThrowingDartItem((new Item.Settings()).maxCount(8), new StatusEffectInstance(StatusEffects.BLINDNESS, 1200,0))); // 20s
		LEVITATION_POISON_DART = registerDartItem("levitation_poison_dart", new ThrowingDartItem((new Item.Settings()).maxCount(8), new StatusEffectInstance(StatusEffects.LEVITATION, 1200,0))); // 20s
		GLOWING_POISON_DART = registerDartItem("glowing_poison_dart", new ThrowingDartItem((new Item.Settings()).maxCount(8), new StatusEffectInstance(StatusEffects.GLOWING, 1200,0))); // 20s
		WITHER_POISON_DART = registerDartItem("wither_poison_dart", new ThrowingDartItem((new Item.Settings()).maxCount(8), new StatusEffectInstance(StatusEffects.WITHER, 1200,0))); // 20s
		DARKNESS_POISON_DART = registerDartItem("darkness_poison_dart", new ThrowingDartItem((new Item.Settings()).maxCount(8), new StatusEffectInstance(StatusEffects.DARKNESS, 1200,0))); // 20s
		HUNGER_POISON_DART = registerDartItem("hunger_poison_dart", new ThrowingDartItem((new Item.Settings()).maxCount(8), new StatusEffectInstance(StatusEffects.HUNGER, 1200,0))); // 20s
		NAUSEA_POISON_DART = registerDartItem("nausea_poison_dart", new ThrowingDartItem((new Item.Settings()).maxCount(8), new StatusEffectInstance(StatusEffects.NAUSEA, 1200,0))); // 20s
		WATER_BREATHING_POISON_DART = registerDartItem("water_breathing_poison_dart", new ThrowingDartItem((new Item.Settings()).maxCount(8), new StatusEffectInstance(StatusEffects.WATER_BREATHING, 1200,0))); // 20s
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register((entries) -> entries.addAll(Stream.of(
				THROWING_DART,
				CORRUPTED_POISON_DART,
				POISON_POISON_DART,
				SPEED_POISON_DART,
				STRENGTH_POISON_DART,
				NIGHT_VISION_POISON_DART,
				JUMP_BOOST_POISON_DART,
				INVISIBILITY_POISON_DART,
				REGENERATION_POISON_DART,
				FIRE_RESISTANCE_POISON_DART,
				ABSORPTION_POISON_DART,
				DOLPHIN_POISON_DART,
				SLOW_FALL_POISON_DART,
				SLOWNESS_POISON_DART,
				MINING_FATIQUE_POISON_DART,
				WEAKNESS_POISON_DART,
				BLINDNESS_POISON_DART,
				LEVITATION_POISON_DART,
				GLOWING_POISON_DART,
				WITHER_POISON_DART,
				DARKNESS_POISON_DART,
				HUNGER_POISON_DART,
				NAUSEA_POISON_DART,
				WATER_BREATHING_POISON_DART
		).map(Item::getDefaultStack).toList()));

		// SOUNDS
		Registry.register(Registries.SOUND_EVENT, ENTITY_POISON_DART_HIT.getId(), ENTITY_POISON_DART_HIT);
		Registry.register(Registries.SOUND_EVENT, ITEM_POISON_DART_COAT.getId(), ITEM_POISON_DART_COAT);
		Registry.register(Registries.SOUND_EVENT, ITEM_POISON_DART_THROW.getId(), ITEM_POISON_DART_THROW);

		// TICK



	}
}