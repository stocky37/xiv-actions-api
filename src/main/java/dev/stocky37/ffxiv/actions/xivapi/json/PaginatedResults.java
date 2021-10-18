package dev.stocky37.ffxiv.actions.xivapi.json;

import java.util.List;

public record PaginatedResults<T>(List<T> Results) {
}
