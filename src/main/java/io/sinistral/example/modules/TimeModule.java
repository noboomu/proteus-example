/**
 * 
 */
package io.sinistral.example.modules;

import java.time.Clock;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

 
/**
 * @author jbauer
 *
 */
@Singleton
public class TimeModule extends AbstractModule
{ 
	 
	public void configure()
	{ 
		Clock clock = Clock.systemUTC();
		
		bind(Clock.class).toInstance(clock); 
	} 

}
