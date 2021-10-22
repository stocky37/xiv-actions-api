package dev.stocky37.xiv.actions.xivapi.json;

import java.util.List;

public record XivApiPaginatedList<T>(List<T> Results) {
}
