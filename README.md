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
	
Rest Blocks
-----------

Every Rest using block starts with a rest keyword. With rest(...) you can setup header parameter and additional paths. For example:

	rest { implicit s =>
	}
	
	// with header paramter for every subsequent call
	rest(header = ("MyName1", "1") ::("MyName2", "2") :: Nil) { implicit s =>
	}
	
	// this base path will be appended to the base URI
	rest(basePath = "node/1/") { implicit s =>
	}
	
Calling Rest Methods
--------------------
Rest methods can be called inside a Rest block and following a path string. The principal syntax is:

	"path".XX						// Unit return value and no request entity
	"path".XX[SomeCaseClass]		// return value SomeCaseClass and no request entity
	"path".XX <=(RequestCaseClass)  // Unit return value and RequestCaseClass as request entity
	"path".XX[SomeCaseClass] <=(RequestCaseClass) // all together
	
where XX is one of POST, PUT, DELETE. GET does not support request entities.

ClientResponse
--------------
Expecting an instance of class ClientResponse means: No exception will be thrown. The status of the response can be retrieved from the returned object.
To get the response entity you can use toEntity:

	rest { implicit s =>
	    val cr = "".GET[ClientResponse]
	    val root = cr.toEntity[GetRoot]
	}
	

	