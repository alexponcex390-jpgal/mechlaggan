package com.spiralmecha.client;

import com.spiralmecha.SpiralMecha;
import com.spiralmecha.entity.MechaEntity;
import com.spiralmecha.network.ModNetwork;
import com.spiralmecha.registry.ModEntities;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = SpiralMecha.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
class ClientModEvents {
    @SubscribeEvent public static void registerRenderers(net.minecraftforge.client.event.EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.MECHA.get(), MechaRenderer::new);
    }
    @SubscribeEvent public static void registerKeys(RegisterKeyMappingsEvent event) {
        ClientEvents.registerKeys(event);
    }
}

@Mod.EventBusSubscriber(modid = SpiralMecha.MOD_ID, value = Dist.CLIENT)
public class ClientEvents {
    public static KeyMapping skill1, skill2, skill3, ultimate, transform, dismount, lightAttack;
    private static boolean attackWasDown = false;
    public static void registerKeys(RegisterKeyMappingsEvent event) {
        String cat = "key.categories.spiralmecha";
        skill1 = new KeyMapping("key.spiralmecha.skill1", GLFW.GLFW_KEY_1, cat);
        skill2 = new KeyMapping("key.spiralmecha.skill2", GLFW.GLFW_KEY_2, cat);
        skill3 = new KeyMapping("key.spiralmecha.skill3", GLFW.GLFW_KEY_3, cat);
        ultimate = new KeyMapping("key.spiralmecha.ultimate", GLFW.GLFW_KEY_R, cat);
        transform = new KeyMapping("key.spiralmecha.transform", GLFW.GLFW_KEY_G, cat);
        dismount = new KeyMapping("key.spiralmecha.dismount", GLFW.GLFW_KEY_X, cat);
        lightAttack = new KeyMapping("key.spiralmecha.attack", GLFW.GLFW_MOUSE_BUTTON_LEFT, cat);
        event.register(skill1); event.register(skill2); event.register(skill3);
        event.register(ultimate); event.register(transform); event.register(dismount); event.register(lightAttack);
    }

    @SubscribeEvent public static void clientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null) return;
        if (!(mc.player.getVehicle() instanceof MechaEntity mecha)) return;
        float f = 0, s = 0;
        if (mc.options.keyUp.isDown()) f++;
        if (mc.options.keyDown.isDown()) f--;
        if (mc.options.keyLeft.isDown()) s++;
        if (mc.options.keyRight.isDown()) s--;
        ModNetwork.sendControls(mecha.getId(), f, s, mc.options.keyJump.isDown(), mc.options.keySprint.isDown());
        while (skill1.consumeClick()) ModNetwork.sendAction("skill_1");
        while (skill2.consumeClick()) ModNetwork.sendAction("skill_2");
        while (skill3.consumeClick()) ModNetwork.sendAction("skill_3");
        while (ultimate.consumeClick()) ModNetwork.sendAction("ultimate");
        while (transform.consumeClick()) ModNetwork.sendAction("transform");
        while (dismount.consumeClick()) ModNetwork.sendAction("dismount");
        boolean down = lightAttack.isDown();
        if (down && !attackWasDown) ModNetwork.sendAction("attack_light");
        attackWasDown = down;
    }

    @SubscribeEvent public static void renderHud(RenderGuiOverlayEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || !(mc.player.getVehicle() instanceof MechaEntity mecha)) return;
        GuiGraphics g = event.getGuiGraphics();
        int x = 12, y = mc.getWindow().getGuiScaledHeight()-52;
        int w = 150;
        g.fill(x-5,y-5,x+w+8,y+42,0xB0101020);
        float health = mecha.getHealth()/mecha.getMaxHealth();
        float energy = mecha.getEnergy()/100f;
        g.fill(x,y,x+w,y+8,0xFF3A1820);
        g.fill(x,y,x+(int)(w*Math.max(0,health)),y+8,0xFFE53935);
        g.fill(x,y+13,x+w,y+21,0xFF18243A);
        g.fill(x,y+13,x+(int)(w*energy),y+21,0xFF28C7D8);
        g.drawString(mc.font, mecha.isGurren() ? "GURREN LAGANN" : "LAGGAN", x, y+26, 0xFFFFD75E, false);
        g.drawString(mc.font, "ESPIRAL " + (int)mecha.getEnergy() + "%", x+82, y+26, 0xFF7BE8F2, false);
    }
}
