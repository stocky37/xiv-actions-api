package dev.stocky37.xiv.model;

import dev.stocky37.xiv.core.Timeline;
import java.util.Collection;

public record Rotation(Collection<Timeline.Event> rotation, double dps) {
}
