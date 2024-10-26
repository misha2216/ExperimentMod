package net.misha2216.experiment;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.player.UseItemCallback;
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
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Position;
import net.minecraft.world.World;

import net.misha2216.experiment.entity.PoisonDartEntity;
import net.misha2216.experiment.event.UseItemEvent;
import net.misha2216.experiment.item.ModItemGroups;
import net.misha2216.experiment.item.ModItems;
import net.misha2216.experiment.item.ThrowingDartItem;
import net.misha2216.experiment.block.PoisonDartCauldronBlockInteraction;
import net.misha2216.experiment.potion.ModPotions;
import net.misha2216.experiment.statuseffect.CorruptedEffect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Stream;

public class ExperimentMod implements ModInitializer {
	public static final String MOD_ID = "experiment";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	//Status effects
	public static final StatusEffect CORRUPTED = registerStatusEffect("corrupted", new CorruptedEffect(StatusEffectCategory.HARMFUL, 0xAA00AA));

	//Entities
	public static final EntityType<PoisonDartEntity> POISON_DART_ENTITY = FabricEntityTypeBuilder.<PoisonDartEntity>create(SpawnGroup.MISC, PoisonDartEntity::new)
			.dimensions(EntityDimensions.changing(0.5f, 0.5f))
			.trackRangeBlocks(4)
			.trackedUpdateRate(20)
			.build();

	//Items
	public static Item POISON_DART = registerDartItem("poison_dart", new ThrowingDartItem((new Item.Settings()).maxCount(8)));

	public static Identifier id(String path) {
		return new Identifier(MOD_ID, path);
	}

	//Sounds
	public static final SoundEvent POISON_DART_ENTITY_HIT = SoundEvent.of(id("entity.poison_dart.hit"));
	public static final SoundEvent POISON_DART_COAT = SoundEvent.of(id("item.poison_dart.coat"));
	public static final SoundEvent POISON_DART_THROW = SoundEvent.of(id("item.poison_dart.throw"));


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
				Potion potion = PotionUtil.getPotion(itemStack);
				if (potion != null) {
					List<StatusEffectInstance> effects = potion.getEffects();
					throwingDart.addEffects(effects);
				}
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
		PoisonDartCauldronBlockInteraction.bootstrap();

		ModItemGroups.registerItemGroups();
		ModItems.registerModItems();

		// POTIONS
		ModPotions.registerPotionRecipes();

		// ENTITIES
		registerEntity("poison_dart_entity", POISON_DART_ENTITY);

		// ITEMS
		// POISON_DART = registerDartItem("poison_dart", new PoisonDartItem((new Item.Settings()).maxCount(8)));
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register((entries) -> entries.addAll(Stream.of(
				POISON_DART
		).map(Item::getDefaultStack).toList()));

		// SOUNDS
		Registry.register(Registries.SOUND_EVENT, POISON_DART_ENTITY_HIT.getId(), POISON_DART_ENTITY_HIT);
		Registry.register(Registries.SOUND_EVENT, POISON_DART_COAT.getId(), POISON_DART_COAT);
		Registry.register(Registries.SOUND_EVENT, POISON_DART_THROW.getId(), POISON_DART_THROW);

		// TICK


		// EVENT
		UseItemCallback.EVENT.register(new UseItemEvent());

	}
}