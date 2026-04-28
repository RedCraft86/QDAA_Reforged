package com.redcraft86.qdaa.mixin;

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
        return width * 2;
    }

    @ModifyVariable(method = "onFramebufferResize", at = @At("HEAD"), ordinal = 1, argsOnly = true)
    private int qdaa_modifyHeight(int height) {
        return height * 2;
    }

    @Redirect(method = "refreshFramebufferSize",
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/lwjgl/glfw/GLFW;glfwGetFramebufferSize(J[I[I)V"
            ), remap = false
    )
    private void qdaa_modifyInitialSize(long window, int[] w, int[] h) {
        GLFW.glfwGetFramebufferSize(window, w, h);
        w[0] *= 2;
        h[0] *= 2;
    }
}
