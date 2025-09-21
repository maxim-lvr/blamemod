package net.maxou.blamemod.block.entity;

import net.maxou.blamemod.BlameMod;
import net.maxou.blamemod.block.ModBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, BlameMod.MOD_ID);

    public static final RegistryObject<BlockEntityType<GemPolishingStationBlockEntity>> GEM_POLISHING_BE =
            BLOCK_ENTITIES.register("gem_polishing_be", () ->
                    BlockEntityType.Builder.of(GemPolishingStationBlockEntity::new,
                            ModBlocks.GEM_POLISHING_STATION.get()).build(null));

    public static final RegistryObject<BlockEntityType<GemMixingStationBlockEntity>> GEM_MIXING_BE =
            BLOCK_ENTITIES.register("gem_mixing_be", () ->
                    BlockEntityType.Builder.of(GemMixingStationBlockEntity::new,
                            ModBlocks.GEM_MIXING_STATION.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
