package dev.stocky37.xiv.actions.jaxrs;

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

	@ConfigProperty(name = "xivapi.api-key") String apiKey;

	@Override
	public void filter(ClientRequestContext requestContext) {
		requestContext.setUri(UriBuilder.fromUri(requestContext.getUri())
			.queryParam("private_key", apiKey)
			.build());
	}
}
