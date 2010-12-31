package org.sjersey.test.json.neo4jstuff

import reflect.BeanProperty

/**
 * this case classes used by REST method:
 * http://components.neo4j.org/neo4j-server/0.5-1.2/rest.html#Listing_node_indexes
 *
 * @author Christopher Schmidt
 */

case class GetIndex(@BeanProperty var my_nodes: MyNodes) {
  def this() = this (null)
}

case class MyNodes(@BeanProperty var template: String,
                   @BeanProperty var provider: String,
                   @BeanProperty var `type` : String) {
  def this() = this (null, null, null)
}