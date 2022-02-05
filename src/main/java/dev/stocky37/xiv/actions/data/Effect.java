package dev.stocky37.xiv.actions.data;

import java.time.Duration;

public record Effect(String id, Duration started, Duration end) {

}
