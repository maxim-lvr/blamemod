package net.maxou.blamemod.item.custom;

import net.maxou.blamemod.item.ModItems;
import net.maxou.blamemod.sound.ModSounds;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DownBall extends Item {
    public DownBall(Properties pProperties) {
        super(pProperties);
    }


    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pUsedHand);
        pLevel.playSound(null, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(), ModSounds.METAL_DETECTOR_FOUND_ORE.get(), SoundSource.NEUTRAL,
                1.5F, 1F);

        if (!pLevel.isClientSide && pPlayer instanceof ServerPlayer serverPlayer) {
            CommandSourceStack source = serverPlayer.createCommandSourceStack();
            serverPlayer.getServer().getCommands().performPrefixedCommand(
                    source, "gravity set_base_direction down " + pPlayer.getName().getString()
            );
            for (int i = 0; i < 9; i++){
                //System.out.println(i + " : " + pPlayer.getSlot(i).get()  + " : " + pPlayer.getSlot(i).get().is(ModItems.DOWN_BALL.get()));
                if(pPlayer.getSlot(i).get().is(ModItems.UP_BALL.get())
                        || pPlayer.getSlot(i).get().is(ModItems.DOWN_BALL.get())
                        || pPlayer.getSlot(i).get().is(ModItems.RIGHT_BALL.get())
                        || pPlayer.getSlot(i).get().is(ModItems.LEFT_BALL.get())){
                    pPlayer.getCooldowns().addCooldown(pPlayer.getSlot(i).get().getItem(), 20);
                }
            }
            pPlayer.getItemInHand(pUsedHand).hurtAndBreak(1, pPlayer, player -> pPlayer.broadcastBreakEvent(pPlayer.getUsedItemHand()));
        }

        return InteractionResultHolder.sidedSuccess(itemstack, pLevel.isClientSide());
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("tooltip.blamemod.down_ball.tooltip"));
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    private void outputValuableCoordinates(Player player) {
        player.sendSystemMessage(Component.literal("WHOAAA"));
    }

}