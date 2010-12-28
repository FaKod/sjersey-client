package org.sjersey.test.json.neo4jstuff

import javax.xml.bind.annotation.{XmlAccessorType, XmlAccessType, XmlRootElement}
import java.{lang => jl}
import reflect.BeanProperty

/**
 * @author Christopher Schmidt
 */

case class PathRequest (@BeanProperty var order: String,
                        @BeanProperty var max_depth:jl.Integer,
                        @BeanProperty var uniqueness: String) {
  def this() = this(null, 0, null)
}

case class TraversePath (@BeanProperty var start:String,
                         @BeanProperty var nodes:Array[String],
                         @BeanProperty var length:jl.Integer,
                         @BeanProperty var relationships:Array[String],
                         @BeanProperty var end:String) {
  def this() = this(null, null, null, null, null)
}