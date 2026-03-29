package com.redcraft86.qdaa.mixin;

import com.mojang.blaze3d.platform.NativeImage;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(NativeImage.class)
public interface AccessNativeImage {
    @Accessor("pixels")
    long qdaa_getPixels();
}
