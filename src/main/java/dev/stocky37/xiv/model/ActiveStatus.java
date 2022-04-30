package dev.stocky37.xiv.model;

import java.time.Duration;

public record ActiveStatus(Status status, Duration start, Duration end) {

}
