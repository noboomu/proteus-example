/**
 * 
 */
package io.sinistral.example.wrappers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.undertow.server.HandlerWrapper;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.accesslog.AccessLogHandler;
import io.undertow.server.handlers.accesslog.AccessLogReceiver;

/**
 * @author jbauer
 *
 */
public class DummyWrapper  implements HandlerWrapper 
{
	private static Logger log = LoggerFactory.getLogger(DummyWrapper.class.getCanonicalName());
 
 
	/* (non-Javadoc)
	 * @see io.undertow.server.HandlerWrapper#wrap(io.undertow.server.HttpHandler)
	 */
	@Override
	public HttpHandler wrap(HttpHandler handler)
	{
		return new HttpHandler(){

			@Override
			public void handleRequest(HttpServerExchange exchange) throws Exception
			{
				// TODO Auto-generated method stub
				
				log.debug(exchange.getHostAndPort());
				
				handler.handleRequest(exchange);
				
			}
			
			
		};
	}
 

}
