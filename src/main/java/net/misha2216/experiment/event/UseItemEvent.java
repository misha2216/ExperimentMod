package net.misha2216.experiment.event;


import java.util.List;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionUtil;
import net.minecraft.world.World;
import net.minecraft.sound.SoundEvents;
import net.minecraft.sound.SoundCategory;

public class UseItemEvent implements UseItemCallback {
    public UseItemEvent() {
    }

    public TypedActionResult<ItemStack> interact(PlayerEntity user, World world, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (stack.isFood()) {
            ItemStack poisoned;
            if (stack.getOrCreateNbt().getBoolean("effects")) {
                poisoned = stack.copy();
                poisoned.setCount(1);
            } else {
                poisoned = stack.getItem().getDefaultStack();
            }

            ItemStack itemStack = user.getOffHandStack();
            List list;
            if (!itemStack.isOf(Items.POTION) && !itemStack.isOf(Items.SPLASH_POTION)) {
                if (itemStack.isOf(Items.LINGERING_POTION)) {
                    list = PotionUtil.getPotionEffects(itemStack);
                    if (!list.isEmpty() && PotionUtil.getPotionEffects(stack).isEmpty()) {
                        world.playSound((PlayerEntity)null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_SPLASH_POTION_BREAK, SoundCategory.PLAYERS, 0.5F, 0.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F));
                        PotionUtil.getCustomPotionEffects(stack.getNbt(), list);
                        PotionUtil.setCustomPotionEffects(stack, list);
                        stack.getOrCreateNbt().putBoolean("effects", true);
                        if (!user.getAbilities().creativeMode) {
                            itemStack.decrement(1);
                        }

                        return TypedActionResult.success(stack);
                    }
                }
                //Возврат бутылок
            } else {
                list = PotionUtil.getPotionEffects(itemStack);
                if (!list.isEmpty()) {
                    PotionUtil.getCustomPotionEffects(poisoned.getNbt(), list);
                    PotionUtil.setCustomPotionEffects(poisoned, list);
                    poisoned.getOrCreateNbt().putBoolean("effects", true);
                    if (!user.getAbilities().creativeMode) {
                        boolean is_potion = itemStack.isOf(Items.POTION);
                        itemStack.decrement(1);
                        if (is_potion) {
                            if (itemStack.isEmpty()) {
                                user.getInventory().offHand.set(0, new ItemStack(Items.GLASS_BOTTLE));
                            } else {
                                user.getInventory().insertStack(new ItemStack(Items.GLASS_BOTTLE));
                            }
                        }
                    }

                    world.playSound((PlayerEntity)null, user.getX(), user.getY(), user.getZ(), itemStack.isOf(Items.SPLASH_POTION) ? SoundEvents.ENTITY_SPLASH_POTION_BREAK : SoundEvents.ENTITY_WANDERING_TRADER_DRINK_POTION, SoundCategory.PLAYERS, 0.5F, 0.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F));
                    stack.decrement(1);
                    if (stack.isEmpty()) {
                        user.getInventory().insertStack(user.getInventory().selectedSlot, poisoned);
                    } else {
                        user.getInventory().insertStack(poisoned);
                    }

                    return TypedActionResult.success(stack);
                }
            }
        }

        return TypedActionResult.pass(stack);
    }
}
