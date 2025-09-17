package net.maxou.blamemod;

import com.google.gson.*;
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

import java.nio.file.*;
import java.util.*;

@Mod.EventBusSubscriber(modid = BlameMod.MOD_ID)
public class QueuedStructureGenerator {

    // Réglages généraux
    private static final int TILE_CHUNKS = 2;
    private static final int CHUNK_SIZE  = 16;
    private static final int BUILDING_Y  = 112;
    private static final float SPAWN_CHANCE = 1.0f;
    private static final List<Integer> ROOF_LEVELS = List.of(-60, -7, 46, 109, 250);

    // États
    private static final Set<ChunkPos> roofPlacedChunks = new HashSet<>();
    private static final Set<ChunkPos> buildingReservedChunks = new HashSet<>();
    private static final Set<ChunkPos> generatedChunks = new HashSet<>();
    private static final Set<ChunkPos> occupiedStructureChunks = new HashSet<>();

    // JSON forcé
    private record JsonStructureInfo(String structure, int direction) {}
    private static final Map<ChunkPos, JsonStructureInfo> FORCED_STRUCTURES = new HashMap<>();
    private static boolean jsonLoaded = false;

    // Définition compacte des types
    private record BuildingType(String base, String mid, String top, int w, int l, int hBase, int hMid, int hTop) {}
    private static final List<BuildingType> TYPES = List.of(
            new BuildingType("batiment1_base","batiment1_mid","batiment1_top",27,28,6,5,7),
            new BuildingType("batiment1_base","batiment1_mid","batiment1_top",27,28,6,5,7)
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

        if (!jsonLoaded) {
            loadJson();
            jsonLoaded = true;
        }

        for (ServerLevel level : event.getServer().getAllLevels()) {
            if (!level.dimension().location().getPath().equals("overworld")) continue;

            for (ServerPlayer p : level.players()) {
                ChunkPos center = new ChunkPos(p.blockPosition());
                for (int dx = -8; dx <= 8; dx++) for (int dz = -8; dz <= 8; dz++) {
                    ChunkPos c = new ChunkPos(center.x + dx, center.z + dz);
                    if (generatedChunks.contains(c)) continue; // déjà généré

                    StructureTemplateManager mgr = level.getStructureManager();
                    BlockPos chunkBase = c.getWorldPosition();

                    // JSON forcé
                    JsonStructureInfo forced = FORCED_STRUCTURES.get(c);
                    if (forced != null) {
                        if (occupiedStructureChunks.contains(c)) {
                            System.out.println("⚠️ Chunk déjà occupé, on saute: " + c);
                            generatedChunks.add(c);
                            continue;
                        }

                        Rotation rot = dirToRotation(forced.direction());
                        BlockPos chunkNW = chunkBase.offset(0, BUILDING_Y, 0);

                        // Ajustement spécifique si rotation = 1 (90°)
                        BlockPos placePos = chunkNW;
                        if (rot == Rotation.CLOCKWISE_90) {
                            placePos = placePos.east(15);
                        }
                        if (rot == Rotation.CLOCKWISE_180) {
                            placePos = placePos.east(15);
                            placePos = placePos.south(15);
                        }
                        if (rot == Rotation.COUNTERCLOCKWISE_90) {
                            placePos = placePos.south(15);
                        }

                        place(mgr, level, placePos, forced.structure(), rot);

                        occupiedStructureChunks.add(c);
                        generatedChunks.add(c);
                        continue;
                    }

                    // Génération aléatoire
                    //tryPlaceBuilding(c, chunkBase, level, mgr);
                    //generatedChunks.add(c);

                    // Toits
                    if (roofPlacedChunks.add(c)) {
                        for (int y : ROOF_LEVELS) place(mgr, level, chunkBase.offset(0, y, 0), "roof_1");
                    }
                }
            }
        }
    }

    // Conversion direction JSON -> Rotation
    private static Rotation dirToRotation(int dir) {
        return switch (dir & 3) {
            case 0 -> Rotation.NONE;
            case 1 -> Rotation.CLOCKWISE_90;
            case 2 -> Rotation.CLOCKWISE_180;
            case 3 -> Rotation.COUNTERCLOCKWISE_90;
            default -> Rotation.NONE;
        };
    }

    // Applique une rotation à l’offset puis le soustrait à la base
    private static BlockPos applyRotatedMinusOffset(BlockPos base, Rotation rot, int offX, int offZ) {
        int rx = offX, rz = offZ;
        switch (rot) {
            case NONE -> { /* pas de changement */ }
            case CLOCKWISE_90 -> { int tx = rx; rx = -rz; rz = tx; }
            case CLOCKWISE_180 -> { rx = -rx; rz = -rz; }
            case COUNTERCLOCKWISE_90 -> { int tx = rx; rx = rz; rz = -tx; }
        }
        return base.offset(-rx, 0, -rz);
    }

    private static void tryPlaceBuilding(ChunkPos chunkPos, BlockPos chunkBase, ServerLevel level, StructureTemplateManager mgr) {
        if (mod(chunkPos.x, TILE_CHUNKS) != 0 || mod(chunkPos.z, TILE_CHUNKS) != 0) return;

        long seed = ((long)chunkPos.x * 341873128712L) ^ ((long)chunkPos.z * 132897987541L) ^ 0x9E3779B97F4A7C15L;
        Random rand = new Random(seed);
        if (rand.nextFloat() > SPAWN_CHANCE) return;

        if (!isAreaFree(chunkPos)) return;

        BuildingType t = TYPES.get(rand.nextInt(TYPES.size()));

        int maxOffX = CHUNK_SIZE * TILE_CHUNKS - t.w; if (maxOffX < 0) maxOffX = 0;
        int maxOffZ = CHUNK_SIZE * TILE_CHUNKS - t.l; if (maxOffZ < 0) maxOffZ = 0;
        int tileX = Math.floorDiv(chunkPos.x, TILE_CHUNKS), tileZ = Math.floorDiv(chunkPos.z, TILE_CHUNKS);
        int offX = (tileX & 1) == 0 ? 0 : maxOffX;
        int offZ = (tileZ & 1) == 0 ? 0 : maxOffZ;

        BlockPos basePos = chunkBase.offset(offX, BUILDING_Y, offZ);

        reserve2x2(chunkPos);

        int heigh_build;
        place(mgr, level, basePos, t.base);
        int currentY = basePos.getY() + t.hBase;
        if(t.base.equals("batiment2_base")){
            heigh_build = 9;
        } else {
            heigh_build = 22;
        }
        int midCount = rand.nextInt(heigh_build) + 3;
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
        place(mgr, level, pos, name, Rotation.NONE);
    }

    private static void place(StructureTemplateManager mgr, ServerLevel level, BlockPos pos, String name, Rotation rotation) {
        StructureTemplate t = mgr.getOrCreate(new ResourceLocation("blamemod", name));
        if (t == null) {
            System.out.println("❌ Introuvable : " + name);
            return;
        }

        StructurePlaceSettings s = new StructurePlaceSettings()
                .setRotation(rotation)
                .setMirror(Mirror.NONE)
                .setIgnoreEntities(true)
                .addProcessor(net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor.AIR)
                .addProcessor(net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor.STRUCTURE_BLOCK)
                .addProcessor(new net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor(
                        java.util.List.copyOf(SKIP_TEMPLATE_BLOCKS)
                ));

        if (!t.placeInWorld(level, pos, pos, s, level.random, 2)) {
            System.out.println("⚠️ Échec de placement de " + name + " à " + pos + " rot=" + rotation);
        }
    }

    private static void loadJson() {
        try {
            Path file = Path.of("config/blamemod_structures.json");
            if (!Files.exists(file)) return;

            String raw = Files.readString(file);
            JsonArray chunks = JsonParser.parseString(raw).getAsJsonArray();

            for (JsonElement e : chunks) {
                JsonObject obj = e.getAsJsonObject();

                String chunkStr = obj.get("chunk").getAsString();
                chunkStr = chunkStr.replace("(", "").replace(")", "").replace(" ", "");
                String[] parts = chunkStr.split(",");
                int cx = Integer.parseInt(parts[0]);
                int cz = Integer.parseInt(parts[1]);

                String struct = obj.get("structure").getAsString().replace("blamemod:", "");
                int dir = obj.get("direction").getAsInt();

                FORCED_STRUCTURES.put(new ChunkPos(cx, cz), new JsonStructureInfo(struct, dir));
            }

            System.out.println("✅ Structures JSON chargées : " + FORCED_STRUCTURES.size());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static int mod(int a, int m) { int r = a % m; return r < 0 ? r + m : r; }
}
