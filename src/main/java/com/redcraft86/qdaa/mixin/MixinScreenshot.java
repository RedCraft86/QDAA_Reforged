package com.redcraft86.qdaa.mixin;

import com.redcraft86.qdaa.WindowSize;
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
        if (frameBuffer == Minecraft.getInstance().getMainRenderTarget()) {
            int uWidth = frameBuffer.width;
            int uHeight = frameBuffer.height;
            var sData = memAlloc(uWidth * uHeight * 4);
            try {
                frameBuffer.bindRead();
                glGetTexImage(GL_TEXTURE_2D, 0, GL_RGBA, GL_UNSIGNED_BYTE, sData);
                for (int i = 0; i < sData.limit(); i += 4) {
                    sData.put(i + 3, (byte)0xFF);
                }

                int oWidth = WindowSize.oWidth();
                int oHeight = WindowSize.oHeight();

                var img = new NativeImage(oWidth, oHeight, false);
                var dData = MemoryUtil.memByteBuffer(
                    ((AccessNativeImage)(Object)img).qdaa_getPixels(),
                    oWidth * oHeight * 4
                );

                stbir_resize_uint8_generic(
                    sData, uWidth, uHeight, 0, dData, oWidth, oHeight, 0,
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
