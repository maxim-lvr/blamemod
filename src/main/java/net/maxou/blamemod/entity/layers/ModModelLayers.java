package net.maxou.blamemod.entity.layers;

import net.maxou.blamemod.BlameMod;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

public class ModModelLayers {
    public static final ModelLayerLocation RHINO_LAYER = new ModelLayerLocation(
            new ResourceLocation(BlameMod.MOD_ID, "rhino_layer"), "rhino_layer");

    public static final ModelLayerLocation DRONE_LAYER = new ModelLayerLocation(
            new ResourceLocation(BlameMod.MOD_ID, "drone_layer"), "drone_layer");

    public static final ModelLayerLocation ZOMBIE_CYBORG_LAYER = new ModelLayerLocation(
            new ResourceLocation(BlameMod.MOD_ID, "zombie_cyborg_layer"), "zombie_cyborg_layer");

    public static final ModelLayerLocation CUBE_BOSS_LAYER = new ModelLayerLocation(
            new ResourceLocation(BlameMod.MOD_ID, "cube_boss_layer"), "cube_boss_layer");

    public static final ModelLayerLocation MAGIC_PROJECTILE_LAYER = new ModelLayerLocation(
            new ResourceLocation(BlameMod.MOD_ID, "magic_projectile_layer"), "magic_projectile_layer");

}
