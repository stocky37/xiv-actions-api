package dev.stocky37.xiv.actions.xivapi.json;

import java.util.List;

public record PaginatedResults<T>(List<T> Results) {
}
