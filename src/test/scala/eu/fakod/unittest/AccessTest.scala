package eu.fakod.unittest

import _root_.java.net.URI
import javax.ws.rs.core.MediaType
import com.sun.jersey.api.client.{UniformInterfaceException, ClientResponse}
import json.neo4jstuff._
import eu.fakod.sjerseyclient.{SimpleWebResourceProvider, Rest}
import org.specs2.mutable.SpecificationWithJUnit
import eu.fakod.test.json.polymorphic.{Cat, Dog}
import com.fasterxml.jackson.databind.JsonNode
import eu.fakod.sjerseyclient.RichJson._
import scala.language.reflectiveCalls

/**
 * @author Christopher Schmidt
 */

class AccessTest extends SpecificationWithJUnit with Rest with SimpleWebResourceProvider {

  sequential

  // base location of Neo4j server instance
  override def baseUriAsString = "http://localhost:7474/db/data/"

  // all subsequent REST calls should use JSON notation
  override val mediaType = MediaType.APPLICATION_JSON :: Nil

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
    case ee: Throwable => throw ee
  }


  "A DELETE / POST" should {

    "create new nodes (POST)" in {
      rest {
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
            val cr = (location.toString).DELETE[ClientResponse] <=()
            // no exception and No Content means successful
            cr.getStatus must beEqualTo(ClientResponse.Status.NO_CONTENT.getStatusCode)
          }

      }
      success
    }
  }

  "A get call" should {

    "return the root node with Header Parameter" in {

      rest {
        implicit s =>

          val cr: ClientResponse = "".GET // this returns a ClientResponse instance

          val root = cr.toEntity[GetRoot]

          cr.headers.size must beGreaterThan(0)

          cr.headers.foreach {
            case (key, value) =>
              println("Header Key: \"" + key + "\" Header Value: \"" + value + "\"")
          }

          root must not beNull

          (root.node_index) must not beEmpty
      }
    }

    "return the root node" in {

      rest(header = ("MyName1", "1") ::("MyName2", "2") :: Nil) {
        implicit s =>

          val root = "".GET[GetRoot]

          println("returnes the AnyRef extensions property as LinkedHashMap: " + root.extensions.getClass
            + " content: " + root.extensions)

          root must not beNull

          root.node_index must not beEmpty
      }
    }

    "create and return the index" in {

      rest {
        implicit s =>

          val cr = "index/node".POST[ClientResponse] <= CreateNodeIndex("favorites")

          cr.getStatus must beEqualTo(ClientResponse.Status.CREATED.getStatusCode)


          try {
            val index = "/index/node/favorites".GET[Array[String]]
            index.size must be_==(0)

            val jsonNode = "/index/node".GET[JsonNode]
            println("returnes the AnyRef properties as LinkedHashMap: " + jsonNode.getClass
              + " content: " + jsonNode)

            (jsonNode \ "favorites" \ "provider").textValue must beEqualTo("lucene")
            jsonNode.fNames.size

          }
          catch {
            case e: UniformInterfaceException => {
              println("Status was: " + e.getResponse.getStatus)
              sys.error("there should be an index here")
            }
          }
      }
      success
    }
  }

  "A POST call" should {
    "return the path of node 1" in {
      rest {
        implicit s =>

          val path = "node/1/traverse/path".POST[Array[TraversePath]] <= PathRequest(order = "depth first", max_depth = 4, uniqueness = "node path")

          path must not beEmpty

          println("Array length: " + path.length)
          path.foreach(tp => println("TraversePath: " + tp.toString))
      }
      success
    }
  }

  "A PUT call" should {

    "set the properties of node 1" in {
      rest {
        implicit s =>

          "node/1/properties".PUT <= MatrixNodeProperties(name = "Thomas Anderson Neo", profession = "Hacker")

          val properties = "node/1/properties".GET[MatrixNodeProperties]

          properties.name must not beEmpty

          properties.name must equalTo("Thomas Anderson Neo")
      }
    }

    "set the properties of node 1 with basePath" in {
      rest(basePath = "node/1/", query = ("query_1", "param_1") ::("query_2", "param_2") :: Nil) {
        implicit s =>

          "properties".PUT <= MatrixNodeProperties(name = "Thomas Anderson", profession = "Hacker")

          val properties = "properties".GET[MatrixNodeProperties]

          properties.name must not beEmpty

          properties.name must beEqualTo("Thomas Anderson")

      }
    }
  }

  "exeption handling" should {
    "be overwritable" in {

      rest {
        implicit s =>

          try {
            "ThisPathIsNotValid".GET[GetRoot]
          }
          catch {
            case _: Throwable => exTest must beTrue
          }
      }
      success
    }
  }

  /**
   * using this to append /properties to a returned location from a node POST
   */
  def properties = new {
    def of(s: URI) = s + "/properties"
  }

  "Dog and Cat nodes" should {
    "be possible to create and read" in {
      rest {
        implicit s =>

          val dog_URI: URI = "node".POST[ClientResponse] <= Dog(name = "Max", barkVolume = 130)
          val cat_URI: URI = "node".POST[ClientResponse] <= Cat(name = "Felix", likesCream = true, lives = 10)

          (properties of dog_URI).GET[Dog] match {
            case dog: Dog => dog.barkVolume must beEqualTo(130)
            //case cat: Cat => sys.error("Animal should not be a Cat")
            case _ => sys.error("Animal neither a Dog nor a Cat")
          }

          (properties of cat_URI).GET[Cat] match {
            case cat: Cat => cat.lives must beEqualTo(10)
            //case dog: Dog => sys.error("animal should not be a Dog")
            case _ => sys.error("Animal neither a Dog nor a Cat")
          }

      }
    }
  }
}