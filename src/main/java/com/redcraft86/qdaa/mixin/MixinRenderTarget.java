package com.redcraft86.qdaa.mixin;

import com.redcraft86.qdaa.QDAA;
import net.minecraft.client.Minecraft;
import com.mojang.blaze3d.pipeline.MainTarget;
import com.mojang.blaze3d.pipeline.RenderTarget;
import static org.lwjgl.opengl.GL33.*;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// Not really sure if Embeddium also access this but offset the priority just in case
@Mixin(value = RenderTarget.class, priority = 1001)
public class MixinRenderTarget {
    @Unique private boolean qdaa_isValid() {
        return (Object)this == Minecraft.getInstance().getMainRenderTarget();
    }

    @ModifyConstant(constant = @Constant(intValue = GL_NEAREST), method = "createBuffers")
    private int qdaa_linearFiltering(int original) {
        return qdaa_isValid() ? GL_LINEAR : original;
    }

    @Inject(method = "blitToScreen(II)V", at = @At("HEAD"), remap = false, cancellable = true)
    private void qdaa_sssaDraw(int width, int height, CallbackInfo ci) {
        if (qdaa_isValid()) {
            MainTarget self = (MainTarget)(Object)this;
            glBindFramebuffer(GL_READ_FRAMEBUFFER, self.frameBufferId);
            glBindFramebuffer(GL_DRAW_FRAMEBUFFER, 0);
            glBlitFramebuffer(
                    0, 0, width, height,
                    0, 0, QDAA.unscale(width), QDAA.unscale(height),
                    GL_COLOR_BUFFER_BIT, GL_LINEAR
            );

            ci.cancel();
        }
    }
}
