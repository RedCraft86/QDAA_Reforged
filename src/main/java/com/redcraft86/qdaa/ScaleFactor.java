package com.redcraft86.qdaa;

public enum ScaleFactor {
    OFF     (1.00f),
    LOW     (1.25f),
    MEDIUM  (1.50f),
    HIGH    (1.75f),
    ULTRA   (2.00f);

    public final float Scale;
    ScaleFactor(float Scale) {
        this.Scale = Scale;
    }
}
