package net.maxou.blamemod.entity;

import net.maxou.blamemod.BlameMod;
import net.maxou.blamemod.entity.custom.CubeBossEntity;
import net.maxou.blamemod.entity.custom.DroneEntity;
import net.maxou.blamemod.entity.custom.RhinoEntity;
import net.maxou.blamemod.entity.custom.ZombieCyborgEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import javax.swing.text.html.parser.Entity;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPE =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, BlameMod.MOD_ID);

    public static final RegistryObject<EntityType<RhinoEntity>> RHINO =
            ENTITY_TYPE.register("rhino", () -> EntityType.Builder.of(RhinoEntity::new, MobCategory.CREATURE)
                    .sized(2.5f, 2.5f).build("rhino"));

    public static final RegistryObject<EntityType<DroneEntity>> DRONE =
            ENTITY_TYPE.register("drone", () -> EntityType.Builder.of(DroneEntity::new, MobCategory.MONSTER)
                    .sized(0.5f, 0.5f).build("drone"));

    public static final RegistryObject<EntityType<ZombieCyborgEntity>> ZOMBIE_CYBORG =
            ENTITY_TYPE.register("zombie_cyborg", () -> EntityType.Builder.of(ZombieCyborgEntity::new, MobCategory.MONSTER)
                    .sized(1f, 2f).build("zombie_cyborg"));

    public static final RegistryObject<EntityType<CubeBossEntity>> CUBE_BOSS =
            ENTITY_TYPE.register("cube_boss", () -> EntityType.Builder.of(CubeBossEntity::new, MobCategory.MONSTER)
                    .sized(1f, 2f).build("zombie_cyborg"));


    public static void register(IEventBus eventBus) {
        ENTITY_TYPE.register(eventBus);
    }
}
