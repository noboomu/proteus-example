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
import javax.ws.rs.DefaultValue;
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

import org.pac4j.core.profile.CommonProfile;
import org.pac4j.undertow.account.Pac4jAccount;

import com.google.common.io.Files;
import com.google.inject.Singleton;

import io.sinistral.example.models.Fortune;
import io.sinistral.example.wrappers.AccessLogWrapper;
import io.sinistral.example.wrappers.DummyWrapper;
import io.sinistral.example.wrappers.SessionWrapper;
import io.sinistral.proteus.annotations.Chain;
import io.sinistral.proteus.server.ServerRequest;
import io.sinistral.proteus.server.ServerResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import io.undertow.security.api.SecurityContext;
import io.undertow.security.idm.Account;
import io.undertow.server.HttpServerExchange;

/**
 * @author jbauer
 *
 */

 
@Tags({@Tag(name = "examples")})
@Path("/examples")
@Produces({MediaType.APPLICATION_JSON}) 
@Consumes({MediaType.WILDCARD}) 
@Singleton
@Chain({DummyWrapper.class,AccessLogWrapper.class,SessionWrapper.class})
public class Examples
{  
	private static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(Examples.class.getCanonicalName());
	 
	@GET
	@Path("/hello")
	@Produces({MediaType.TEXT_PLAIN,MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML}) 
	@Operation(description = "Hello world, format depends on request accept header" )
	public ServerResponse<Map<String,String>> helloWorldText(ServerRequest request)
	{ 
		Map<String,String> message = new HashMap<>();
		message.put("message", "Hello, world!");
		 
		
		return response(message);
	}
	
	@GET
	@Path("/github")
	@Produces(MediaType.APPLICATION_JSON)
	@Operation(description="Github OAuth protected endpoint")
	@SecurityRequirement(name="GithubSecurityWrapper")
	public ServerResponse<List<CommonProfile>> githubAuth(ServerRequest request)
	{ 
		List<CommonProfile> profiles = getProfiles(request.getExchange());
		
		return response(profiles).applicationJson();
	}
	
	
	@GET
	@Path("/hello/json")
	@Produces({MediaType.APPLICATION_JSON}) 
	@Operation(description = "Hello world json endpoint" )
	public ServerResponse<Map<String,String>> helloWorldJson(ServerRequest request)
	{ 
		Map<String,String> message = new HashMap<>();
		message.put("message", "Hello, world!");
		
		return response( message ).applicationJson();
	}
	
	@GET
	@Path("/hello/xml")
	@Produces({MediaType.APPLICATION_XML}) 
	@Operation(description = "Hello world xml endpoint" )
	public ServerResponse<Map<String,String>> helloWorldXml(ServerRequest request)
	{ 
		Map<String,String> message = new HashMap<>();
		message.put("message", "Hello, world!");
		
		return response( message ).applicationXml();
	}
	
	@GET
	@Path("/echo/{pathString}/{pathLong}")
	@Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML}) 
	@Operation(description= "Echo various parameters, format depends on request accept header" )
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
	@Operation(description= "Echo fortune, format depends on request accept header"  )
	public ServerResponse<Fortune> echoFortune(ServerRequest request, @BeanParam Fortune fortune )
	{ 
		return response(fortune); 
	}
	
	
	@GET
	@Path("/bean/{id}")
	@Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML}) 
	@Operation(description = "Retrieve a fortune, format depends on request accept header"  )
	public ServerResponse<Fortune> fortuneById(ServerRequest request, @PathParam("id") Integer id )
	{ 
		Fortune fortune = new Fortune(id, "This is your fortune.");
		return response(fortune); 
	}
	
	@POST
	@Path("/echo/file")
	@Produces(MediaType.APPLICATION_OCTET_STREAM) 
 	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Operation(description = "Echo posted file"  )
	public ServerResponse<ByteBuffer> responseUploadFilePath(ServerRequest request, @FormParam("file") java.nio.file.Path file ) throws Exception
	{  
		return response(ByteBuffer.wrap(Files.toByteArray(file.toFile()))).applicationOctetStream(); 
	}
	 
	
	
	@GET
	@Path("/echo/complex")
	@Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML}) 
	@Operation(description= "Complex query parameters, format depends on request accept header")
	public ServerResponse<Map<String,Object>> complexParameters(
	                    final ServerRequest serverRequest,   
	                    @QueryParam("date") @DefaultValue("2007-12-03T10:15:30+01:00") Optional<OffsetDateTime>  date,  
	                    @QueryParam("uuid") @DefaultValue("b8dacc3b-8d0c-485e-a524-18586b827ce2") UUID uuid,
	                    @QueryParam("fortuneType") Fortune.FortuneType fortuneType, 
	                    @QueryParam("intList") @DefaultValue("[1,2,3,4]")   List<Integer>  intList 
	                    )
	{
 			
		Map<String,Object> parameters = new HashMap<>();
		
		parameters.put("date", date.orElse(OffsetDateTime.now()));
		parameters.put("uuid", uuid);
		parameters.put("fortuneType", fortuneType);
		parameters.put("intList", intList); 
		
		return response(parameters);
	}
	
	private static List<CommonProfile> getProfiles(final HttpServerExchange exchange)
	{
		log.info("getting account profiles");

		Pac4jAccount userAccount = null;

		final SecurityContext securityContext = exchange.getSecurityContext();

		if (securityContext != null)
		{

			log.info("securityContext is not null.");

			final Account account = securityContext.getAuthenticatedAccount();

			if (account instanceof Pac4jAccount)
			{

				log.info("Pac4jAccount is not null.");

				userAccount = (Pac4jAccount) account;
			}
		}

		if (userAccount != null)
		{
			return userAccount.getProfiles();
		}

		return null;
	}
}
