/**
 * 
 */
package io.sinistral.example.services;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Binder;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import io.sinistral.proteus.services.BaseService;

/**
 * @author jbauer
 *
 */


@Singleton
public class TimeService extends BaseService
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

