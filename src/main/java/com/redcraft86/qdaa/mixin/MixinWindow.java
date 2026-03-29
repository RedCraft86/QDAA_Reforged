package com.redcraft86.qdaa.mixin;

import com.redcraft86.qdaa.QDAA;

import org.lwjgl.glfw.GLFW;
import com.mojang.blaze3d.platform.Window;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Window.class)
public class MixinWindow {
    @ModifyVariable(method = "onFramebufferResize", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private int qdaa_modifyWidth(int width) {
        return QDAA.upscale(width);
    }

    @ModifyVariable(method = "onFramebufferResize", at = @At("HEAD"), ordinal = 1, argsOnly = true)
    private int qdaa_modifyHeight(int height) {
        return QDAA.upscale(height);
    }

    @Redirect(method = "refreshFramebufferSize",
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/lwjgl/glfw/GLFW;glfwGetFramebufferSize(J[I[I)V"
            ), remap = false
    )
    private void qdaa_modifyInitialSize(long window, int[] w, int[] h) {
        GLFW.glfwGetFramebufferSize(window, w, h);
        w[0] = QDAA.upscale(w[0]);
        h[0] = QDAA.upscale(h[0]);
    }
}
