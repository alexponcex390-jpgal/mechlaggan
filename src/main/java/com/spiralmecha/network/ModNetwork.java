package com.spiralmecha.network;

import com.spiralmecha.SpiralMecha;
import com.spiralmecha.entity.MechaEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public final class ModNetwork {
    private static final String VERSION = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
        SpiralMecha.id("main"), () -> VERSION, VERSION::equals, VERSION::equals);
    private static int id = 0;
    public static void register() {
        CHANNEL.registerMessage(id++, ControlPacket.class, ControlPacket::encode, ControlPacket::decode, ControlPacket::handle);
        CHANNEL.registerMessage(id++, ActionPacket.class, ActionPacket::encode, ActionPacket::decode, ActionPacket::handle);
    }
    public static void sendAction(String action) { CHANNEL.sendToServer(new ActionPacket(action)); }
    public static void sendControls(int entityId, float forward, float strafe, boolean jump, boolean sprint) {
        CHANNEL.sendToServer(new ControlPacket(entityId, forward, strafe, jump, sprint));
    }
}
