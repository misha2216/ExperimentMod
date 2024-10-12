package net.misha2216.experiment.item;

import dev.maxoduke.mods.potioncauldron.block.PotionCauldronBlock;
import net.minecraft.block.Block;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.TippedArrowItem;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.sound.SoundCategory;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.misha2216.experiment.ExperimentMod;
import net.misha2216.experiment.entity.PoisonDartEntity;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;


public class PoisonDartItem extends TippedArrowItem {
    private boolean isCauldronClicked;

    public PoisonDartItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        world.playSound(null, user.getBlockPos(), ExperimentMod.POISON_DART_THROW, SoundCategory.NEUTRAL, 0.5f, 1.0f);

        if (!world.isClient && !isCauldronClicked) {
            PoisonDartEntity poisonDart = new PoisonDartEntity(world, user);
            poisonDart.setVelocity(user, user.getPitch(), user.getYaw(), 0.0f, 3.0f, 1.0f);
            poisonDart.setDamage(poisonDart.getDamage());
            poisonDart.setItem(itemStack);
            Potion potion = PotionUtil.getPotion(itemStack);
            if (potion != null) {
                List<StatusEffectInstance> effects = potion.getEffects();
                poisonDart.addEffects(effects);
            }
            world.spawnEntity(poisonDart);

            user.incrementStat(Stats.USED.getOrCreateStat(this));
            if (!user.getAbilities().creativeMode) {
                itemStack.decrement(1);
            }
        }

        return TypedActionResult.success(itemStack, world.isClient());
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        Block clickedBlock = world.getBlockState(context.getBlockPos()).getBlock();

        if (!(clickedBlock instanceof PotionCauldronBlock)) {
            isCauldronClicked = false;
            return ActionResult.PASS;
        } else if (!world.isClient()) {
            isCauldronClicked = true;
        }

        return ActionResult.SUCCESS;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        PotionUtil.buildTooltip(stack, tooltip, 1.0f);
    }

    public int getColor(ItemStack itemStack)
    {
        Potion potion = PotionUtil.getPotion(itemStack);
        List<StatusEffectInstance> effects = potion.getEffects();

        if (!effects.isEmpty())
        {
            return effects.get(0).getEffectType().getColor();
        }
        else
            return 0;
    }
}