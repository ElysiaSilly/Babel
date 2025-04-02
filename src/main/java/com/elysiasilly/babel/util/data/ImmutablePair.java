package com.elysiasilly.babel.util.data;

import java.util.Objects;

public record ImmutablePair<F, S> (F first, S second) {

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ImmutablePair<?, ?> other && this.first.equals(other.first) && this.second.equals(other.second);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.first, this.second);
    }
}
