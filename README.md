Scala Jersey Client
==================

Some convenience methods to use the Jersey Client API with Scala.

Please look at [A REST Client with Jersey and Scala](http://blog.fakod.eu/2010/12/10/yet-another-trya-rest-client-with-jersey-and-scala/) as well

Building
--------

Start the [Neo4j Server 1.2](http://neo4j.org/download/) which will be used for tests only

    $ git clone git://github.com/FaKod/sjersey-client.git
    $ cd sjersey-client
    $ mvn clean install

Or try to maven fetch it with a Github Maven Repo:

    <repositories>
      <repository>
        <id>fakod-releases</id>
        <url>https://raw.github.com/FaKod/fakod-mvn-repo/master/releases</url>
      </repository>
    </repositories>

    <dependencies>
      <dependency>
        <groupId>org.scala-libs</groupId>
        <artifactId>sjersey-client</artifactId>
        <version>0.0.1</version>
      </dependency>
    </dependencies>

Troubleshooting
---------------

Please consider using [Github issues tracker](https://github.com/FaKod/sjersey-client/issues) to submit bug reports or feature requests.


Using this library
==================

webResource Provider
-------------------

Trait Rest contains the GET, POST, PUT and DELETE Methods. To work it needs a webResource instance which is provided by trait SimpleWebResourceProvider. The current implementation uses the Jersey version of an Apache Http Client. Its specific configuration can be changed with method doConfig.
Base URI and MimeType can be setup like this:


    class AccessTest extends Rest with SimpleWebResourceProvider {
		// base location of Neo4j server instance
		override def baseUriAsString = "http://localhost:7474/db/data/"

		// all subsequent REST calls should use JSON notation
		override val mediaType = Some(MediaType.APPLICATION_JSON)
	}
	
Scala Case Class Marshaling
---------------------------

sjersey-client uses Jerkson JSON to/from Scala Case Class marshaling. So it is possible to use

	case class MatrixNodeProperties(name: String, profession: String)
	
as JSON object like this:

	"node".POST[ClientResponse] <= MatrixNodeProperties(name = "Neo", profession = "Hacker")
	
Traverse Path call is defined by

	case class TraversePath(start:String, nodes:List[String], length:Int, relationships:List[String], end:String)

is filled with the following JSON:

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
	
	
Rest Blocks
-----------

Every Rest using block starts with a rest keyword. With rest(...) you can setup header parameter and additional paths. For example:

	// no extra settings
	rest { implicit s =>
	}
	
	// with header parameter for every subsequent call
	rest(header = ("MyParam1", "1") ::("MyParam2", "2") :: Nil) { implicit s =>
	}
	
	// this base path will be appended to the base URI
	rest(basePath = "node/1/") { implicit s =>
	}

	// with query parameter for all subsequent calls
	rest(query = ("query_1", "param_1") ::("query_2", "param_2") :: Nil) { implicit s =>
    }
	
Calling Rest Methods
--------------------
Rest methods can be called inside a Rest block and following a path string. The principal syntax is:

	"path".XX <=()					              // Unit return value and no request entity
	"path".XX[SomeCaseClass] <=()		          // return value SomeCaseClass and no request entity
	"path".XX <=(RequestCaseClass)                // Unit return value and RequestCaseClass as request entity
	"path".XX[SomeCaseClass] <=(RequestCaseClass) // all together
	
where XX is one of POST, PUT, DELETE. 
GET does not support request entities:

	"path".GET
	"path".GET[SomeCaseClass]

ClientResponse
--------------
Expecting an instance of class ClientResponse means: No exception will be thrown. The status of the response can be retrieved from the returned object.
To get the response entity you can use toEntity:

	rest { implicit s =>
	    val cr = "".GET[ClientResponse]
	    val root = cr.toEntity[GetRoot]
	}
	
Example Creating Neo4j Nodes
----------------------------

The following code creates 6 Nodes in Neo4j Server and deletes them again.

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
	
	