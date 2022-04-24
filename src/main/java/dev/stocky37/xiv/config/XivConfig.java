package dev.stocky37.xiv.config;

import io.smallrye.config.ConfigMapping;
import java.time.Duration;

@ConfigMapping(prefix = "xiv")
public interface XivConfig {
	Duration ping();

	Duration animationLock();

	String gcdGroup();
}
