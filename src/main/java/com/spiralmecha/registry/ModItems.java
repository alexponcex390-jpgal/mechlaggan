package com.spiralmecha.registry;

import com.spiralmecha.SpiralMecha;
import com.spiralmecha.item.SpiralCoreItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SpiralMecha.MOD_ID);
    public static final RegistryObject<Item> SPIRAL_CORE = ITEMS.register("spiral_core",
        () -> new SpiralCoreItem(new Item.Properties().stacksTo(1)));
}
