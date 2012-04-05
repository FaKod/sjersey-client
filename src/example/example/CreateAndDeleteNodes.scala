package example

import org.sjersey.client.{SimpleWebResourceProvider, Rest}
import javax.ws.rs.core.MediaType
import com.sun.jersey.api.client.ClientResponse
import org.sjersey.unittest.json.neo4jstuff.MatrixNodeProperties

case class MatrixNodeProperties(name: String, profession: String)

/**
 *
 */
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

    // print them to the console
    locations.foreach(s => println("created nodes: " + s.getPath))

    // and remove them
    for (location <- locations)
      (location.toString).DELETE <=()
  }
}