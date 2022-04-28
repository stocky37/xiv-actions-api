package dev.stocky37.xiv.model.transform;

import dev.stocky37.xiv.model.Status;
import dev.stocky37.xiv.util.Util;
import dev.stocky37.xiv.xivapi.json.XivStatus;
import java.time.Duration;
import java.util.Collections;
import java.util.Optional;
import java.util.function.Function;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class StatusConverter implements Function<XivStatus, Status> {
	private final Util util;

	@Inject
	public StatusConverter(Util util) {
		this.util = util;
	}

	@Override
	public Status apply(XivStatus status) {
		return new Status(
			status.ID().toString(),
			status.Name(),
			util.prefixXivApi(status.Icon()),
			util.prefixXivApi(status.IconHD()),
			Duration.ZERO,
			Optional.empty(),
			Collections.emptyList()
		);
	}
}
