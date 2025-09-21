package net.maxou.blamemod.item;

import net.maxou.blamemod.BlameMod;
import net.maxou.blamemod.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, BlameMod.MOD_ID);

    public static final RegistryObject<CreativeModeTab> BLAMEMOD_TAB = CREATIVE_MODE_TABS.register("blamemod_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.SAPPHIRE.get()))
                    .title(Component.translatable("creativetab.blamemod_tab"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModItems.SAPPHIRE.get());
                        output.accept(ModItems.RAW_SAPPHIRE.get());

                        output.accept(ModBlocks.SAPPHIRE_BLOCK.get());
                        output.accept(ModBlocks.RAW_SAPPHIRE_BLOCK.get());
                        output.accept(ModItems.METAL_DETECTOR.get());
                        output.accept(ModItems.JUMPING_BALL.get());
                        output.accept(ModBlocks.SOUND_BLOCK.get());
                        output.accept(ModItems.STRAWBERRY.get());
                        output.accept(ModItems.PINE_CONE.get());

                        output.accept(ModBlocks.SAPPHIRE_FENCE.get());
                        output.accept(ModBlocks.SAPPHIRE_DOOR.get());
                        output.accept(ModBlocks.SAPPHIRE_WALL.get());
                        output.accept(ModBlocks.SAPPHIRE_PRESSURE_PLATE.get());
                        output.accept(ModBlocks.SAPPHIRE_TRAPDOOR.get());
                        output.accept(ModBlocks.SAPPHIRE_BUTTON.get());
                        output.accept(ModBlocks.SAPPHIRE_FENCE_GATE.get());
                        output.accept(ModBlocks.SAPPHIRE_STAIRS.get());
                        output.accept(ModBlocks.SAPPHIRE_SLAB.get());
                        output.accept(ModItems.KEY_CARD.get());

                        output.accept(ModItems.SAPPHIRE_SWORD.get());
                        output.accept(ModItems.SAPPHIRE_PICKAXE.get());
                        output.accept(ModItems.SAPPHIRE_AXE.get());
                        output.accept(ModItems.SAPPHIRE_SHOVEL.get());
                        output.accept(ModItems.SAPPHIRE_HOE.get());

                        output.accept(ModItems.SAPPHIRE_HELMET.get());
                        output.accept(ModItems.SAPPHIRE_CHESTPLATE.get());
                        output.accept(ModItems.SAPPHIRE_LEGGINGS.get());
                        output.accept(ModItems.SAPPHIRE_BOOTS.get());

                        output.accept(ModItems.STRAWBERRY_SEEDS.get());
                        output.accept(ModItems.CORN_SEEDS.get());
                        output.accept(ModItems.CORN.get());
                        output.accept(ModBlocks.CATMINT.get());

                        output.accept(ModItems.BAR_BRAWL_DISC_MUSIC.get());

                        output.accept(ModItems.RHINO_SPAWN_EGG.get());
                        output.accept(ModItems.DRONE_SPAWN_EGG.get());

                        output.accept(ModBlocks.FAKE_STONE.get());

                        output.accept(ModBlocks.GEM_POLISHING_STATION.get());
                        output.accept(ModBlocks.GEM_MIXING_STATION.get());



                        output.accept(Items.DIAMOND_AXE);
                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
