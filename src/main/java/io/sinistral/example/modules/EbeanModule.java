/**
 * 
 */
package io.sinistral.example.modules;

import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;
import com.typesafe.config.Config;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import io.ebean.EbeanServer;
import io.ebean.EbeanServerFactory;
import io.ebean.config.ServerConfig;

 
/**
 * @author jbauer
 *
 */
public class EbeanModule extends AbstractModule
{

	private static Logger log = LoggerFactory.getLogger(EbeanModule.class.getCanonicalName());

	@Inject
	@Named("mysql.hikaricp.jdbcUrl")
	protected String jdbcUrl;

	@Inject
	@Named("mysql.hikaricp.username")
	protected String username;
	
	@Inject
	@Named("mysql.hikaricp.password")
	protected String password;
	
	@Inject
	@Named("mysql.hikaricp.dataSourceClassName")
	protected String dataSourceClassName;
	
	@Inject
	@Named("mysql.hikaricp.ds.databaseName")
	protected String databaseName;
	
	@Inject
	@Named("mysql.hikaricp.ds.serverName")
	protected String serverName;
	
	@Inject
	@Named("mysql.hikaricp.ds.portNumber")
	protected Integer portNumber;
	
	@Inject
	@Named("mysql.hikaricp.maximumPoolSize")
	protected Integer maximumPoolSize;
	
	@Inject
	@Named("mysql.hikaricp.minimumIdle")
	protected Integer minimumIdle;
	
	@Inject
	@Named("mysql.hikaricp.ds.useServerPrepStmts")
	protected Boolean useServerPrepStmts;
	
	@Inject
	@Named("mysql.hikaricp.ds.cachePrepStmts")
	protected Boolean cachePrepStmts;
	
	@Inject
	@Named("mysql.hikaricp.ds.cacheCallableStmts")
	protected Boolean useCacheCallableStmts;
	
	@Inject
	@Named("mysql.hikaricp.ds.prepStmtCacheSize")
	protected Integer prepStmtCacheSize;
	
	@Inject
	@Named("mysql.hikaricp.ds.prepStmtCacheSqlLimit")
	protected Integer prepStmtCacheSqlLimit;
	

	 
	protected HikariDataSource ds;
	 
	/* (non-Javadoc)
	 * @see io.sinistral.proteus.services.BaseService#configure(com.google.inject.Binder)
	 */

	public void configure()
	{
		log.debug("Binding " + this.getClass().getSimpleName());
		
		HikariConfig config = new HikariConfig();
		 
		config.setJdbcUrl(jdbcUrl);
		config.setUsername(username);
		config.setPassword(password); 
		config.setMinimumIdle(minimumIdle);
		config.setMaximumPoolSize(maximumPoolSize);
		config.addDataSourceProperty("databaseName", databaseName);
		config.addDataSourceProperty("useSSL",false);
		config.addDataSourceProperty("UseServerPrepStmts", useServerPrepStmts);
		config.addDataSourceProperty("CachePrepStmts", cachePrepStmts);
		config.addDataSourceProperty("CacheCallableStmts", useCacheCallableStmts);
		config.addDataSourceProperty("PrepStmtCacheSize", prepStmtCacheSize);
		config.addDataSourceProperty("PrepStmtCacheSqlLimit", prepStmtCacheSqlLimit);

		this.ds = new HikariDataSource(config);
		
		binder().bind(HikariDataSource.class).toInstance(this.ds);
		
		bind(EbeanServer.class).toProvider(EbeanServerProvider.class).asEagerSingleton();

	}
  
 
	public static class EbeanServerProvider implements Provider<EbeanServer> {

		private final DataSource dataSource;
		private final Config config;
		
		@Inject
		public EbeanServerProvider( HikariDataSource dataSource, Config config  )
		{
			this.dataSource = dataSource;
			this.config = config;
		}
		
		  @Override
		  public EbeanServer get() {

		    ServerConfig ebeanConfig = new ServerConfig();
		    ebeanConfig.setName("default");
		    
		    List<String> modelClassNames = config.getStringList("ebean.default");
		    
		   
		    // ebean.transaction.onqueryonly=COMMIT
		    // load configuration from ebean.properties
		   
	 
		    
		    Properties props = new Properties();

		    config.entrySet().stream().filter( e -> e.getKey().indexOf("ebean") > -1 ).forEach(prop -> {
		      Object value = prop.getValue().unwrapped();
		      log.debug("property: " +   prop.getKey());
		      props.setProperty(  prop.getKey(), value.toString());
		    });
		    
		    
		    
		    ebeanConfig.loadFromProperties(props);

		    for( String n : modelClassNames )
		    {
		    	log.debug("Adding " + n + " to ebean classes");
		    	
		    	try
				{
					ebeanConfig.addClass(Class.forName(n));
				} catch (ClassNotFoundException e)
				{
					log.error(e.getMessage(),e);
				}
		    }
		    ebeanConfig.setDefaultServer(true);
		    ebeanConfig.setDataSource(this.dataSource);
		    // other programmatic configuration

		    return EbeanServerFactory.create(ebeanConfig);
		  }
		}
	 

}
