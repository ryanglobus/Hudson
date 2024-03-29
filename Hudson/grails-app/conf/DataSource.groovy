dataSource {
	pooled = true
	jmxExport = true
	// H2
	//driverClassName = "org.h2.Driver"
	//username = "sa"
	//password = ""
}
hibernate {
	cache.use_second_level_cache = true
	cache.use_query_cache = false
	cache.region.factory_class = 'net.sf.ehcache.hibernate.EhCacheRegionFactory' // Hibernate 3
	//    cache.region.factory_class = 'org.hibernate.cache.ehcache.EhCacheRegionFactory' // Hibernate 4
	singleSession = true // configure OSIV singleSession mode
}

// environment specific settings
environments {
	development {
		dataSource {
			logSql = false
			// if you don't want to wipe data when recompiling, change to update
			dbCreate = "create-drop" // one of 'create', 'create-drop', 'update', 'validate', ''
			// H2
			//url = "jdbc:h2:mem:devDb;MVCC=TRUE;LOCK_TIMEOUT=10000;DB_CLOSE_ON_EXIT=FALSE"
			//driverClassName = "org.h2.Driver"

			// MySQL
			//url = "jdbc:mysql://127.0.0.1:3306/hudson-dev"
			//url = "jdbc:mysql://localhost:3306/hudson-dev?user=manager&password=secret&useUnicode=true&characterEncoding=UTF-8";
			//dialect = "org.hibernate.dialect.MySQL5Dialect"
			//driverClassName = "com.mysql.jdbc.Driver"
			//factory="org.apache.tomcat.jdbc.pool.DataSourceFactory"
	
			// SQLite
			url = "jdbc:sqlite:../hudson-dev.db"
			dialect = "org.hibernate.dialect.SQLiteDialect"
			driverClassName="org.sqlite.JDBC"

			properties {
				maxActive = 1
			}
		}
	}
	test {
		dataSource {
			logSql = false
			dbCreate = "update"
			// H2
			//url = "jdbc:h2:mem:testDb;MVCC=TRUE;LOCK_TIMEOUT=10000;DB_CLOSE_ON_EXIT=FALSE"
			//driverClassName = "org.h2.Driver"

			// MySQL
			//url = "jdbc:mysql://127.0.0.1:3306/hudson-test"
			//dialect = "org.hibernate.dialect.MySQL5Dialect"
			//driverClassName = "com.mysql.jdbc.Driver"

			// SQLite
			url = "jdbc:sqlite:../hudson-test.db"
			dialect = "org.hibernate.dialect.SQLiteDialect"
			driverClassName="org.sqlite.JDBC"
		}
	}
	production {
		dataSource {
			// SQLite
			dbCreate = "update"
			url = "jdbc:sqlite:../hudson-prod.db"
			dialect = "org.hibernate.dialect.SQLiteDialect"
			driverClassName="org.sqlite.JDBC"

			properties {
				maxActive = 1
			}
			// MySQL
			/*
			dbCreate = "update"
			//url = "jdbc:h2:prodDb;MVCC=TRUE;LOCK_TIMEOUT=10000;DB_CLOSE_ON_EXIT=FALSE"
			url = "jdbc:mysql://hudsondb.cyxciiu7qvuv.us-west-2.rds.amazonaws.com:3306/hudsondb"
			username="hudson"
			password="bgkrMDWAMDCA12!"
			dialect = "org.hibernate.dialect.MySQL5Dialect"
			driverClassName = "com.mysql.jdbc.Driver"
			properties {
				// See http://grails.org/doc/latest/guide/conf.html#dataSource for documentation
				jmxEnabled = true
				initialSize = 5
				maxActive = 50
				minIdle = 5
				maxIdle = 25
				maxWait = 10000
				maxAge = 10 * 60000
				timeBetweenEvictionRunsMillis = 5000
				minEvictableIdleTimeMillis = 60000
				validationQuery = "SELECT 1"
				validationQueryTimeout = 3
				validationInterval = 15000
				testOnBorrow = true
				testWhileIdle = true
				testOnReturn = false
				jdbcInterceptors = "ConnectionState"
				defaultTransactionIsolation = java.sql.Connection.TRANSACTION_READ_COMMITTED
			}
			*/
		}
	}
}
