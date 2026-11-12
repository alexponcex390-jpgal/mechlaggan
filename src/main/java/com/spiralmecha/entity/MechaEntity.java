package com.spiralmecha.entity;

import com.spiralmecha.SpiralMecha;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.core.particles.ParticleTypes;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;

public class MechaEntity extends PathfinderMob implements GeoEntity {
    private static final EntityDataAccessor<Boolean> GURREN = SynchedEntityData.defineId(MechaEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Float> ENERGY = SynchedEntityData.defineId(MechaEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<String> ACTION = SynchedEntityData.defineId(MechaEntity.class, EntityDataSerializers.STRING);
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private float inputForward, inputStrafe;
    private boolean inputJump, inputSprint;
    private int abilityCooldown;
    private int actionTicks;

    public MechaEntity(EntityType<? extends PathfinderMob> type, Level level) {
        super(type, level);
        this.setPersistenceRequired();
        this.xpReward = 0;
    }

    public static net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder createAttributes() { return MechaAttributes.create(); }

    @Override protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(GURREN, false);
        entityData.define(ENERGY, 100f);
        entityData.define(ACTION, "idle");
    }

    @Override public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);
        if (GURREN.equals(key)) refreshDimensions();
    }

    @Override protected void registerGoals() {}

    public boolean isGurren() { return entityData.get(GURREN); }
    public float getEnergy() { return entityData.get(ENERGY); }
    public String getActionName() { return entityData.get(ACTION); }
    public void setInputs(float forward, float strafe, boolean jump, boolean sprint) {
        inputForward = Mth.clamp(forward, -1, 1);
        inputStrafe = Mth.clamp(strafe, -1, 1);
        inputJump = jump; inputSprint = sprint;
    }

    @Override public EntityDimensions getDimensions(Pose pose) {
        return isGurren() ? EntityDimensions.scalable(10.7f, 20.0f) : EntityDimensions.scalable(3.4f, 2.5f);
    }

    @Override public double getPassengersRidingOffset() {
        return isGurren() ? 18.0 : 2.12;
    }

    @Override protected void positionRider(Entity passenger, MoveFunction moveFunction) {
        if (!hasPassenger(passenger)) return;
        double seat = isGurren() ? 18.0 : 2.05;
        moveFunction.accept(passenger, getX(), getY() + seat, getZ());
    }

    @Override public boolean canAddPassenger(Entity passenger) {
        return getPassengers().isEmpty() && passenger instanceof Player;
    }

    @Override public LivingEntity getControllingPassenger() {
        return getPassengers().isEmpty() ? null : (LivingEntity)getPassengers().get(0);
    }

    @Override public void travel(Vec3 travelVector) {
        if (getControllingPassenger() instanceof Player) {
            float speed = isGurren() ? 0.34f : 0.26f;
            if (inputSprint) speed *= 1.65f;
            Vec3 move = new Vec3(inputStrafe, 0, inputForward);
            if (move.lengthSqr() > 1) move = move.normalize();
            this.setYRot(getControllingPassenger().getYRot());
            this.yRotO = this.getYRot();
            this.yBodyRot = this.getYRot();
            this.yHeadRot = this.getYRot();
            this.moveRelative(speed, move);
            Vec3 velocity = this.getDeltaMovement();
            if (inputJump && this.onGround()) velocity = new Vec3(velocity.x, isGurren() ? 0.95 : 0.62, velocity.z);
            this.setDeltaMovement(velocity.x, velocity.y, velocity.z);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().multiply(0.72, 0.98, 0.72));
            if (this.onGround() && Math.abs(inputForward) < 0.05 && Math.abs(inputStrafe) < 0.05)
                this.setDeltaMovement(0, this.getDeltaMovement().y, 0);
        } else {
            super.travel(travelVector);
        }
    }

    @Override public void tick() {
        super.tick();
        if (!level().isClientSide) {
            if (abilityCooldown > 0) abilityCooldown--;
            if (actionTicks > 0 && --actionTicks == 0) entityData.set(ACTION, "idle");
            if (getEnergy() < 100) entityData.set(ENERGY, Math.min(100, getEnergy() + (isGurren() ? 0.16f : 0.22f)));
            if (tickCount % 20 == 0 && getControllingPassenger() == null) setInputs(0,0,false,false);
        }
    }

    public boolean transform() {
        if (level().isClientSide || abilityCooldown > 0) return false;
        if (getEnergy() < (isGurren() ? 30 : 20)) return false;
        float ratio = getHealth() / Math.max(1, getMaxHealth());
        boolean next = !isGurren();
        entityData.set(ENERGY, getEnergy() - (next ? 20 : 30));
        entityData.set(GURREN, next);
        AttributeInstance hp = getAttribute(Attributes.MAX_HEALTH);
        AttributeInstance damage = getAttribute(Attributes.ATTACK_DAMAGE);
        AttributeInstance speed = getAttribute(Attributes.MOVEMENT_SPEED);
        AttributeInstance knock = getAttribute(Attributes.KNOCKBACK_RESISTANCE);
        if (hp != null) hp.setBaseValue(next ? 1000 : 200);
        if (damage != null) damage.setBaseValue(next ? 50 : 10);
        if (speed != null) speed.setBaseValue(next ? 0.34 : 0.26);
        if (knock != null) knock.setBaseValue(next ? 1.0 : 0.65);
        setHealth(Math.max(1, getMaxHealth() * ratio));
        refreshDimensions();
        entityData.set(ACTION, "transform");
        actionTicks = 60;
        abilityCooldown = 40;
        level().playSound(null, blockPosition(), SoundEvents.BEACON_POWER_SELECT, SoundSource.PLAYERS, 2.0f, next ? 0.55f : 0.9f);
        spiralBurst(ParticleTypes.END_ROD, next ? 100 : 45, next ? 2.8 : 1.4);
        return true;
    }

    public void performAction(String action) {
        if (level().isClientSide || !(getControllingPassenger() instanceof Player pilot)) return;
        if (action.equals("transform")) { transform(); return; }
        if (action.equals("dismount")) { pilot.stopRiding(); return; }
        if (abilityCooldown > 0) return;
        float cost; int cooldown; double radius; float damage; String anim;
        switch (action) {
            case "attack_light" -> { cost=2; cooldown=8; radius=3.5; damage=isGurren()?35:7; anim="attack_punch"; }
            case "attack_heavy" -> { cost=7; cooldown=18; radius=5; damage=isGurren()?75:15; anim="attack_slam"; }
            case "skill_1" -> { cost=12; cooldown=35; radius=isGurren()?9:5; damage=isGurren()?115:24; anim=isGurren()?"drill_burst":"drill_start"; }
            case "skill_2" -> { cost=18; cooldown=50; radius=isGurren()?12:6; damage=isGurren()?155:32; anim=isGurren()?"dash":"attack_spin"; }
            case "skill_3" -> { cost=24; cooldown=70; radius=isGurren()?14:7; damage=isGurren()?210:42; anim=isGurren()?"charge_long":"attack_charge"; }
            case "ultimate" -> {
                if (!isGurren()) { cost=30; cooldown=90; radius=8; damage=65; anim="attack_charge"; }
                else { cost=55; cooldown=180; radius=20; damage=300; anim="giga_drill_breaker"; }
            }
            default -> { return; }
        }
        if (getEnergy() < cost) return;
        entityData.set(ENERGY, getEnergy()-cost);
        abilityCooldown = cooldown;
        entityData.set(ACTION, anim);
        actionTicks = Math.max(12, cooldown);
        AABB area = getBoundingBox().inflate(radius, radius*0.55, radius);
        List<LivingEntity> targets = level().getEntitiesOfClass(LivingEntity.class, area,
            e -> e != this && e != pilot && e.isAlive() && !getPassengers().contains(e));
        Vec3 look = pilot.getLookAngle().normalize();
        for (LivingEntity target : targets) {
            Vec3 to = target.position().subtract(position()).normalize();
            if (action.equals("attack_light") || action.equals("attack_heavy") || to.dot(look) > (isGurren()?0.05:-0.15)) {
                target.hurt(damageSources().mobAttack(this), damage);
                target.knockback(isGurren()?1.8:0.8, -look.x, -look.z);
            }
        }
        spiralBurst(action.equals("ultimate") ? ParticleTypes.FLAME : ParticleTypes.CRIT,
            action.equals("ultimate") ? 180 : 35, radius*0.45);
        level().playSound(null, blockPosition(), SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.PLAYERS, 2.2f, isGurren()?0.55f:0.95f);
    }

    private void spiralBurst(net.minecraft.core.particles.ParticleOptions particle, int count, double radius) {
        if (level().isClientSide) return;
        for (int i=0;i<count;i++) {
            double angle = (Math.PI*2*i)/count;
            double r = radius * (0.35 + (i % 7) / 10.0);
            double y = 0.4 + (i % 13) * (isGurren()?0.45:0.08);
            level().addParticle(particle, getX()+Math.cos(angle)*r, getY()+y, getZ()+Math.sin(angle)*r,
                Math.cos(angle)*0.12, 0.06, Math.sin(angle)*0.12);
        }
    }

    @Override public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("GurrenForm", isGurren());
        tag.putFloat("SpiralEnergy", getEnergy());
    }
    @Override public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        entityData.set(GURREN, tag.getBoolean("GurrenForm"));
        entityData.set(ENERGY, tag.contains("SpiralEnergy") ? tag.getFloat("SpiralEnergy") : 100f);
        refreshDimensions();
        AttributeInstance hp = getAttribute(Attributes.MAX_HEALTH);
        AttributeInstance damage = getAttribute(Attributes.ATTACK_DAMAGE);
        if (hp != null) hp.setBaseValue(isGurren()?1000:200);
        if (damage != null) damage.setBaseValue(isGurren()?50:10);
    }

    @Override public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "mecha_controller", 3, state -> {
            String anim = getActionName();
            if (anim == null || anim.equals("idle")) {
                if (state.isMoving()) anim = "walk"; else anim = "idle";
            }
            state.getController().setAnimation(RawAnimation.begin().thenLoop(anim));
            return PlayState.CONTINUE;
        }));
    }
    @Override public AnimatableInstanceCache getAnimatableInstanceCache() { return cache; }

    @Override public boolean hurt(DamageSource source, float amount) {
        if (source.getEntity() instanceof Player player && player == getControllingPassenger()) return false;
        return super.hurt(source, amount);
    }
}
