package net.oliverbravery.coda.features;

import net.minecraft.block.BlockState;
import net.minecraft.block.LecternBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.oliverbravery.coda.Coda;
import net.oliverbravery.coda.utilities.Utils;

public class LibrarianBookTrade {
    public static Enchantment wantedEnchant = null;
    public static Integer maxEmeraldPrice = 25;
    public static boolean enabled = false;
    public static String state = "";
    public static Screen priorScreen = null;
    private static BlockPos lecturnPos = null;
    private static double minVillagerDistance = 5;
    private  static double minLecternDistance = 1;
    private static VillagerEntity villagerToTradeWith = null;
    private static Integer attemptCount = 0;
    private static Integer stateOnSameTick = 0;
    private static String currentState = null;
    private static Integer maxSameTickAmount = 60;

    public static void tick(MinecraftClient client) {
        if(client.player != null && client.interactionManager != null && enabled) {
            StateCheck();
            if(FindClosestVillagerEntity(client) != null && (FindLectern(client) != null ||
                    (state == "DESTROYING_LECTERN" || state == "PLACE_LECTERN"))) {
                if (state == "START") {
                    SetVillagerToTradeWith(client);
                    lecturnPos = FindLectern(client);
                    state = "SEARCH_TRADE";
                } else if (state == "DESTROYING_LECTERN") {
                    BlockPos lPos = FindLectern(client);
                    if (lPos != null) {
                        state = "DESTROYING_LECTERN";
                        DestroyLectern(client, lecturnPos);
                    } else {
                        state = "PLACE_LECTERN";
                    }
                } else if (state == "PLACE_LECTERN") {
                    PlaceLectern(client, lecturnPos);
                } else if (state == "SEARCH_TRADE") {
                    VillagerEntity e = FindClosestVillagerEntity(client);
                    if (e == null) {
                        ErrorLocated();
                    } else {
                        CheckTrades(client, e);
                    }
                } else if (state == "GOOD_TRADE") {
                    UpdateStatus(false);
                    state = "COMPLETE";
                    enabled = false;
                    CorrectVillagerLocated();
                } else if (state == "BAD_TRADE") {
                    //refresh
                    UpdateStatus(true);
                    state = "DESTROYING_LECTERN";
                }
            }
            else {
                ErrorLocated();
            }
        }
    }

    public static void StateCheck() {
        if(state == currentState) {
            stateOnSameTick++;
        }
        else if(currentState == null) {
            currentState = state;
            stateOnSameTick++;
        }
        else if(state != currentState) {
            stateOnSameTick = 0;
            currentState = state;
        }
        if(stateOnSameTick >= maxSameTickAmount) {
            ErrorLocated();
        }
    }

    public static void Run(Enchantment e, Integer price) {
        wantedEnchant = e;
        maxEmeraldPrice = price;
        lecturnPos = null;
        villagerToTradeWith = null;
        attemptCount = 0;
        stateOnSameTick = 0;
        currentState = null;
        state = "START";
        enabled = !enabled;
    }

    private static void CorrectVillagerLocated() {
        MinecraftClient client = MinecraftClient.getInstance();
        client.send(() -> client.setScreen(null));
        enabled = false;
        state = "";
        Utils.SendChatMessage("Correct villager has been found");
    }

    private static void SetVillagerToTradeWith(MinecraftClient client) {
        VillagerEntity e = FindClosestVillagerEntity(client);
        villagerToTradeWith = e;
    }

    private static void ErrorLocated() {
        enabled = false;
        state = "";
        lecturnPos = null;
        MinecraftClient client = MinecraftClient.getInstance();
        client.send(() -> client.setScreen(null));
        Utils.SendChatMessage("Error has occurred. Please make sure you are close to a lectern and " +
                "villager and holding lectern(s) in off-hand.");
    }

    private  static void UpdateStatus(boolean mode){
        if(mode) {
            attemptCount++;
            Utils.SendActionBarMessage(String.format("Searching for book - attempts so far: %s", attemptCount));
        }
        else{
            Utils.SendActionBarMessage(String.format("found book in %s attempts", attemptCount));
        }
    }

    private static VillagerEntity FindClosestVillagerEntity(MinecraftClient client) {
        double closestDist = minVillagerDistance;
        Vec3d playerPos = client.player.getPos();
        VillagerEntity closestEntity = null;
        for(Entity entity:client.world.getEntities()) {
            Vec3d ePos = entity.getPos();
            if (entity instanceof VillagerEntity) {
                if(villagerToTradeWith == null && ePos.distanceTo(playerPos) < closestDist) {
                    closestEntity = (VillagerEntity) entity;
                    closestDist = ePos.distanceTo(playerPos);
                }
                else if(ePos.distanceTo(playerPos) < minVillagerDistance && entity == villagerToTradeWith){
                    closestEntity = (VillagerEntity) entity;
                    closestDist = -1;
                }
            }
        }
        return closestEntity;
    }

    private static void CheckTrades(MinecraftClient client, VillagerEntity closestEntity) {
        priorScreen = client.currentScreen;
        client.interactionManager.interactEntity(client.player, closestEntity, Hand.MAIN_HAND);
    }

    private static BlockPos FindLectern(MinecraftClient client) {
        BlockPos lecternPos = null;
        double closestLectern = minLecternDistance;
        for (int y = -1; y <= 0; y++) {
            for (int x = -4; x <= 4; x++) {
                for (int z = -4; z <= 4; z++) {
                    BlockPos pos = client.player.getBlockPos().add(x,y,z);
                    BlockState blockState = client.world.getBlockState(pos);
                    if(blockState.getBlock() instanceof LecternBlock ) {
                        Vec3d lPos = new Vec3d(pos.getX(),pos.getY(),pos.getZ());
                        if(lPos.distanceTo(client.player.getPos()) < closestLectern) {
                            lecternPos = pos;
                            closestLectern = lPos.distanceTo(client.player.getPos());
                        }
                        else if(pos.isWithinDistance(client.player.getPos(),minLecternDistance + 1)) {
                            if((lPos.distanceTo(client.player.getPos()) - 1) < closestLectern) {
                                lecternPos = pos;
                                closestLectern = lPos.distanceTo(client.player.getPos());
                            }
                        }
                    }
                }
            }
        }
        return lecternPos;
    }

    private static void DestroyLectern(MinecraftClient client, BlockPos lecternPos) {
        client.player.swingHand(Hand.MAIN_HAND, true);
        client.interactionManager.updateBlockBreakingProgress(lecternPos,Direction.UP);
        client.player.networkHandler
                .sendPacket(new HandSwingC2SPacket(Hand.MAIN_HAND));
    }

    private static void PlaceLectern(MinecraftClient client, BlockPos lecternPos) {
        BlockHitResult hit = new BlockHitResult(new Vec3d(lecternPos.getX(), lecternPos.getY(),
                lecternPos.getZ()), Direction.UP, lecternPos, false);
        client.interactionManager.interactBlock(client.player,Hand.OFF_HAND, hit);
        state = "SEARCH_TRADE";
    }
}

