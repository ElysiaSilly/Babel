package com.elysiasilly.babel.util;

public class MutablePair<LEFT, RIGHT> {

    public LEFT left;
    public RIGHT right;

    public MutablePair(LEFT left, RIGHT right) {
        this.left = left;
        this.right = right;
    }
}
