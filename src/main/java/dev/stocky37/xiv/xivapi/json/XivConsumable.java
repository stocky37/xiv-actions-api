package dev.stocky37.xiv.xivapi.json;

import java.util.Map;

public record XivConsumable(
	Long ID,
	String Name,
	String Icon,
	String IconHD,
	Integer CooldownS,
	Kind ItemKind,
	Action ItemAction,
	Map<String, Bonus> Bonuses
) {

	public record Bonus(Integer MaxHQ, Integer ValueHQ) {}

	public record Kind(Long ID) {}

	public record Action(Long DataHQ2) {}
}
