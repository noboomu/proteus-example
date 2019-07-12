/**
 * 
 */
package io.sinistral.example.services;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;

import io.sinistral.proteus.services.DefaultService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Binder;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * @author jbauer
 *
 */


@Singleton
public class TimeService extends DefaultService
{
	private static Logger log = LoggerFactory.getLogger(TimeService.class.getCanonicalName());

	protected Clock clock;
	
	@Inject
	public TimeService( Clock clock )
	{ 
		this.clock = clock;
	}
	 
	@Override
	public void configure(Binder binder)
	{
		log.info("Configuring " + this.getClass().getSimpleName());

	}

	@Override
	protected void startUp() throws Exception
	{
 		super.startUp();
 		
	}
	 

	@Override
	protected void shutDown() throws Exception
	{ 
		super.shutDown();
		
		log.info("Shutting down " + this.getClass().getSimpleName());
	}

	public LocalDate getLocalDate()
	{
		return LocalDate.now(this.clock);
	}
  
	public Instant getInstant()
	{
		return Instant.now(this.clock);
	}
}

