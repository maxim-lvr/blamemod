package net.maxou.blamemod.item.custom;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class JumpingBall extends Item {
    public JumpingBall(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        Player player = pContext.getPlayer();
        Level level = pContext.getLevel();

        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
            CommandSourceStack source = serverPlayer.createCommandSourceStack();
            serverPlayer.getServer().getCommands().performPrefixedCommand(
                    source, "gravity set_base_direction up " + player.getName().getString()
            );

            player.addEffect(new MobEffectInstance(MobEffects.JUMP, 1000, 5));
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("tooltip.blamemod.jumping_ball.tooltip"));
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    private void outputValuableCoordinates(Player player) {
        player.sendSystemMessage(Component.literal("Right clicked"));
    }

}