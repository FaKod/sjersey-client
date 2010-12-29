package org.sjersey.test.json.neo4jstuff

import reflect.BeanProperty

/**
 * this case classes used by REST method:
 * http://components.neo4j.org/neo4j-server/0.1-1.2.M05/rest.html#Listing_indexes
 *
 * @author Christopher Schmidt
 */

case class GetIndex(@BeanProperty var node:Array[Node]) {
  def this() = this(null)
}

case class Node (@BeanProperty var template:String,
                 @BeanProperty var `type`:String) {
  def this() = this(null, null)
}