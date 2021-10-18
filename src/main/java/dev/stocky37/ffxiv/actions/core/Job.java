package dev.stocky37.ffxiv.actions.core;

import java.util.List;

public record Job(int id, String name, String abbreviation, List<Action> actions) {

}
