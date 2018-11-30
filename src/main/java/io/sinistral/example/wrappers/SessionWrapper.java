/**
 * 
 */
package io.sinistral.example.wrappers;

import io.undertow.server.HandlerWrapper;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.session.Session;
import io.undertow.server.session.SessionConfig;
import io.undertow.server.session.SessionManager;

/**
 * @author jbauer
 * 
 * Ensures a session is present.
 *
 */
public class SessionWrapper  implements HandlerWrapper 
{
	@Override
	public HttpHandler wrap(HttpHandler handler)
	{
		return new HttpHandler(){

			@Override
			public void handleRequest(HttpServerExchange exchange) throws Exception
			{ 
				 
				 SessionManager sm = exchange.getAttachment(SessionManager.ATTACHMENT_KEY);
		         SessionConfig sessionConfig = exchange
		                 .getAttachment(SessionConfig.ATTACHMENT_KEY);
		         
		         Session session = sm.getSession(exchange, sessionConfig);
		         
		         if(session == null)
		         {
		        	 session = sm.createSession(exchange, sessionConfig);
		         }
		         
				handler.handleRequest(exchange);
				
			}
			
			
		};
	}
}
