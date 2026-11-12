package com.spiralmecha;

import com.mojang.logging.LogUtils;
import com.spiralmecha.registry.ModEntities;
import com.spiralmecha.registry.ModItems;
import com.spiralmecha.network.ModNetwork;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import software.bernie.geckolib.GeckoLib;
import org.slf4j.Logger;

@Mod(SpiralMecha.MOD_ID)
public class SpiralMecha {
    public static final String MOD_ID = "spiralmecha";
    public static final Logger LOGGER = LogUtils.getLogger();

    public SpiralMecha() {
        GeckoLib.initialize();
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ModEntities.ENTITY_TYPES.register(bus);
        ModItems.ITEMS.register(bus);
        ModNetwork.register();
    }
    public static ResourceLocation id(String path) { return new ResourceLocation(MOD_ID, path); }
}
