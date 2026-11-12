package com.spiralmecha.network;

import com.spiralmecha.entity.MechaEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import java.util.function.Supplier;

public record ControlPacket(int entityId, float forward, float strafe, boolean jump, boolean sprint) {
    public static void encode(ControlPacket p, FriendlyByteBuf b) {
        b.writeInt(p.entityId); b.writeFloat(p.forward); b.writeFloat(p.strafe); b.writeBoolean(p.jump); b.writeBoolean(p.sprint);
    }
    public static ControlPacket decode(FriendlyByteBuf b) {
        return new ControlPacket(b.readInt(), b.readFloat(), b.readFloat(), b.readBoolean(), b.readBoolean());
    }
    public static void handle(ControlPacket p, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null || !(player.getVehicle() instanceof MechaEntity mecha) || mecha.getId() != p.entityId) return;
            mecha.setInputs(p.forward, p.strafe, p.jump, p.sprint);
        });
        ctx.get().setPacketHandled(true);
    }
}
