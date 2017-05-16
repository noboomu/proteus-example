/**
 * 
 */
package io.sinistral.example.wrappers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.undertow.server.HandlerWrapper;
import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.accesslog.AccessLogHandler;
import io.undertow.server.handlers.accesslog.AccessLogReceiver;

/**
 * @author jbauer
 *
 */
public class AccessLogWrapper  implements HandlerWrapper, AccessLogReceiver
{
	private static Logger log = LoggerFactory.getLogger(AccessLogWrapper.class.getCanonicalName());

	protected AccessLogHandler accessLogHandler;
 
	/* (non-Javadoc)
	 * @see io.undertow.server.HandlerWrapper#wrap(io.undertow.server.HttpHandler)
	 */
	@Override
	public HttpHandler wrap(HttpHandler handler)
	{
		return new AccessLogHandler(handler,this,"combined",this.getClass().getClassLoader());
	}

	/* (non-Javadoc)
	 * @see io.undertow.server.handlers.accesslog.AccessLogReceiver#logMessage(java.lang.String)
	 */
	@Override
	public void logMessage(String message)
	{
		log.debug(message); 
	}

}
