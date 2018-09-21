/**
 * 
 */
package io.sinistral.example.controllers;

import static io.sinistral.proteus.server.ServerResponse.response;

import java.nio.ByteBuffer;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.google.common.io.Files;
import com.google.inject.Singleton;

import io.sinistral.example.models.Fortune;
import io.sinistral.example.wrappers.AccessLogWrapper;
import io.sinistral.example.wrappers.DummyWrapper;
import io.sinistral.proteus.annotations.Chain;
import io.sinistral.proteus.server.ServerRequest;
import io.sinistral.proteus.server.ServerResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;


/**
 * @author jbauer
 *
 */

@Api(tags = "examples")
@Path("/examples")
@Produces({MediaType.APPLICATION_JSON}) 
@Consumes({MediaType.WILDCARD}) 
@Singleton
@Chain({DummyWrapper.class,AccessLogWrapper.class})
public class Examples
{  
	  
	
	@GET
	@Path("/hello")
	@Produces({MediaType.TEXT_PLAIN,MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML}) 
	@ApiOperation(value = "Hello world, format depends on request accept header" )
	public ServerResponse<Map<String,String>> helloWorldText(ServerRequest request)
	{ 
		Map<String,String> message = new HashMap<>();
		message.put("message", "Hello, world!");
		
		return response(message);
	}
	
	
	@GET
	@Path("/hello/json")
	@Produces({MediaType.APPLICATION_JSON}) 
	@ApiOperation(value = "Hello world json endpoint" )
	public ServerResponse<Map<String,String>> helloWorldJson(ServerRequest request)
	{ 
		Map<String,String> message = new HashMap<>();
		message.put("message", "Hello, world!");
		
		return response( message ).applicationJson();
	}
	
	@GET
	@Path("/hello/xml")
	@Produces({MediaType.APPLICATION_XML}) 
	@ApiOperation(value = "Hello world xml endpoint" )
	public ServerResponse<Map<String,String>> helloWorldXml(ServerRequest request)
	{ 
		Map<String,String> message = new HashMap<>();
		message.put("message", "Hello, world!");
		
		return response( message ).applicationXml();
	}
	
	@GET
	@Path("/echo/{pathString}/{pathLong}")
	@Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML}) 
	@ApiOperation(value = "Echo various parameters, format depends on request accept header" )
	public ServerResponse<Map<String,Object>> echoParameters(ServerRequest request, 
	                                                         @PathParam("pathString") String pathString, 
	                                                         @PathParam("pathLong") Long pathLong,
	                                                         @QueryParam("query") String query,
	                                                         @QueryParam("optionalQuery") Optional<String> optionalQuery,
	                                                         @HeaderParam("header") String header 
	                                                         )
	{ 
		Map<String,Object> parameters = new HashMap<>();
		parameters.put("pathString", pathString);
		parameters.put("pathLong", pathLong);
		parameters.put("query", query);
		
		optionalQuery.ifPresent( p -> {
			parameters.put("optionalQuery", optionalQuery);

		});
		
		parameters.put("header", header); 
 
		return response(parameters);
	}
	
	
	@PUT
	@Path("/echo/bean")
	@Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML}) 
	@ApiOperation(value = "Echo fortune, format depends on request accept header"  )
	public ServerResponse<Fortune> randomWorld(ServerRequest request, @BeanParam Fortune fortune )
	{ 
		return response(fortune); 
	}
	
	@POST
	@Path("/echo/file")
	@Produces(MediaType.APPLICATION_OCTET_STREAM) 
 	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@ApiOperation(value = "Echo posted file",   httpMethod = "POST" )
	public ServerResponse<ByteBuffer> responseUploadFilePath(ServerRequest request, @FormParam("file") java.nio.file.Path file ) throws Exception
	{  
		return response(ByteBuffer.wrap(Files.toByteArray(file.toFile()))).applicationOctetStream(); 
	}
	 
	
	
	
	@GET
	@Path("/echo/complex")
	@Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML}) 
	@ApiOperation(value = "Complex query parameters, format depends on request accept header", httpMethod = "GET")
	public ServerResponse<Map<String,Object>> complexParameters(
	                    final ServerRequest serverRequest,   
	                    @QueryParam("date") @ApiParam(format="date", defaultValue="2007-12-03T10:15:30+01:00") OffsetDateTime  date,  
	                    @QueryParam("uuid") @ApiParam(defaultValue="b8dacc3b-8d0c-485e-a524-18586b827ce2") UUID uuid,
	                    @QueryParam("fortuneType") Fortune.FortuneType fortuneType, 
	                    @QueryParam("intList") @ApiParam(defaultValue="1,2,3,4")   List<Integer>  intList 
	                    )
	{
 			
		Map<String,Object> parameters = new HashMap<>();
		
		parameters.put("date", date);
		parameters.put("uuid", uuid);
		parameters.put("fortuneType", fortuneType);
		parameters.put("intList", intList); 
		
		return response(parameters);
	}
}
