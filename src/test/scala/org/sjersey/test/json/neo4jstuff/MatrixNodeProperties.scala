package org.sjersey.test.json.neo4jstuff

import reflect.BeanProperty

/**
 * this case classes used by REST method:
 * http://components.neo4j.org/neo4j-server/0.1-1.2.M05/rest.html#Create_node_with_properties
 *
 * @author Christopher Schmidt
 */

case class MatrixNodeProperties(@BeanProperty var name:String,
                                @BeanProperty var profession:String) {
  def this() = this(null, null)
}