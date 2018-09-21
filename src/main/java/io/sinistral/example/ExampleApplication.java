package io.sinistral.example;

import io.sinistral.example.controllers.Examples;
import io.sinistral.proteus.ProteusApplication;

/**
 * Hello world!
 *
 */
public class ExampleApplication extends ProteusApplication
{
    public static void main( String[] args )
    {
        
        ExampleApplication app = new ExampleApplication();
        
        app.addService(io.sinistral.proteus.services.SwaggerService.class);
		 
		app.addService(io.sinistral.example.services.TimeService.class);
  
		app.addModule(io.sinistral.example.modules.TimeModule.class);

		app.addController(Examples.class);  
		
		app.start();
        
    }
}
