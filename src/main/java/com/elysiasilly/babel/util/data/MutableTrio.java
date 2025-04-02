package com.elysiasilly.babel.util.data;

import java.util.Objects;

public class MutableTrio<F, S, T> {

    public F first;
    public S second;
    public T third;

    public MutableTrio(F first, S second, T third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof MutableTrio<?, ?, ?> other && this.first.equals(other.first) && this.second.equals(other.second) && this.third.equals(other.third);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.first, this.second, this.third);
    }
}
