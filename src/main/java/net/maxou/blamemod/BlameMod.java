package net.maxou.blamemod;

import com.mojang.logging.LogUtils;
import net.maxou.blamemod.block.ModBlocks;
import net.maxou.blamemod.block.entity.ModBlockEntities;
import net.maxou.blamemod.entity.ModEntities;
import net.maxou.blamemod.entity.client.CubeBossRenderer;
import net.maxou.blamemod.entity.client.DroneRenderer;
import net.maxou.blamemod.entity.client.RhinoRenderer;
import net.maxou.blamemod.entity.client.ZombieCyborgRenderer;
import net.maxou.blamemod.item.ModCreativeModTabs;
import net.maxou.blamemod.item.ModItems;
import net.maxou.blamemod.loot.ModLootModifiers;
import net.maxou.blamemod.recipe.ModRecipes;
import net.maxou.blamemod.screen.GemMixingStationScreen;
import net.maxou.blamemod.screen.GemPolishingStationScreen;
import net.maxou.blamemod.screen.ModMenuTypes;
import net.maxou.blamemod.sound.ModSounds;
import net.maxou.blamemod.villager.ModVillagers;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(BlameMod.MOD_ID)
public class BlameMod {
    public static final String MOD_ID = "blamemod";
    private static final Logger LOGGER = LogUtils.getLogger();
    public BlameMod(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();

        ModCreativeModTabs.register(modEventBus);

        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModLootModifiers.register(modEventBus);
        ModVillagers.register(modEventBus);
        ModSounds.register(modEventBus);
        ModEntities.register(modEventBus);

        ModBlockEntities.register(modEventBus);
        ModMenuTypes.register(modEventBus);
        ModRecipes.register(modEventBus);

        modEventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::addCreative);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(ModBlocks.CATMINT.getId(), ModBlocks.POTTED_CATMINT);
        });
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if(event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            event.accept(ModItems.SAPPHIRE);
            event.accept(ModItems.RAW_SAPPHIRE);
        }
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        System.out.println("Serveur lancé – BlameMod actif !");
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            EntityRenderers.register(ModEntities.RHINO.get(), RhinoRenderer::new);
            EntityRenderers.register(ModEntities.DRONE.get(), DroneRenderer::new);
            EntityRenderers.register(ModEntities.ZOMBIE_CYBORG.get(), ZombieCyborgRenderer::new);
            EntityRenderers.register(ModEntities.CUBE_BOSS.get(), CubeBossRenderer::new);

            MenuScreens.register(ModMenuTypes.GEM_POLISHING_MENU.get(), GemPolishingStationScreen::new);
            MenuScreens.register(ModMenuTypes.GEM_MIXING_MENU.get(), GemMixingStationScreen::new);
        }
    }
}