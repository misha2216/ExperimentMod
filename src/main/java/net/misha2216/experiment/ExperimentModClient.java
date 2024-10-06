package net.misha2216.experiment;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.misha2216.experiment.client.render.entity.PoisonDartEntityRenderer;

public class ExperimentModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {

        // MODEL LAYERS
        EntityRendererRegistry.register(ExperimentMod.POISON_DART, PoisonDartEntityRenderer::new);
    }
}
