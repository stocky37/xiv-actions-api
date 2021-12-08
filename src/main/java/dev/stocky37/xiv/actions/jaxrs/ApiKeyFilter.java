package dev.stocky37.xiv.actions.jaxrs;

import java.util.Optional;
import javax.ws.rs.ConstrainedTo;
import javax.ws.rs.RuntimeType;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.ext.Provider;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@Provider
@ConstrainedTo(RuntimeType.CLIENT)
public class ApiKeyFilter implements ClientRequestFilter {

	@ConfigProperty(name = "xivapi.api-key") Optional<String> apiKey;

	@Override
	public void filter(ClientRequestContext requestContext) {
		apiKey.ifPresent(s -> requestContext.setUri(UriBuilder.fromUri(requestContext.getUri())
			.queryParam("private_key", s)
			.build()));
	}
}
