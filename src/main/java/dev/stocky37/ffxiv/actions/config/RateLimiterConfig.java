package dev.stocky37.ffxiv.actions.config;

import com.google.common.util.concurrent.RateLimiter;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@SuppressWarnings("UnstableApiUsage")
@ApplicationScoped
public class RateLimiterConfig {
	@Produces
	public RateLimiter rateLimiter(
		@ConfigProperty(name = "xivapi.rate-limit", defaultValue = "20") int rateLimit
	) {
		return RateLimiter.create(rateLimit);
	}
}
