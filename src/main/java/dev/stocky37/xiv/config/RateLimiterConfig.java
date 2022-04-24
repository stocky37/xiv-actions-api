package dev.stocky37.xiv.config;

import com.google.common.util.concurrent.RateLimiter;
import javax.enterprise.inject.Produces;
import javax.inject.Singleton;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@SuppressWarnings("UnstableApiUsage")
@Singleton
public class RateLimiterConfig {
	@Produces
	public RateLimiter rateLimiter(
		@ConfigProperty(name = "xivapi.rate-limit", defaultValue = "20") int rateLimit
	) {
		return RateLimiter.create(rateLimit);
	}
}
