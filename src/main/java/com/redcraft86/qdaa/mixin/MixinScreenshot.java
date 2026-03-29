package com.redcraft86.qdaa.mixin;

import com.redcraft86.qdaa.QDAA;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Screenshot;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.pipeline.RenderTarget;
import static org.lwjgl.system.MemoryUtil.*;
import static org.lwjgl.stb.STBImageResize.*;
import static org.lwjgl.opengl.GL33.*;

import org.lwjgl.system.MemoryUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Screenshot.class)
public class MixinScreenshot {
    @Inject(method = "takeScreenshot", at = @At("HEAD"), cancellable = true)
    private static void qdaa_takeScreenshot(RenderTarget frameBuffer, CallbackInfoReturnable<NativeImage> cir) {

        // Credit: exaptations for the original implementation
        if (frameBuffer == Minecraft.getInstance().getMainRenderTarget()) {
            int fWidth = frameBuffer.width;
            int fHeight = frameBuffer.height;
            var sData = memAlloc(fWidth * fHeight * 4);
            try {
                frameBuffer.bindRead();
                glGetTexImage(GL_TEXTURE_2D, 0, GL_RGBA, GL_UNSIGNED_BYTE, sData);
                for (int i = 0; i < sData.limit(); i += 4) {
                    sData.put(i + 3, (byte)0xFF);
                }

                int sWidth = QDAA.unscale(fWidth);
                int sHeight = QDAA.unscale(fHeight);

                var img = new NativeImage(sWidth, sHeight, false);
                var dData = MemoryUtil.memByteBuffer(
                    ((AccessNativeImage)(Object)img).qdaa_getPixels(),
                    sWidth * sHeight * 4
                );

                stbir_resize_uint8_generic(
                    sData, fWidth, fHeight, 0, dData, sWidth, sHeight, 0,
                    4, 0, 3, STBIR_EDGE_CLAMP, STBIR_FILTER_BOX, STBIR_COLORSPACE_LINEAR
                );

                img.flipY();
                cir.setReturnValue(img);
            } finally {
                memFree(sData);
            }
        }
    }
}
