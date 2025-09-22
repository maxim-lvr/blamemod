package net.maxou.blamemod.entity.client;

import net.maxou.blamemod.BlameMod;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

public class ModModelLayers {
    public static final ModelLayerLocation RHINO_LAYER = new ModelLayerLocation(
            new ResourceLocation(BlameMod.MOD_ID, "rhino_layer"), "main");

    public static final ModelLayerLocation DRONE_LAYER = new ModelLayerLocation(
            new ResourceLocation(BlameMod.MOD_ID, "drone_layer"), "main");

    public static final ModelLayerLocation ZOMBIE_CYBORG_LAYER = new ModelLayerLocation(
            new ResourceLocation(BlameMod.MOD_ID, "zombie_cyborg_layer"), "main");

}
