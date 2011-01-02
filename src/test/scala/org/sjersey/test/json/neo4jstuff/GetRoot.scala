package org.sjersey.test.json.neo4jstuff

import reflect.BeanProperty

/**
 * this case classes used by REST method:
 * http://components.neo4j.org/neo4j-server/0.5-1.2/rest.html#Get_root
 *
 * @author Christopher Schmidt
 */

case class GetRoot( @BeanProperty var relationship_index:String,
                    @BeanProperty var node:String,
                    @BeanProperty var extensions_info:String,
                    @BeanProperty var node_index:String,
                    @BeanProperty var reference_node:String,
                    @BeanProperty var extensions:AnyRef) {
  def this() = this(null, null, null, null, null, null)
}