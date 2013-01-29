package eu.fakod

import com.sun.jersey.api.client.WebResource
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.{NullNode, JsonNodeFactory, ArrayNode}

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

    sealed case class JObject(val underlaying: JsonNode) {

      private val _underlaying = if (underlaying == null) JsonNodeFactory.instance.nullNode else underlaying

      def \(fName: String) = _underlaying.get(fName)

      import scala.collection.JavaConversions._

      def \\(fName: String): Seq[JsonNode] = _underlaying.findValues(fName)

      def fNames: Iterator[String] = _underlaying.fieldNames

      def f: List[JField] = _underlaying.fields.toList.map(me => JField(me.getKey, me.getValue))
    }

    case class JArray(val underlaying: JsonNode) {

      private val _underlaying = if (underlaying == null) JsonNodeFactory.instance.nullNode else underlaying

      def elementsAsList = {
        import scala.collection.JavaConversions.asScalaIterator
        val list: Iterator[JsonNode] = _underlaying.asInstanceOf[ArrayNode].elements
        list.toList
      }
    }

    case class JField(val name: String, val value: JsonNode)

  }

}