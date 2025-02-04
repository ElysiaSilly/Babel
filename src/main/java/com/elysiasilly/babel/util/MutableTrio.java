package com.elysiasilly.babel.util;

public class MutableTrio<LEFT, MIDDLE, RIGHT> {

    public LEFT left;
    public MIDDLE middle;
    public RIGHT right;

    public MutableTrio(LEFT left, MIDDLE middle, RIGHT right) {
        this.left = left;
        this.middle = middle;
        this.right = right;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof MutableTrio<?, ?, ?> other && this.left.equals(other.left) && this.middle.equals(other.middle) && this.right.equals(other.right);
    }
}
