package com.imaginedreal.gwtgantt.geometry;

public class Rectangle {

    private int left, top, right, bottom;

    public Rectangle() {
    }

    public Rectangle(Point p1, Point p2) {
        assert(p1!=null);
        assert(p2!=null);
        left = p1.getX();
        right = p2.getX();
        top = p1.getY();
        bottom = p2.getY();
    }

    public Rectangle(int left, int top, int width, int height) {
        this.left = left;
        this.top = top;
        this.right = left + width;
        this.bottom = top + height;
    }

    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public int getRight() {
        return right;
    }

    public void setRight(int right) {
        this.right = right;
    }

    public int getBottom() {
        return bottom;
    }

    public void setBottom(int bottom) {
        this.bottom = bottom;
    }

    public int getWidth() {
        return right - left;
    }

    public int getHeight() {
        return bottom - top;
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("Rectangle: ")
                .append("x {").append(left).append("} ")
                .append("y {").append(top).append("} ")
                .append("w {").append(getWidth()).append("} ")
                .append("h {").append(getHeight()).append("}")
                .toString();
    }
}
