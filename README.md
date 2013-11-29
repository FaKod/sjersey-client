Scala Jersey Client
==================

Some convenience methods to use the Jersey Client API with Scala.

Please look at [A REST Client with Jersey and Scala](http://blog.fakod.eu/2010/12/10/yet-another-trya-rest-client-with-jersey-and-scala/) as well

Building
--------

Start the [Neo4j Server 1.2](http://neo4j.org/download/) which will be used for tests only

```bash
    $ git clone git://github.com/FaKod/sjersey-client.git
    $ cd sjersey-client
    $ mvn clean install
```

Or fetch it with Maven (the Sonatype Maven Repo is only needed if you want to use a SNAPSHOT version):

```xml
<repositories>
  <repository>
    <id>sonatype-snapshots</id>
    <url>https://oss.sonatype.org/content/repositories/snapshots/</url>
  </repository>
  ...
</repositories>

<dependencies>
  <dependency>
    <groupId>org.scala-libs</groupId>
    <artifactId>sjersey-client</artifactId>
    <version>0.3.3-SNAPSHOT</version>
  </dependency>
</dependencies>
```

Troubleshooting
---------------

Please consider using [Github issues tracker](https://github.com/FaKod/sjersey-client/issues) to submit bug reports or feature requests.

##Versions:

### 0.3.2
* prepared for maven release
* bumped versions (Scala, Jackson etc.)
* removed dependency to sjersey

### 0.3.0-SNAPSHOT
* last version for Scala 2.9.2

Using this library
==================

Rest Blocks
-----------

Every Rest using block starts with a rest keyword. With rest(...) you can setup header parameter and additional paths. 

Possible parameter for the call to rest are:

Name | Type | Description
------------ | ------------- | ------------
header | List[(String, String)]  | header parameter to provide
basePath | String  | append this to the path
query | List[(String, String)] | query parameter
cType | List[String] | content type setting per request
cAccept | List[String]| accept per setting 

<br>

For example:

```scala
	// no extra settings
	rest { 
		implicit s =>
	}
	
	// with header parameter for every subsequent call
	rest(header = ("MyParam1", "1") ::("MyParam2", "2") :: Nil) { 
		implicit s =>
	}
	
	// this base path will be appended to the base URI
	rest(basePath = "node/1/") { 
		implicit s =>
	}

	// with query parameter for all subsequent calls
	rest(query = ("query_1", "param_1") ::("query_2", "param_2") :: Nil) { 
		implicit s =>
    }
```    

Calling Rest Methods
--------------------
Rest methods can be called inside a Rest block and following a path string. The principal syntax is:

```scala
	"path".XX <=()                                  // Unit return value and no request entity
	"path".XX[SomeCaseClass] <=()                   // return instance of type SomeCaseClass and no request entity
	"path".XX <=(myRequestCaseClass)                // Unit return value and instance myRequestCaseClass as request entity
	"path".XX[SomeCaseClass] <=(myRequestCaseClass) // all
```
	
where XX is one of POST, PUT, DELETE. 
GET does not support request entities:

```scala
	"path".GET
	"path".GET[SomeCaseClass]
```

ClientResponse
--------------
Expecting an instance of class ClientResponse means: No exception will be thrown. The status of the response can be retrieved from the returned object.
To get the response entity you can use toEntity:

```scala
	rest { implicit s =>
	    val cr = "".GET[ClientResponse]
	    //convert cr to a instance of SomeCaseClass
	    val root = cr.toEntity[SomeCaseClass] 
	}
```

webResource Provider
-------------------

Trait Rest contains the GET, POST, PUT and DELETE Methods. To get it work it needs a webResource instance which is provided by trait SimpleWebResourceProvider. The current implementation uses the Jersey version of an Apache Http Client. Its specific configuration can be changed with method doConfig.
Base URI and MimeType can be setup like this:

```scala
    class AccessTest extends Rest with SimpleWebResourceProvider {
		// base location of Neo4j server instance
		override def baseUriAsString = "http://localhost:7474/db/data/"

		// all subsequent REST calls should use JSON notation
		override val mediaType = MediaType.APPLICATION_JSON :: Nil
	}
```
	
Overwriteable methods for **SimpleWebResourceProvider**:

```scala
      /**
       * allows to add some Classes to be added to configuration by config.getClasses.add
       */
       protected def addClasses: List[Class[_]] = Nil
	
	  /**
	   * client configuration parameter like
	   * (ApacheHttpClientConfig.PROPERTY_PREEMPTIVE_AUTHENTICATION, java.lang.Boolean.TRUE)
	   * (ApacheHttpClientConfig.PROPERTY_HANDLE_COOKIES, java.lang.Boolean.TRUE)
	   */
	  def getApacheHttpClientConfig: List[(String, AnyRef)] = Nil
	
	  /**
	   * allows some custom configuration
	   * called after getApacheHttpClientConfig config and
	   * before creation of the client
	   */
	  def doConfig(c: DefaultApacheHttpClientConfig): Unit = {}
	
	  /**
	   * has to be implemented to return the base URI (host, port, path) as String
	   */
	  def baseUriAsString: String
	
	  /**
	   * has to be overwritten so disable the HTTP logging filter
	   */
	  def enableLogFilter = true
```
	  
Overwriteable methods for **Rest**:

```scala	  
	  /**
	   * override REST Exception Handler still default here
	   */
	  override def restExceptionHandler: ExceptionHandlerType = {
	    t => throw t
	  }
		
	  /**
	   *  multiple Media Types as List of Strings
	   */
	  protected val mediaType: List[String] = Nil
```	  
	
Scala Case Class Marshaling
---------------------------

sjersey-client uses Jerkson JSON to/from Scala Case Class marshaling. So it is possible to use

```scala
	case class MatrixNodeProperties(name: String, profession: String)
```
	
as JSON object like this:

```scala
	"node".POST[ClientResponse] <= MatrixNodeProperties(name = "Neo", profession = "Hacker")
```
	
Traverse Path call is defined by

```scala
	case class TraversePath(start:String, nodes:List[String], length:Int, relationships:List[String], end:String)
```

is filled with the following JSON:

```json
	[ {
	  "start" : "http://localhost:7474/db/data/node/1",
	  "nodes" : [ "http://localhost:7474/db/data/node/1", "http://localhost:7474/db/data/node/3" ],
	  "length" : 1,
	  "relationships" : [ "http://localhost:7474/db/data/relationship/6" ],
	  "end" : "http://localhost:7474/db/data/node/3"
	}, {
	  "start" : "http://localhost:7474/db/data/node/1",
	  "nodes" : [ "http://localhost:7474/db/data/node/1", "http://localhost:7474/db/data/node/2" ],
	  "length" : 1,
	  "relationships" : [ "http://localhost:7474/db/data/relationship/1" ],
	  "end" : "http://localhost:7474/db/data/node/2"
	}, {
	  "start" : "http://localhost:7474/db/data/node/1",
	  "nodes" : [ "http://localhost:7474/db/data/node/1", "http://localhost:7474/db/data/node/0" ],
	  "length" : 1,
	  "relationships" : [ "http://localhost:7474/db/data/relationship/0" ],
	  "end" : "http://localhost:7474/db/data/node/0"
	} ]
```	
		
Example Creating Neo4j Nodes
----------------------------

The following code creates 6 Nodes in Neo4j Server and deletes them again.

```scala
	object CreateAndDeleteNodes extends App with Rest with SimpleWebResourceProvider {

	  // base location of Neo4j server instance
	  override def baseUriAsString = "http://localhost:7474/db/data/"

	  // all subsequent REST calls should use JSON notation
	  override val mediaType = Some(MediaType.APPLICATION_JSON)

	  // yes I want so see HTTP logging output
	  override def enableLogFilter = true

	  rest {
	    implicit s =>

	    // defining node names and profession
	    val nodes = ("Mr. Andersson", "Hacker") ::
	      ("Morpheus", "Hacker") ::
	      ("Trinity", "Hacker") ::
	      ("Cypher", "Hacker") ::
	      ("Agent Smith", "Program") ::
	      ("The Architect", "Whatever") :: Nil

	    // for all notes
	    val locations =
	      for (_@(name, prof) <- nodes;
	           // create node
	           cr = "node".POST[ClientResponse] <= MatrixNodeProperties(name, prof)
	           // if creation was successful use yield
	           if (cr.getStatus == ClientResponse.Status.CREATED.getStatusCode)
	      // yield all created locations
	      ) yield cr.getLocation

	    // print them to console
	    locations.foreach(s => println("created node path: " + s.getPath))

	    // and remove them
	    for (location <- locations) 
			(location.toString).DELETE <=()
	  }
	}
```
	
	