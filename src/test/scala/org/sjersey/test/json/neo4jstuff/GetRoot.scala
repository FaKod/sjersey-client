package org.sjersey.test.json.neo4jstuff

import reflect.BeanProperty

/**
 * this case classes used by REST method:
 * http://components.neo4j.org/neo4j-server/0.1-1.2.M05/rest.html#Get_root
 *
 * @author Christopher Schmidt
 */

case class GetRoot( @BeanProperty var index:String,
                    @BeanProperty var node:String,
                    @BeanProperty var reference_node:String) {
  def this() = this(null, null, null)
}