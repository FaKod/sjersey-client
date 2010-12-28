package org.sjersey.test.json.neo4jstuff

import reflect.BeanProperty

/**
 * @author Christopher Schmidt
 */

case class GetRoot( @BeanProperty var index:String,
                    @BeanProperty var node:String,
                    @BeanProperty var reference_node:String) {
  def this() = this(null, null, null)
}