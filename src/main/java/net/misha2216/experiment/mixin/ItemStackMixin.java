package net.misha2216.experiment.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.client.item.TooltipContext;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin({ItemStack.class})
public class ItemStackMixin {
    public ItemStackMixin() {
    }

    @Redirect(
            method = {"getTooltip(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/client/item/TooltipContext;)Ljava/util/List;"},
            at = @At(
                    value = "INVOKE",
                    target = "net/minecraft/item/ItemStack.hasNbt ()Z"
            ),
            slice = @Slice(
                    from = @At(
                            value = "INVOKE",
                            target = "net/minecraft/text/MutableText.formatted (Lnet/minecraft/util/Formatting;)Lnet/minecraft/text/MutableText;"
                    ),
                    to = @At(
                            value = "INVOKE",
                            target = "net/minecraft/nbt/NbtCompound.getKeys ()Ljava/util/Set;"
                    )
            )
    )
    private boolean injected(ItemStack itemStack, @Nullable PlayerEntity player, TooltipContext context) {
        return !itemStack.isFood() && itemStack.hasNbt();
    }
}
