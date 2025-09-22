package net.maxou.blamemod.compat;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.maxou.blamemod.BlameMod;
import net.maxou.blamemod.recipe.GemPolishingRecipe;
import net.maxou.blamemod.screen.GemMixingStationScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeManager;

import java.util.List;

@JeiPlugin
public class JEIBlameModPlugin implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(BlameMod.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new GemPolishingCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager reciperManager = Minecraft.getInstance().level.getRecipeManager();
        List<GemPolishingRecipe> polishingRecipes = reciperManager.getAllRecipesFor(GemPolishingRecipe.Type.INSTANCE);
        registration.addRecipes(GemPolishingCategory.GEM_POLISHING_TYPE, polishingRecipes);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(GemMixingStationScreen.class,60, 30, 20, 30,
                GemPolishingCategory.GEM_POLISHING_TYPE);
    }
}
