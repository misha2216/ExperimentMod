package net.misha2216.experiment.mixin;


import dev.maxoduke.mods.potioncauldron.PotionCauldron;
import dev.maxoduke.mods.potioncauldron.block.PotionCauldronBlock;
import dev.maxoduke.mods.potioncauldron.block.PotionCauldronBlockEntity;
import dev.maxoduke.mods.potioncauldron.block.PotionCauldronBlockInteraction;
import dev.maxoduke.mods.potioncauldron.networking.ServerNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.potion.Potion;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.misha2216.experiment.ExperimentMod;
import net.misha2216.experiment.item.ModItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.logging.Level;

import net.minecraft.world.InteractionResult;

import static dev.maxoduke.mods.potioncauldron.block.PotionCauldronBlockInteraction.MAP;

@Mixin(PotionCauldronBlockInteraction.class)
public class CauldronDartMixin {
    @Inject(method = "bootstrap", at = @At("HEAD"), cancellable = true)
    private static void bootstrap(CallbackInfo ci){
        MAP.put(ExperimentMod.POISON_DART, PotionCauldronBlockInteraction::createPoisonDartFromPotionCauldron);
            }
    private static InteractionResult createPoisonDartFromPotionCauldron(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand ignored, ItemStack stack)
    {
        if (!PotionCauldron.CONFIG_MANAGER.clientOrServerConfig().shouldAllowCreatingTippedArrows())
            return InteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;

        if (blockState.getValue(LayeredCauldronBlock.LEVEL) == 0)
            return InteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;

        if (level.isClientSide)
            return InteractionResult.sidedSuccess(true);

        HashMap<Integer, Integer> cauldronLevelToArrows = PotionCauldron.CONFIG_MANAGER.serverConfig().maxTippedArrowsPerLevel();

        int currentCauldronLevel = blockState.getValue(LayeredCauldronBlock.LEVEL);
        int maxTippedArrowCount = cauldronLevelToArrows.get(currentCauldronLevel);
        int tippedArrowCount = Math.min(stack.getCount(), maxTippedArrowCount);

        int usedCauldronLevels = -1;
        for (var item : cauldronLevelToArrows.entrySet())
        {
            if (tippedArrowCount <= item.getValue())
            {
                usedCauldronLevels = item.getKey();
                break;
            }
        }
        int remainingCauldronLevels = currentCauldronLevel - usedCauldronLevels;

        PotionCauldronBlockEntity blockEntity = (PotionCauldronBlockEntity) level.getBlockEntity(blockPos);
        Holder<Potion> potion = blockEntity.getPotion();

        ItemStack tippedArrows = new ItemStack(Items.TIPPED_ARROW);
        tippedArrows.setCount(tippedArrowCount);
        tippedArrows.set(DataComponents.POTION_CONTENTS, new PotionContents(potion));

        ServerNetworking.sendParticlesToClients(new ParticlePayload(ParticleTypes.EFFECT, blockPos, PotionContents.getColor(potion.value().getEffects()), true));
        level.setBlockAndUpdate(blockPos, remainingCauldronLevels == 0 ? Blocks.CAULDRON.defaultBlockState() : blockState.setValue(PotionCauldronBlock.LEVEL, remainingCauldronLevels));

        if (!player.isCreative())
            stack.shrink(tippedArrowCount);

        Inventory inventory = player.getInventory();
        if (!inventory.add(tippedArrows))
            player.drop(tippedArrows, false);

        level.playSound(null, blockPos, SoundEvents.GENERIC_SPLASH, SoundSource.BLOCKS, 1.0f, 1.0f);
        level.gameEvent(null, GameEvent.FLUID_PICKUP, blockPos);

        return InteractionResult.sidedSuccess(false);
    }
}
