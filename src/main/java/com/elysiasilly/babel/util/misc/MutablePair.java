package com.elysiasilly.babel.util.misc;

import java.util.Objects;

public class MutablePair<F, S> {

    public F first;
    public S second;

    public MutablePair(F first, S second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof MutablePair<?, ?> other && this.first.equals(other.first) && this.second.equals(other.second);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.first, this.second);
    }
}
