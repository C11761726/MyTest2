package com.mytest;

public class SettingDataStruct {
    private String width;
    private String height;
    private String left;
    private String top;
    private String imagePath;

    public String getHeight() {
        return height;
    }

    public int getHeightInt() {
        if ((height == null) || height.isEmpty()) {
            return 200;
        }
        return Integer.parseInt(height);
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWidth() {
        return width;
    }

    public int getWidthInt() {
        if ((width == null) || width.isEmpty()) {
            return 200;
        }
        return Integer.parseInt(width);
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getLeft() {
        return left;
    }

    public int getLeftInt() {
        if ((left == null) || left.isEmpty()) {
            return 200;
        }
        return Integer.parseInt(left);
    }

    public void setLeft(String left) {
        this.left = left;
    }

    public String getTop() {
        return top;
    }

    public int getTopInt() {
        if ((top == null) || top.isEmpty()) {
            return 200;
        }
        return Integer.parseInt(top);
    }

    public void setTop(String top) {
        this.top = top;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
