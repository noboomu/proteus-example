/**
 * 
 */
package io.sinistral.example.controllers;

import static io.sinistral.proteus.server.ServerResponse.response;

import java.nio.ByteBuffer;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.google.common.collect.ImmutableMap;
import com.google.inject.Singleton;
import com.jsoniter.output.JsonStream;

import io.sinistral.example.models.Fortune;
import io.sinistral.example.models.User;
import io.sinistral.example.models.World;
import io.sinistral.example.wrappers.AccessLogWrapper;
import io.sinistral.example.wrappers.DummyWrapper;
import io.sinistral.proteus.annotations.Chain;
import io.sinistral.proteus.server.ServerRequest;
import io.sinistral.proteus.server.ServerResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.ResponseHeader;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HttpString;


/**
 * @author jbauer
 *
 */

@Api(tags = "examples",
authorizations = {@Authorization(value = "default-api-key")} )
@Path("/examples")
@Produces((MediaType.APPLICATION_JSON)) 
@Consumes((MediaType.WILDCARD)) 
@Singleton
@Chain(DummyWrapper.class)
public class Examples
{  
	  
	
	@GET
	@Path("/plaintext")
	@Produces((MediaType.TEXT_PLAIN)) 
	@ApiOperation(value = "Plaintext endpoint",   httpMethod = "GET",  response=String.class  )
	public void plaintext(HttpServerExchange exchange)
	{ 
		io.sinistral.proteus.server.ServerResponse.response("Hello, World!").contentType(MediaType.TEXT_PLAIN).send(exchange);
	}
	
	
	@GET
	@Path("/json")
	@Chain({AccessLogWrapper.class})
	@ApiOperation(value = "Json serialization endpoint",   httpMethod = "GET", response=Map.class  )
	public void json(HttpServerExchange exchange)
	{ 
		io.sinistral.proteus.server.ServerResponse.response( JsonStream.serializeToBytes(ImmutableMap.of("message", "Hello, World!")) ).applicationJson().send(exchange);
	}
	
	@GET
	@Path("/echo")
	@Produces((MediaType.TEXT_PLAIN)) 
	@ApiOperation(value = "Echo a message",   httpMethod = "GET", response=String.class )
	public io.sinistral.proteus.server.ServerResponse<ByteBuffer> echo(String message)
	{ 
		return io.sinistral.proteus.server.ServerResponse.response("Hello, World!").contentType(MediaType.TEXT_PLAIN);
	}
	
	@GET
	@Path("/world")
	@Produces((MediaType.APPLICATION_JSON)) 
	@ApiOperation(value = "Return a random world instance",   httpMethod = "GET", response=World.class )
	public io.sinistral.proteus.server.ServerResponse<World> randomWorld(  Integer id,  Integer randomNumber )
	{ 
		return io.sinistral.proteus.server.ServerResponse.response().entity(new World(id,randomNumber));
	}
	
	@GET
	@Path("/worlds")
	@Produces((MediaType.APPLICATION_JSON)) 
	@ApiOperation(value = "Return a list of random world instances",   httpMethod = "GET", response=World.class, responseContainer="list" )
	public io.sinistral.proteus.server.ServerResponse<List<World>> randomWorlds(   List<Integer> ids,  Integer randomNumber )
	{ 
		List<World> worlds = new ArrayList<>();
		
		for( Integer id : ids )
		{
			worlds.add(new World(id,randomNumber));
		}
		
		return io.sinistral.proteus.server.ServerResponse.response().entity(worlds);
	}
	
	@GET
	@Path("/future/user")
	@ApiOperation(value = "Future user endpoint",   httpMethod = "GET" )
	public CompletableFuture<ServerResponse<User>> responseFutureUser()
	{ 
		return CompletableFuture.completedFuture(response( new User(123L) ).applicationJson() );
	}
	
	@GET
	@Path("/ebean/fortune/{fortuneId}")
	@ApiOperation(value = "Ebean fortune",   httpMethod = "GET" ,
			 responseHeaders = @ResponseHeader(name = "X-Rack-Cache", description = "Explains whether or not a cache was used", response = Boolean.class)
			 )
	public ServerResponse<Fortune> ebeanFortune( Long fortuneId )
	{ 
		Fortune fortune = Fortune.find.byId(fortuneId);
		
		ServerResponse<Fortune> response = response(fortune)
				.header(HttpString.tryFromString("X-Rack-Cache"), "false")
				.applicationJson();
		
		if(fortune == null)
		{
			return response.notFound();
		} 
		else
		{
			return response;
		}
	}
	
	@GET
	@Path("/complex/{pathLong}")
	@ApiOperation(value = "Complex parameters", httpMethod = "GET")
	public ServerResponse<Map<String,Object>> complexParameters(
	                    final ServerRequest serverRequest, 
	                    @PathParam("pathLong") final Long pathLong, 
	                    @QueryParam("optionalQueryString")  Optional<String> optionalQueryString, 
	                    @QueryParam("optionalQueryLong")  Optional<Long> optionalQueryLong, 
	                    @QueryParam("optionalQueryDate") @ApiParam(format="date")  Optional<OffsetDateTime>  optionalQueryDate, 
	                    @QueryParam("optionalQueryUUID") Optional<UUID> optionalQueryUUID, 
	                    @HeaderParam("optionalHeaderUUID") Optional<UUID> optionalHeaderUUID,
	                    @QueryParam("optionalQueryEnum") Optional<User.UserType> optionalQueryEnum,
	                    @HeaderParam("optionalHeaderString") Optional<String> optionalHeaderString,
	                    @QueryParam("queryUUID") UUID queryUUID,  
	                    @HeaderParam("headerString") String headerString,
 	                    @QueryParam("queryEnum") User.UserType queryEnum, 
	                    @QueryParam("queryIntegerList")    List<Integer>  queryIntegerList, 
	                    @QueryParam("queryLong")   Long  queryLong
 	                    
	                    )
	{
 			
		Map<String,Object> responseMap = new HashMap<>();
		
		responseMap.put("optionalQueryString", optionalQueryString.orElse(null));
		responseMap.put("optionalQueryLong", optionalQueryLong.orElse(null));
	 	responseMap.put("optionalQueryDate", optionalQueryDate.map(OffsetDateTime::toString).orElse(null));
		responseMap.put("optionalQueryUUID", optionalQueryUUID.map(UUID::toString).orElse(null));
		responseMap.put("optionalHeaderUUID", optionalHeaderUUID.map(UUID::toString).orElse(null));
		responseMap.put("optionalHeaderString", optionalHeaderString.orElse(null));
		responseMap.put("optionalQueryEnum", optionalQueryEnum.orElse(null));
		responseMap.put("queryEnum", queryEnum);
		responseMap.put("queryUUID", queryUUID.toString()); 
		responseMap.put("queryLong", queryLong);
		responseMap.put("pathLong", pathLong);
		responseMap.put("headerString", headerString); 
		responseMap.put("queryIntegerList", queryIntegerList); 
		return response(responseMap).applicationJson(); 
	}
}
