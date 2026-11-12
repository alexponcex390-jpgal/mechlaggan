package com.spiralmecha.network;

import com.spiralmecha.entity.MechaEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import java.util.function.Supplier;

public record ActionPacket(String action) {
    public static void encode(ActionPacket p, FriendlyByteBuf b) { b.writeUtf(p.action, 32); }
    public static ActionPacket decode(FriendlyByteBuf b) { return new ActionPacket(b.readUtf(32)); }
    public static void handle(ActionPacket p, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player != null && player.getVehicle() instanceof MechaEntity mecha) mecha.performAction(p.action);
        });
        ctx.get().setPacketHandled(true);
    }
}
