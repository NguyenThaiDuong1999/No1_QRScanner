package com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils;

public class FrameMetaDataNo1 {

    private final int width;
    private final int height;
    private final int rotation;

    private FrameMetaDataNo1(int width, int height, int rotation) {
        this.width = width;
        this.height = height;
        this.rotation = rotation;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getRotation() {
        return rotation;
    }

    /** Builder of {@link FrameMetaDataNo1}. */
    public static class Builder {

        private int width;
        private int height;
        private int rotation;

        public Builder setWidth(int width) {
            this.width = width;
            return this;
        }

        public Builder setHeight(int height) {
            this.height = height;
            return this;
        }

        public Builder setRotation(int rotation) {
            this.rotation = rotation;
            return this;
        }

        public FrameMetaDataNo1 build() {
            return new FrameMetaDataNo1(width, height, rotation);
        }
    }
}