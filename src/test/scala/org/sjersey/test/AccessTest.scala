package org.sjersey.test

import json.neo4jstuff._
import org.specs.SpecificationWithJUnit
import javax.ws.rs.core.MediaType
import org.sjersey.client.{SimpleWebResourceProvider, Rest}
//import org.codehaus.jettison.json.JSONArray
import com.sun.jersey.api.client.{UniformInterfaceException, ClientResponse}

/**
 * @author Christopher Schmidt
 */

class AccessTest extends SpecificationWithJUnit with Rest with SimpleWebResourceProvider {

  // base location of Neo4j server instance
  override def baseUriAsString = "http://localhost:7474/db/data/"

  // all subsequent REST calls should use JSON notation
  override val mediaType = Some(MediaType.APPLICATION_JSON)

  // yes I want so see HTTP logging output
  override def enableLogFilter = true

  /**
   * yes restExceptionHandler is my default exception handler
   * and it has to throw an exception
   * exTest is for testing
   */
  var exTest = false

  override def restExceptionHandler: (Throwable => Unit) = {
    case e: UniformInterfaceException => {
      println("This is a UniformInterfaceException: " + e)
      exTest = true
      throw e
    }
  }

  "A DELETE / POST" should {

    "create new nodes (POST)" in {
      rest{
        implicit s =>

        // defining note names and profession
        val nodes = ("Mr. Andersson", "Hacker") ::
          ("Morpheus", "Hacker") ::
          ("Trinity", "Hacker") ::
          ("Cypher", "Hacker") ::
          ("Agent Smith", "Program") ::
          ("The Architect", "Whatever") :: Nil

        // for all notes
        val locations =
          for (node <- nodes;
               // create node
               cr = "node".POST[ClientResponse] <= MatrixNodeProperties(name = node._1, profession = node._2)
               // if creation was successful use yield
               if (cr.getStatus == ClientResponse.Status.CREATED.getStatusCode)
          // yield all created locations
          ) yield cr.getLocation

        // print them to the console
        locations.foreach(s => println("created nodes: " + s.getPath))

        // and remove them
        for (location <- locations) {
          // the unary ! is used to sign a absolute path (location here)
          val cr = (!location.toString).DELETE[ClientResponse]
          // no exception and No Content means successful
          cr.getStatus mustEqual ClientResponse.Status.NO_CONTENT.getStatusCode
        }

      }
    }
  }

  "A get call" should {
    "return a value" in {

      rest(header = ("MyName1", "1") :: ("MyName2", "2") :: Nil) {
        implicit s =>

        val root = "".GET[GetRoot]
        val index = "/index".GET[GetIndex]

        root must notBeNull
        root.index must notBeNull
        root.index.length must beGreaterThan(0)

        index.node must notBeNull
        index.node.length must beGreaterThan(0)

        println(root.index + " " + root.node)
        println(if (index.node != null) index.node(0).template)
      }
    }
  }

  "A POST call" should {
    "return the path of node 3" in {
      rest{
        implicit s =>

        val path = "node/3/traverse/path".POST[Array[TraversePath]] <= PathRequest(order = "depth first", max_depth = 4, uniqueness = "node path")

        path must notBeNull
        path.length must beGreaterThan(0)

        println("Array length: " + path.length)
        path.foreach( tp => println("TraversePath: " + tp.toString))
      }
    }
  }

  "A PUT call" should {
    "set the properties of node 1" in {
      rest{
        implicit s =>

          "node/1/properties".PUT <= MatrixNodeProperties(name = "Thomas Anderson Neo", profession = "Hacker")

        val properties = "node/1/properties".GET[MatrixNodeProperties]

        properties.name must notBeNull
        properties.name.length must beGreaterThan(0)
        properties.name must equalIgnoreCase("Thomas Anderson Neo")
      }
    }

    "set the properties of node 1 with basePath" in {
      rest(basePath = "node/1/") {
        implicit s =>

          "properties".PUT <= MatrixNodeProperties(name = "Thomas Anderson", profession = "Hacker")

        val properties = "properties".GET[MatrixNodeProperties]

        properties.name must notBeNull
        properties.name.length must beGreaterThan(0)
        properties.name must equalIgnoreCase("Thomas Anderson")

      }
    }
  }

  "exeption handling" should {
    "be overwritable" in {

      rest{
        implicit s =>

          try {
            "NotValid".GET[GetRoot]
          }
          catch {
            case _ => exTest must beTrue
          }
      }

    }
  }
}