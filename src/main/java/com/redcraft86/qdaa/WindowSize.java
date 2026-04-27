package com.redcraft86.qdaa;

import org.lwjgl.glfw.GLFW;

public class WindowSize {
    private static int width = 0;
    private static int height = 0;

    public static void updateWindow(long window) {
        int[] w = new int[1], h = new int[1];
        GLFW.glfwGetFramebufferSize(window, w, h);
        WindowSize.width = w[0];
        WindowSize.height = h[0];
    }

    public static void setWidth(int value) {
        WindowSize.width = value;
    }

    public static void setHeight(int value) {
        WindowSize.height = value;
    }

    public static int oWidth() {
        return WindowSize.width;
    }

    public static int oHeight() {
        return WindowSize.height;
    }

    public static int uWidth() {
        return upscale(WindowSize.width);
    }

    public static int uHeight() {
        return upscale(WindowSize.height);
    }

    private static int upscale(int value) {
        return (int)(value * (ClientCfg.isLoaded() ? ClientCfg.SCALE_FACTOR.get().Scale : 1.0f));
    }
}
