package net.misha2216.experiment;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.misha2216.experiment.client.render.entity.PoisonDartEntityRenderer;
import net.misha2216.experiment.entity.PoisonDartEntity;
import net.misha2216.experiment.item.PoisonDartItem;

public class ExperimentModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> {
            PoisonDartItem item = (PoisonDartItem) stack.getItem();
            return tintIndex == 0 ? item.getColor(stack) : 0xFFFFFF;
        }, ExperimentMod.POISON_DART);

        // MODEL LAYERS
        EntityRendererRegistry.register(ExperimentMod.POISON_DART_ENTITY, PoisonDartEntityRenderer::new);
    }
}
