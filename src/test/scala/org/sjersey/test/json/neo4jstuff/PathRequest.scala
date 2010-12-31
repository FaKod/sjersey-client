package org.sjersey.test.json.neo4jstuff

import java.{lang => jl}
import reflect.BeanProperty

/**
 * this case classes used by REST method:
 * http://components.neo4j.org/neo4j-server/0.5-1.2/rest.html#Traverse
 *
 * @author Christopher Schmidt
 */

case class PathRequest (@BeanProperty var order: String,
                        @BeanProperty var max_depth:jl.Integer,
                        @BeanProperty var uniqueness: String) {
  def this() = this(null, 0, null)
}

/**
 * path traversal response
 */
case class TraversePath (@BeanProperty var start:String,
                         @BeanProperty var nodes:Array[String],
                         @BeanProperty var length:jl.Integer,
                         @BeanProperty var relationships:Array[String],
                         @BeanProperty var end:String) {
  def this() = this(null, null, null, null, null)
}