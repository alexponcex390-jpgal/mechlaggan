package com.spiralmecha.item;

import com.spiralmecha.registry.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import com.spiralmecha.entity.MechaEntity;

public class SpiralCoreItem extends Item {
    public SpiralCoreItem(Properties properties) { super(properties); }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        Player player = context.getPlayer();
        if (level.isClientSide || player == null) return InteractionResult.SUCCESS;
        BlockPos pos = context.getClickedPos().relative(context.getClickedFace());
        MechaEntity mecha = ModEntities.MECHA.get().create(level);
        if (mecha == null) return InteractionResult.FAIL;
        mecha.setPos(pos.getX()+0.5, pos.getY(), pos.getZ()+0.5);
        mecha.setYRot(player.getYRot());
        level.addFreshEntity(mecha);
        player.startRiding(mecha);
        if (!player.getAbilities().instabuild) context.getItemInHand().shrink(1);
        return InteractionResult.CONSUME;
    }
}
