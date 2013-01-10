package eu.fakod

import com.sun.jersey.api.client.WebResource
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.{JsonNodeFactory, ArrayNode}

/**
 *
 * @author Christopher Schmidt
 *         Date: 29.09.11
 *         Time: 06:14
 */

package object sjerseyclient {

  /**
   * class WebResource containes class Builder
   */
  type builderType = WebResource#Builder

  /**
   * function type of creating a instance of class Builder
   */
  type BuilderFuncType = (String, RestCallContext, Boolean) => builderType

  /**
   * function type of REST exception handler
   */
  type ExceptionHandlerType = (Throwable => Unit)

  /**
   * for importing JsonNode helper stuff as
   * <code> import eu.fakod.sjerseyclient.RichJson._</code>
   * @todo complete that
   */
  object RichJson {

    implicit def jsonNodeHelper(node: JsonNode): JObject = new JObject(node)

    implicit def arrayNodeHelper(node: ArrayNode): JArray = new JArray(node)

    object JObject {
      def apply(fList: List[JField]): JObject = {
        val jn = JsonNodeFactory.instance.objectNode
        fList.foreach(f => jn.put(f.name, f.value))
        jsonNodeHelper(jn)
      }
    }

    sealed case class JObject(underlaying: JsonNode) {

      def \(fName: String) = underlaying.get(fName)

      import scala.collection.JavaConversions._

      def \\(fName: String): Seq[JsonNode] = underlaying.findValues(fName)

      def fNames: Iterator[String] = underlaying.fieldNames

      def f: List[JField] = underlaying.fields.toList.map(me => JField(me.getKey, me.getValue))
    }

    case class JArray(underlaying: JsonNode) {
      def elementsAsList = {
        import scala.collection.JavaConversions.asScalaIterator
        val list: Iterator[JsonNode] = underlaying.asInstanceOf[ArrayNode].elements
        list.toList
      }
    }

    case class JField(name: String, value: JsonNode)

  }

}