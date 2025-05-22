package com.elysiasilly.babel.api.client.screen.neo;

import net.neoforged.bus.api.Event;

import java.util.List;

public abstract class BabelScreenEvents extends Event {

    private final BabelScreen screen;

    protected BabelScreenEvents(BabelScreen screen) {
        this.screen = screen;
    }

    public BabelScreen screen() {
        return this.screen;
    }

    public static class BuiltElements extends BabelScreenEvents {

        private final List<BabelElement> elements;

        protected BuiltElements(BabelScreen screen, List<BabelElement> elements) {
            super(screen);
            this.elements = elements;
        }

        public List<BabelElement> elements() {
            return this.elements;
        }
    }
}
