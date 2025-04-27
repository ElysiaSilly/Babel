package com.elysiasilly.babel.util.misc;

import java.util.Objects;

public record ImmutableTrio<F, S, T> (F first, S second, T third) {

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ImmutableTrio<?, ?, ?> other && this.first.equals(other.first) && this.second.equals(other.second) && this.third.equals(other.third);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.first, this.second, this.third);
    }

    @Override
    public String toString() {
        return "[" + first.toString() + ", " + second.toString() + ", " + third.toString() + "]";
    }
}
