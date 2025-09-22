package net.maxou.blamemod.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class DroneEntity extends Monster implements RangedAttackMob {

    private BlockPos anchorPoint;
    private int laserCooldown = 0;


    public DroneEntity(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.anchorPoint = this.blockPosition();
        this.moveControl = new FlyingMoveControl(this, 20, true);
    }

    public final AnimationState idleAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;

    @Override
    protected PathNavigation createNavigation(Level pLevel) {
        return new FlyingPathNavigation(this, pLevel);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        this.setNoGravity(true);

        // Y actuel
        double groundY = this.level().getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, this.blockPosition()).getY();
        double minHeight = groundY + 2;
        double maxHeight = groundY + 5;

        if (this.getY() < minHeight) {
            this.setDeltaMovement(this.getDeltaMovement().add(0, 0.1D, 0)); // remonte
        } else if (this.getY() > maxHeight) {
            this.setDeltaMovement(this.getDeltaMovement().add(0, -0.1D, 0)); // descend
        }

        // --- gestion du rayon laser ---
        if (!this.level().isClientSide) {
            if (laserCooldown > 0) {
                laserCooldown--;
            } else {
                LivingEntity target = this.getTarget();
                if (target != null && this.hasLineOfSight(target)) {
                    target.hurt(new DamageSource(this.level().registryAccess()
                            .registryOrThrow(Registries.DAMAGE_TYPE)
                            .getHolderOrThrow(DamageTypes.INDIRECT_MAGIC),
                            this, this), 0.001F);
                    laserCooldown = 60; // toutes les 3 secondes (20 ticks * 3)
                }
            }
        }
    }

    private void shootFireballAt(LivingEntity target) {
        Vec3 look = this.getLookAngle();
        double dx = target.getX() - this.getX();
        double dy = target.getY(0.5D) - this.getY(0.5D);
        double dz = target.getZ() - this.getZ();

        SmallFireball fireball = new SmallFireball(
                this.level(),
                this,
                dx,
                dy,
                dz
        );
        fireball.setPos(this.getX(), this.getY() + 1.0D, this.getZ()); // spawn devant le drone
        this.level().addFreshEntity(fireball);
    }

    @Override
    public void tick() {
        super.tick();

        if(this.level().isClientSide()) {
            setupAnimationStates();
        }
    }

    private void setupAnimationStates() {
        if(this.idleAnimationTimeout <= 0) {
            this.idleAnimationTimeout = this.random.nextInt(40) + 80;
            this.idleAnimationState.start(this.tickCount);
        } else {
            --this.idleAnimationTimeout;
        }
    }

    @Override
    protected void updateWalkAnimation(float pPartialTick) {
        float f;
        if(this.getPose() == Pose.STANDING) {
            f = Math.min(pPartialTick * 6f, 1f);
        }else {
            f = 0f;
        }

        this.walkAnimation.update(f, 0.2f);
    }

    @Override
    protected void registerGoals() {

        this.goalSelector.addGoal(2, new RangedAttackGoal(this, 1.0D, 40, 16.0F));
        this.goalSelector.addGoal(5, new MoveTowardsRestrictionGoal(this, 1.0D));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0D, 0.0F));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(9, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setAlertOthers());
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 10)
                .add(Attributes.FOLLOW_RANGE, 240)
                .add(Attributes.MOVEMENT_SPEED, 0.250)
                .add(Attributes.ARMOR_TOUGHNESS, 0.2f)
                .add(Attributes.ATTACK_KNOCKBACK, 0.5f)
                .add(Attributes.ATTACK_DAMAGE, 2f)
                .add(Attributes.FLYING_SPEED, 2f);
    }

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return SoundEvents.HOGLIN_AMBIENT;
    }

    @Override
    protected @Nullable SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundEvents.RAVAGER_HURT;
    }

    @Override
    protected @Nullable SoundEvent getDeathSound() {
        return SoundEvents.DOLPHIN_DEATH;
    }

    @Override
    public void performRangedAttack(LivingEntity pTarget, float pVelocity) {
        // version simple type Blaze : petite boule de feu
        double dx = pTarget.getX() - this.getX();
        double dy = pTarget.getY(0.5D) - this.getY(0.5D);
        double dz = pTarget.getZ() - this.getZ();

        SmallFireball fireball = new SmallFireball(this.level(), this, dx, dy, dz);
        fireball.setPos(this.getX(), this.getY() + 1.0D, this.getZ());
        this.level().addFreshEntity(fireball);
    }
}
