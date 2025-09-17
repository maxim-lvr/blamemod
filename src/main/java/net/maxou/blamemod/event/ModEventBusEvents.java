package net.maxou.blamemod.event;

import net.maxou.blamemod.BlameMod;
import net.maxou.blamemod.entity.ModEntities;
import net.maxou.blamemod.entity.custom.DroneEntity;
import net.maxou.blamemod.entity.custom.RhinoEntity;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = BlameMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.RHINO.get(), RhinoEntity.createAttributes().build());
        event.put(ModEntities.DRONE.get(), DroneEntity.createAttributes().build());
    }



}
