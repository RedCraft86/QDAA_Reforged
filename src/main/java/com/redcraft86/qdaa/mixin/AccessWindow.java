package com.redcraft86.qdaa.mixin;

import com.mojang.blaze3d.platform.Window;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Window.class)
public interface AccessWindow {
    @Invoker("onFramebufferResize")
    void qdaa_onFramebufferResize(long window, int width, int height);
}
