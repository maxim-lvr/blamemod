package net.maxou.blamemod.event;

import net.maxou.blamemod.BlameMod;
import net.maxou.blamemod.entity.ModEntities;
import net.maxou.blamemod.entity.custom.CubeBossEntity;
import net.maxou.blamemod.entity.custom.DroneEntity;
import net.maxou.blamemod.entity.custom.RhinoEntity;
import net.maxou.blamemod.entity.custom.ZombieCyborgEntity;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = BlameMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.RHINO.get(), RhinoEntity.createAttributes().build());
        event.put(ModEntities.DRONE.get(), DroneEntity.createAttributes().build());
        event.put(ModEntities.ZOMBIE_CYBORG.get(), ZombieCyborgEntity.createAttributes().build());
        event.put(ModEntities.CUBE_BOSS.get(), CubeBossEntity.createAttributes().build());
    }



}
