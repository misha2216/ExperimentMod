package net.misha2216.experiment.block;


import dev.maxoduke.mods.potioncauldron.PotionCauldron;
import dev.maxoduke.mods.potioncauldron.block.PotionCauldronBlock;
import dev.maxoduke.mods.potioncauldron.block.PotionCauldronBlockEntity;
import dev.maxoduke.mods.potioncauldron.networking.ServerNetworking;
import dev.maxoduke.mods.potioncauldron.networking.packets.ParticlePacket;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.potion.PotionUtil;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import net.misha2216.experiment.ExperimentMod;

import java.util.Map;

import static dev.maxoduke.mods.potioncauldron.block.PotionCauldronBlockInteraction.MAP;

public class PoisonDartCauldronBlockInteraction {
    public PoisonDartCauldronBlockInteraction() {
    }

    public static void bootstrap() {
        MAP.put(ExperimentMod.POISON_DART, PoisonDartCauldronBlockInteraction::createDartFromPotionCauldron);
    }

    private static ActionResult createDartFromPotionCauldron(BlockState blockState, World world, BlockPos blockPos, PlayerEntity playerEntity, Hand hand, ItemStack itemStack) {
        if (!PotionCauldron.CONFIG_MANAGER.clientOrServerConfig().shouldAllowCreatingTippedArrows()
                || blockState.get(LeveledCauldronBlock.LEVEL) == 0) {
            return ActionResult.PASS;
        } else if (world.isClient) {
            return ActionResult.success(true);
        } else {
            var cauldronLevelToDarts = PotionCauldron.CONFIG_MANAGER.serverConfig().maxTippedArrowsPerLevel();
            var currentCauldronLevel = blockState.get(LeveledCauldronBlock.LEVEL);
            var maxPoisonDartsCount = cauldronLevelToDarts.get(currentCauldronLevel);
            var poisonDartsCount = Math.min(itemStack.getCount(), maxPoisonDartsCount);
            var usedCauldronLevels = -1;
            var var11 = cauldronLevelToDarts.entrySet().iterator();

            while (var11.hasNext()) {
                Map.Entry<Integer, Integer> item = var11.next();
                if (poisonDartsCount <= item.getValue()) {
                    usedCauldronLevels = item.getKey();
                    break;
                }
            }

            var remainingCauldronLevels = currentCauldronLevel - usedCauldronLevels;
            var blockEntity = (PotionCauldronBlockEntity) world.getBlockEntity(blockPos);
            var potion = blockEntity.getPotion();
            var poisonDarts = new ItemStack(ExperimentMod.POISON_DART);
            poisonDarts.setCount(poisonDartsCount);
            PotionUtil.setPotion(poisonDarts, potion);
            ServerNetworking.sendParticlesToClients(new ParticlePacket(ParticleTypes.EFFECT, blockPos, PotionUtil.getColor(potion), true));
            world.setBlockState(blockPos, remainingCauldronLevels == 0 ? Blocks.CAULDRON.getDefaultState() : blockState.with(PotionCauldronBlock.LEVEL, remainingCauldronLevels));
            if (!playerEntity.isCreative()) {
                itemStack.decrement(poisonDartsCount);
            }

            var inventory = playerEntity.getInventory();
            if (!inventory.insertStack(poisonDarts)) {
                playerEntity.dropItem(poisonDarts, false);
            }

            world.playSound(null, blockPos, SoundEvents.ENTITY_GENERIC_SPLASH, SoundCategory.BLOCKS, 1.0F, 1.0F);
            world.emitGameEvent(null, GameEvent.FLUID_PICKUP, blockPos);
            return ActionResult.success(false);
        }
    }
}
