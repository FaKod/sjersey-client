package org.sjersey.test.json.neo4jstuff

import reflect.BeanProperty

/**
 * @author Christopher Schmidt
 */

case class GetIndex(@BeanProperty var node:Array[Node]) {
  def this() = this(null)
}

case class Node (@BeanProperty var template:String,
                 @BeanProperty var `type`:String) {
  def this() = this(null, null)
}