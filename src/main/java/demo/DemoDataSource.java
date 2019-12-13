package demo;

import javax.annotation.sql.DataSourceDefinition;


// WORKS
// connection attempt to database:5432, connection can be established successfully
//@DataSourceDefinition(
//    name = "java:global/jdbc/DemoDataSource",
//    className = "org.postgresql.ds.PGSimpleDataSource",
//    serverName = "database",  // set the property
//    portNumber = 5432,        // set the property
//    databaseName = "demo",    // set the property
//    user = "demo",
//    password = "demo")


// FAILS
// connection attempt to localhost:5432. Connection fails, because there is no server on localhost:5432, but on database:5432
@DataSourceDefinition(
    name = "java:global/jdbc/DemoDataSource",
    className = "org.postgresql.ds.PGSimpleDataSource",
    url = "jdbc:postgresql://database:5432/demo",  // only use url, but no serverName, portNumber or databaseName
    user = "demo",
    password = "demo")


// WORKS
// connection attempt to database:5432, connection can be established successfully
// only by setting the serverName, portNumber and databaseName to empty values will make sure that the url property will be used for the database connection
//@DataSourceDefinition(
//    name = "java:global/jdbc/DemoDataSource",
//    className = "org.postgresql.ds.PGSimpleDataSource",
//    url = "jdbc:postgresql://database:5432/demo",  // use the url to configure connection properties
//    user = "demo",
//    password = "demo",
//    serverName = "",    // set it to an empty value
//    portNumber = -1,    // set it to an empty value
//    databaseName = "")  // set it to an empty value


public class DemoDataSource { }
