package net.maxou.blamemod;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.*;

//@Mod.EventBusSubscriber(modid = BlameMod.MOD_ID)
public class saveStructure {

    // Réglages généraux
    private static final int TILE_CHUNKS = 2;         // chaque immeuble tient dans 2x2 chunks (32x32)
    private static final int CHUNK_SIZE  = 16;
    private static final int BUILDING_Y  = 112;
    private static final float SPAWN_CHANCE = 1.0f;     // densité max par tuile
    private static final List<Integer> ROOF_LEVELS = List.of(-60, -7, 46, 109, 250);

    // États
    private static final Set<ChunkPos> roofPlacedChunks = new HashSet<>();
    private static final Set<ChunkPos> buildingReservedChunks = new HashSet<>();

    // Définition compacte des types (facile d’en ajouter d’autres)
    private record BuildingType(String base, String mid, String top, int w, int l, int hBase, int hMid, int hTop) {}
    private static final List<BuildingType> TYPES = List.of(
            new BuildingType("batiment1_base","batiment1_mid","batiment1_top",27,28,6,5,7),
            new BuildingType("batiment1_base","batiment1_mid","batiment1_top",27,28,6,5,7)
            //           new BuildingType("batiment2_base","batiment2_mid","batiment2_top",30,30,11,11,12)
    );

    private static final java.util.Set<net.minecraft.world.level.block.Block> SKIP_TEMPLATE_BLOCKS =
            java.util.Set.of(
                    net.minecraft.world.level.block.Blocks.WHITE_BED,
                    net.minecraft.world.level.block.Blocks.ORANGE_BED,
                    net.minecraft.world.level.block.Blocks.MAGENTA_BED,
                    net.minecraft.world.level.block.Blocks.LIGHT_BLUE_BED,
                    net.minecraft.world.level.block.Blocks.YELLOW_BED,
                    net.minecraft.world.level.block.Blocks.LIME_BED,
                    net.minecraft.world.level.block.Blocks.PINK_BED,
                    net.minecraft.world.level.block.Blocks.GRAY_BED,
                    net.minecraft.world.level.block.Blocks.LIGHT_GRAY_BED,
                    net.minecraft.world.level.block.Blocks.CYAN_BED,
                    net.minecraft.world.level.block.Blocks.PURPLE_BED,
                    net.minecraft.world.level.block.Blocks.BLUE_BED,
                    net.minecraft.world.level.block.Blocks.BROWN_BED,
                    net.minecraft.world.level.block.Blocks.GREEN_BED,
                    net.minecraft.world.level.block.Blocks.RED_BED,
                    net.minecraft.world.level.block.Blocks.BLACK_BED,
                    Blocks.TORCH,
                    Blocks.SEA_LANTERN,
                    Blocks.DRAGON_HEAD,
                    Blocks.DAYLIGHT_DETECTOR,
                    Blocks.REDSTONE_LAMP,
                    Blocks.ANVIL,
                    Blocks.CHIPPED_ANVIL,
                    Blocks.DAMAGED_ANVIL
            );

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        for (ServerLevel level : event.getServer().getAllLevels()) {
            if (!level.dimension().location().getPath().equals("overworld")) continue;

            for (ServerPlayer p : level.players()) {
                ChunkPos center = new ChunkPos(p.blockPosition());
                for (int dx = -8; dx <= 8; dx++) for (int dz = -8; dz <= 8; dz++) {
                    ChunkPos c = new ChunkPos(center.x + dx, center.z + dz);
                    StructureTemplateManager mgr = level.getStructureManager();
                    BlockPos chunkBase = c.getWorldPosition();

                    tryPlaceBuilding(c, chunkBase, level, mgr);

                    if (roofPlacedChunks.add(c)) {
                        for (int y : ROOF_LEVELS) place(mgr, level, chunkBase.offset(0, y, 0), "roof_1");
                    }
                }
            }
        }
    }

    private static void tryPlaceBuilding(ChunkPos chunkPos, BlockPos chunkBase, ServerLevel level, StructureTemplateManager mgr) {
        // ancrage sur grille 2x2
        if (mod(chunkPos.x, TILE_CHUNKS) != 0 || mod(chunkPos.z, TILE_CHUNKS) != 0) return;

        // RNG déterministe
        long seed = ((long)chunkPos.x * 341873128712L) ^ ((long)chunkPos.z * 132897987541L) ^ 0x9E3779B97F4A7C15L;
        Random rand = new Random(seed);
        if (rand.nextFloat() > SPAWN_CHANCE) return;

        // zone libre ?
        if (!isAreaFree(chunkPos)) return;

        // type au hasard (déterministe)
        BuildingType t = TYPES.get(rand.nextInt(TYPES.size()));

        // offsets "collés" dans la tuile 32x32
        int maxOffX = CHUNK_SIZE * TILE_CHUNKS - t.w; if (maxOffX < 0) maxOffX = 0;
        int maxOffZ = CHUNK_SIZE * TILE_CHUNKS - t.l; if (maxOffZ < 0) maxOffZ = 0;
        int tileX = Math.floorDiv(chunkPos.x, TILE_CHUNKS), tileZ = Math.floorDiv(chunkPos.z, TILE_CHUNKS);
        int offX = (tileX & 1) == 0 ? 0 : maxOffX;
        int offZ = (tileZ & 1) == 0 ? 0 : maxOffZ;

        BlockPos basePos = chunkBase.offset(offX, BUILDING_Y, offZ);

        // réserver 2x2
        reserve2x2(chunkPos);

        // placer base -> N mids -> top
        int heigh_build = 0;
        place(mgr, level, basePos, t.base);
        int currentY = basePos.getY() + t.hBase;
        if(t.base == "batiment2_base"){
            heigh_build = 9;
        }else {
            heigh_build = 22;
        }
        int midCount = rand.nextInt(heigh_build) + 3; // 3..10 (simple, propre)
        for (int i = 0; i < midCount; i++) {
            place(mgr, level, new BlockPos(basePos.getX(), currentY, basePos.getZ()), t.mid);
            currentY += t.hMid;
        }
        place(mgr, level, new BlockPos(basePos.getX(), currentY, basePos.getZ()), t.top);
    }

    private static void reserve2x2(ChunkPos origin) {
        for (int cx = 0; cx < TILE_CHUNKS; cx++)
            for (int cz = 0; cz < TILE_CHUNKS; cz++)
                buildingReservedChunks.add(new ChunkPos(origin.x + cx, origin.z + cz));
    }

    private static boolean isAreaFree(ChunkPos origin) {
        for (int cx = 0; cx < TILE_CHUNKS; cx++)
            for (int cz = 0; cz < TILE_CHUNKS; cz++)
                if (buildingReservedChunks.contains(new ChunkPos(origin.x + cx, origin.z + cz))) return false;
        return true;
    }

    private static void place(StructureTemplateManager mgr, ServerLevel level, BlockPos pos, String name) {
        StructureTemplate t = mgr.getOrCreate(new ResourceLocation("blamemod", name));
        if (t == null) {
            System.out.println("❌ Introuvable : " + name);
            return;
        }

        StructurePlaceSettings s = new StructurePlaceSettings()
                .setMirror(Mirror.NONE)
                .setRotation(Rotation.NONE)
                .setIgnoreEntities(true)
                .addProcessor(net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor.AIR)
                .addProcessor(net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor.STRUCTURE_BLOCK)
                .addProcessor(new net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor(
                        java.util.List.copyOf(SKIP_TEMPLATE_BLOCKS) // conversion Set -> List
                ));

        if (!t.placeInWorld(level, pos, pos, s, level.random, 2)) {
            System.out.println("⚠️ Échec de placement de " + name + " à " + pos);
        }
    }


    private static int mod(int a, int m) { int r = a % m; return r < 0 ? r + m : r; }
}
