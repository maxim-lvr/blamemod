package net.maxou.blamemod.event;

import net.maxou.blamemod.BlameMod;
import net.maxou.blamemod.entity.ModEntities;
import net.maxou.blamemod.entity.client.MagicProjectileModel;
import net.maxou.blamemod.entity.client.RhinoModel;
import net.maxou.blamemod.entity.custom.*;
import net.maxou.blamemod.entity.layers.ModModelLayers;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = BlameMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {

    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModModelLayers.RHINO_LAYER, RhinoModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.MAGIC_PROJECTILE_LAYER, MagicProjectileModel::createBodyLayer);
    }
    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.RHINO.get(), RhinoEntity.createAttributes().build());
        event.put(ModEntities.DRONE.get(), DroneEntity.createAttributes().build());
        event.put(ModEntities.ZOMBIE_CYBORG.get(), ZombieCyborgEntity.createAttributes().build());
        event.put(ModEntities.CUBE_BOSS.get(), CubeBossEntity.createAttributes().build());
    }



}
