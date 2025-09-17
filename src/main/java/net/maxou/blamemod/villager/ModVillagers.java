package net.maxou.blamemod.villager;

import com.google.common.collect.ImmutableSet;
import com.google.errorprone.annotations.Immutable;
import net.maxou.blamemod.BlameMod;
import net.maxou.blamemod.block.ModBlocks;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModVillagers {
    public static final DeferredRegister<PoiType> POI_TYPES =
            DeferredRegister.create(ForgeRegistries.POI_TYPES, BlameMod.MOD_ID);

    public static final DeferredRegister<VillagerProfession> VILLAGERS_PROFESSIONS =
            DeferredRegister.create(ForgeRegistries.VILLAGER_PROFESSIONS, BlameMod.MOD_ID);

    public static final RegistryObject<PoiType> SOUND_POI = POI_TYPES.register("sound_poi",
            () -> new PoiType(ImmutableSet.copyOf(ModBlocks.SOUND_BLOCK.get().getStateDefinition().getPossibleStates()),
            1, 1));

    public static final RegistryObject<VillagerProfession> SOUND_MASTER =
            VILLAGERS_PROFESSIONS.register("soundmaster", () -> new VillagerProfession("soundmaster",
    holder -> holder.get() == SOUND_POI.get(), holder -> holder.get() == SOUND_POI.get(),
                    ImmutableSet.of(), ImmutableSet.of(), SoundEvents.VILLAGER_WORK_ARMORER));


    public static void register(IEventBus eventBus) {
        POI_TYPES.register(eventBus);
        VILLAGERS_PROFESSIONS.register(eventBus);
    }

}
