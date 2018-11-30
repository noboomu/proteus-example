/**
 * 
 */
package io.sinistral.example.modules;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.pac4j.core.authorization.authorizer.RequireAnyRoleAuthorizer;
import org.pac4j.core.client.Clients;
import org.pac4j.core.config.ConfigFactory;
import org.pac4j.core.engine.DefaultCallbackLogic;
import org.pac4j.core.profile.CommonProfile;
import org.pac4j.oauth.client.GitHubClient;
import org.pac4j.undertow.account.Pac4jAccount;
import org.pac4j.undertow.context.UndertowWebContext;
import org.pac4j.undertow.handler.CallbackHandler;
import org.pac4j.undertow.handler.SecurityHandler;
import org.pac4j.undertow.profile.UndertowProfileManager;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import io.sinistral.example.security.CustomAuthorizer;
import io.sinistral.proteus.server.endpoints.EndpointInfo;
import io.undertow.security.api.SecurityContext;
import io.undertow.security.idm.Account;
import io.undertow.server.HandlerWrapper;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.RoutingHandler;
import io.undertow.server.handlers.PredicateHandler;
import io.undertow.server.handlers.ResponseCodeHandler;
import io.undertow.server.session.InMemorySessionManager;
import io.undertow.server.session.SessionAttachmentHandler;
import io.undertow.server.session.SessionCookieConfig;
import io.undertow.server.session.SessionManager;

/**
 * @author jbauer
 */
@Singleton
public class SecurityModule extends AbstractModule
{

	private static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(SecurityModule.class.getCanonicalName());


	@Inject
	@Named("security.callbackPath")
	protected String callbackPath;
	
	@Inject
	@Named("security.github.key")
	protected String githubKey;
	
	@Inject
	@Named("security.github.secret")
	protected String githubSecret;
	
	@Inject
	@Named("security.github.handlerKey")
	protected String handlerKey;
	
	@Inject
	private RoutingHandler router;

	@Inject
	@Named("registeredHandlerWrappers")
	private Map<String, HandlerWrapper> registeredHandlerWrappers;

	@Inject
	@Named("registeredEndpoints")
	protected Set<EndpointInfo> registeredEndpoints;
	
	public void configure()
	{

		binder().requestInjection(this);

		org.pac4j.core.config.Config securityConfig = new SecurityConfiguration(githubKey,githubSecret,callbackPath).build();

		binder().bind(org.pac4j.core.config.Config.class).toInstance(securityConfig);

		HttpHandler callbackHandler = CallbackHandler.build(securityConfig, null, true);

		router.add("GET", callbackPath, callbackHandler);

		router.add("POST", callbackPath, callbackHandler);
		
		final HandlerWrapper securityHandlerWrapper = new HandlerWrapper()
		{ 
			@Override
			public HttpHandler wrap(final HttpHandler handler)
			{
				return SecurityHandler.build(handler, securityConfig, "GithubClient");
			} 
		};
		
		registeredHandlerWrappers.put(handlerKey, securityHandlerWrapper);
		
		SessionManager sessionManager = new InMemorySessionManager("SESSION_MANAGER");
		SessionCookieConfig sessionConfig = new SessionCookieConfig();

		SessionAttachmentHandler sessionAttachmentHandler = new SessionAttachmentHandler(sessionManager, sessionConfig);

		binder().bind(SessionAttachmentHandler.class).toInstance(sessionAttachmentHandler);
		
 

	}

	private static class SecurityConfiguration implements ConfigFactory
	{
		private String key;
		private String secret;
		private String callbackPath;
		
		public SecurityConfiguration(String key, String secret, String callbackPath)
		{
			this.key = key;
			this.secret = secret;
			this.callbackPath = callbackPath;
		}
		
		public org.pac4j.core.config.Config build(Object... parameters)
		{

			final GitHubClient client = new GitHubClient(this.key, this.secret);

			final Clients clients = new Clients("http://localhost:8090" + callbackPath, client);

			final org.pac4j.core.config.Config config = new org.pac4j.core.config.Config(clients);
			config.addAuthorizer("admin", new RequireAnyRoleAuthorizer("ROLE_ADMIN"));
			config.addAuthorizer("custom", new CustomAuthorizer());

			return config;
		}
	}
}
