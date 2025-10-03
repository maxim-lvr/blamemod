package net.maxou.blamemod.recipe;

import net.maxou.blamemod.BlameMod;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, BlameMod.MOD_ID);


    public static final RegistryObject<RecipeSerializer<GemPolishingRecipe>> GEM_POLISHING_SERIALIZER =
            SERIALIZERS.register("gem_polishing", () -> GemPolishingRecipe.Serializer.INSTANCE);

    public static final RegistryObject<RecipeSerializer<GemMixingRecipe>> GEM_MIXING_SERIALIZER =
            SERIALIZERS.register("gem_mixing", () -> GemMixingRecipe.Serializer.INSTANCE);

    public static final RegistryObject<RecipeSerializer<CompactingMachineRecipe>> COMPACTING_MACHINE_SERIALIZER =
            SERIALIZERS.register("compacting_machine", () -> CompactingMachineRecipe.Serializer.INSTANCE);


    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);
    }
}
