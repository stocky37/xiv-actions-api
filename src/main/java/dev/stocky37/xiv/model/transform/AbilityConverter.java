package dev.stocky37.xiv.model.transform;

import dev.stocky37.xiv.model.Ability;
import dev.stocky37.xiv.util.Util;
import dev.stocky37.xiv.xivapi.json.XivAbility;
import java.time.Duration;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class AbilityConverter implements Function<XivAbility, Ability> {

	private final String gcdGroup;
	private final Util util;

	@Inject
	public AbilityConverter(
		@ConfigProperty(name = "xiv.gcd-group") String gcdGroup,
		Util util
	) {
		this.gcdGroup = gcdGroup;
		this.util = util;
	}

	@Override
	public Ability apply(XivAbility ability) {
		final Set<String> cooldownGroups = cooldownGroups(ability);
		final boolean onGcd = cooldownGroups.remove(gcdGroup);

		return Ability.builder()
			.withId(ability.ID().toString())
			.withName(ability.Name())
			.withOnGCD(onGcd)
			.withAbilityType(abilityType(ability.ActionCategory().ID()))
			.withCast(duration100ms(ability.Cast100ms()))
			.withRecast(duration100ms(ability.Recast100ms()))
			.withComboFrom(comboAction(ability.ActionComboTargetID()))
			.withCooldownGroups(cooldownGroups)
			.withDamageType(damageType(ability.AttackTypeTargetID()))
			.withDescription(ability.Description())
			.withIcon(util.prefixXivApi(ability.Icon()))
			.withIconHD(util.prefixXivApi(ability.IconHD()))
			.withLevel(ability.ClassJobLevel())
			.withRoleAction(ability.IsRoleAction())
			.build();
	}

	public Duration duration100ms(long duration) {
		return Duration.ofMillis(duration * 100);
	}

	public Ability.DamageType damageType(int damageType) {
		return switch(damageType) {
			case -1, 1, 2, 3 -> Ability.DamageType.PHYSICAL;
			case 0 -> null;
			case 5 -> Ability.DamageType.MAGICAL;
			default -> throw new RuntimeException("Unknown damage type: " + damageType);
		};
	}

	private Ability.AbilityType abilityType(int category) {
		return switch(category) {
			case 2 -> Ability.AbilityType.SPELL;
			case 3 -> Ability.AbilityType.WEAPONSKILL;
			case 4 -> Ability.AbilityType.ABILITY;
			default -> throw new RuntimeException("Unknown ability type: " + category);
		};
	}

	private String comboAction(Long actionId) {
		return actionId == 0 ? null : actionId.toString();
	}

	private Set<String> cooldownGroups(XivAbility t) {
		final Set<String> groups = new HashSet<>();
		if(t.CooldownGroup() > 0) {
			groups.add(t.CooldownGroup().toString());
		}
		if(t.AdditionalCooldownGroup() > 0) {
			groups.add(t.AdditionalCooldownGroup().toString());
		}
		return groups;
	}
}
