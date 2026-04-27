package com.redcraft86.qdaa;

import net.minecraft.client.Minecraft;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;

import com.redcraft86.qdaa.mixin.AccessWindow;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(QDAA.MOD_ID)
public class QDAA {
    public static final String MOD_ID = "qdaa";

    public QDAA(FMLJavaModLoadingContext context)
    {
        IEventBus modEventBus = context.getModEventBus();

        modEventBus.addListener(this::configReload);
        context.registerConfig(ModConfig.Type.CLIENT, ClientCfg.SPEC);
    }

    public void configReload(ModConfigEvent event) {
        if (event.getConfig().getSpec() == ClientCfg.SPEC
            && ClientCfg.isLoaded() && ClientCfg.HOT_RELOADING.get()) {

            // Mimic a window resize when changing scale factor to properly update the render target
            RenderSystem.recordRenderCall(() -> {
                Minecraft mc = Minecraft.getInstance();
                Window window = mc.getWindow();

                ((AccessWindow)(Object)window).qdaa_onFramebufferResize(
                    window.getWindow(), window.getScreenWidth(), window.getScreenHeight()
                );
            });
        }
    }
}