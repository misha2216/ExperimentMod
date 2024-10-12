package net.misha2216.experiment.client.render.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.util.Identifier;
import net.misha2216.experiment.ExperimentMod;
import net.misha2216.experiment.entity.DartEntity;

@Environment(value = EnvType.CLIENT)
public class DartEntityRenderer extends ProjectileEntityRenderer<DartEntity> {
    public static final Identifier TEXTURE = new Identifier(ExperimentMod.MOD_ID, "textures/entity/projectiles/poison_dart.png");

    public DartEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public Identifier getTexture(DartEntity dartEntity) {
        return TEXTURE;
    }
}