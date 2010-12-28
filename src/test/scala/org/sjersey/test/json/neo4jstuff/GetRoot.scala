package org.sjersey.test.json.neo4jstuff

import javax.xml.bind.annotation.{XmlAccessorType, XmlAccessType, XmlRootElement}
import reflect.BeanProperty
import org.codehaus.jackson.annotate.JsonProperty

/**
 * @author Christopher Schmidt
 */

case class GetRoot( @BeanProperty var index:String,
                    @BeanProperty var node:String,
                    @BeanProperty var reference_node:String) {
  def this() = this(null, null, null)
}