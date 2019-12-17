# faulty url in DataSourceDefinition

This a small sample project to illustrate the faulty behaviour of the @DataSourceDefinition

## Issue

The `@DataSourceDefinition` provides the `url` property, but it will be ignored. If the the url is set to `jdbc:postgres://database:5432/demo`, a connection to `localhost:5432` will be established. The same settings work great by setting them as `serverName`, `portNumber` and `databaseName` directly.

## Demo

The project contains a plain JEE microservice with JPA. During startup the schema-generation creates a single table in the database. The data-source is configured in `src/main/java/demo/DemoDataSource.java`. To demonstrate this issue more easily, docker-compose is used to start postgresql in addition to this demo microservice.

It can be executed by a single command:

```bash
docker-compose down && mvn clean package && docker-compose up --build
```

Since the issue occurs only occasionally, this command might need to be executed multiple times.


## Details

### working example ✅

The properties `serverName`, `portNumber` and `databaseName` are set individually. A connection to postgres at `database:5432` will be established successfully.

```java
@DataSourceDefinition(
    name = "java:global/jdbc/DemoDataSource",
    className = "org.postgresql.ds.PGSimpleDataSource",
    serverName = "database",  // set the property
    portNumber = 5432,        // set the property
    databaseName = "demo",    // set the property
    user = "demo",
    password = "demo")
```


### failing example ❌

The same settings from the working example will be used again, but this time, only the `url` property will be set. The result is a connection attempt to `localhost:5432` instead of `database:5432`

```java
@DataSourceDefinition(
    name = "java:global/jdbc/DemoDataSource",
    className = "org.postgresql.ds.PGSimpleDataSource",
    url = "jdbc:postgresql://database:5432/demo",  // only use url, but no serverName, portNumber or databaseName
    user = "demo",
    password = "demo")
```


### strange example 🔥

The same url-setting is used like in the failing example. But the `serverName`, `portNumber` and `databaseName` are set to empty values. Very strange: This time a connection to `database:5432` will be established successfully. The only difference to the failing example are the mentioned three empty values.

```java
@DataSourceDefinition(
    name = "java:global/jdbc/DemoDataSource",
    className = "org.postgresql.ds.PGSimpleDataSource",
    url = "jdbc:postgresql://database:5432/demo",  // use the url to configure connection properties
    user = "demo",
    password = "demo",
    serverName = "",    // set it to an empty value
    portNumber = -1,    // set it to an empty value
    databaseName = "")  // set it to an empty value
```


## Exception

During startup of Payara, the following Exception will occur in the failing case:

```
microservice_1  | [2019-12-13T14:29:00.071+0000] [] [SEVERE] [] [org.eclipse.persistence.session./file:/tmp/payaramicro-rt7729847129244438567tmp/applications/ROOT/WEB-INF/classes/_demo.ejb] [tid: _ThreadID=1 _ThreadName=main] [timeMillis: 1576247340071] [levelValue: 1000] [[
microservice_1  |
microservice_1  | Local Exception Stack:
microservice_1  | Exception [EclipseLink-4002] (Eclipse Persistence Services - 2.7.4.payara-p2): org.eclipse.persistence.exceptions.DatabaseException
microservice_1  | Internal Exception: java.sql.SQLException: Error in allocating a connection. Cause: Connection could not be allocated because: Connection to localhost:5432 refused. Check that the hostname and port are correct and that the postmaster is accepting TCP/IP connections.
microservice_1  | Error Code: 0
microservice_1  | 	at org.eclipse.persistence.exceptions.DatabaseException.sqlException(DatabaseException.java:318)
microservice_1  | 	at org.eclipse.persistence.sessions.JNDIConnector.connect(JNDIConnector.java:150)
microservice_1  | 	at org.eclipse.persistence.sessions.DatasourceLogin.connectToDatasource(DatasourceLogin.java:172)
microservice_1  | 	at org.eclipse.persistence.internal.sessions.DatabaseSessionImpl.setOrDetectDatasource(DatabaseSessionImpl.java:233)
microservice_1  | 	at org.eclipse.persistence.internal.sessions.DatabaseSessionImpl.loginAndDetectDatasource(DatabaseSessionImpl.java:815)
```
