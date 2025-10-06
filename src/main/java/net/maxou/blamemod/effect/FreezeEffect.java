package net.maxou.blamemod.effect;

import net.minecraft.network.protocol.game.ClientboundMoveEntityPacket;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FreezeEffect extends MobEffect{

    private static final Map<UUID, Vec3> freezeBodyPositions = new HashMap<>();
    private static final Map<UUID, float[]> freezeHeadPositions = new HashMap<>();
    private boolean isActive = false;
    public FreezeEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        UUID idBody = pLivingEntity.getUUID();
        UUID idHead = pLivingEntity.getUUID();
        freezeBodyPositions.putIfAbsent(idBody, pLivingEntity.position());
        freezeHeadPositions.putIfAbsent(idHead, new float[]{pLivingEntity.getXRot(), pLivingEntity.getYRot()});

        float[] rot = freezeHeadPositions.get(idHead);
        pLivingEntity.setXRot(rot[0]);
        pLivingEntity.setYRot(rot[1]);
        pLivingEntity.setYHeadRot(rot[1]);
        pLivingEntity.setYBodyRot(rot[1]);
        Vec3 freezePos = freezeBodyPositions.get(idBody);
        pLivingEntity.teleportTo(freezePos.x, freezePos.y, freezePos.z);
        pLivingEntity.setDeltaMovement(Vec3.ZERO);
        pLivingEntity.hasImpulse = false;
    }

    @Override
    public void removeAttributeModifiers(LivingEntity pLivingEntity, AttributeMap pAttributeMap, int pAmplifier) {
        freezeBodyPositions.remove(pLivingEntity.getUUID());
        freezeHeadPositions.remove(pLivingEntity.getUUID());
        super.removeAttributeModifiers(pLivingEntity, pAttributeMap, pAmplifier);
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }
}
