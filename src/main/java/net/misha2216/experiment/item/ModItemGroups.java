package net.misha2216.experiment.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.misha2216.experiment.ExperimentMod;

public class ModItemGroups {
    public static final ItemGroup EXPERIMENT_GROUP = Registry.register(Registries.ITEM_GROUP,
            new Identifier(ExperimentMod.MOD_ID, "part"),
            FabricItemGroup.builder().displayName(Text.translatable("itemgroup.experiment"))
                    .icon(() -> new ItemStack(ModItems.PART)).entries((displayContext, entries) -> {
                        entries.add(ModItems.PART);


                    }).build());


    public static void registerItemGroups() {
        ExperimentMod.LOGGER.info("Registering Item Groups for " + ExperimentMod.MOD_ID);
    }
}