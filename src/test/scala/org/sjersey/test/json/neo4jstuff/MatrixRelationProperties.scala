package org.sjersey.test.json.neo4jstuff

import reflect.BeanProperty

/**
 * this case classes used by REST method:
 * http://components.neo4j.org/neo4j-server/0.5-1.2/rest.html#Create_relationship
 *
 * @author Christopher Schmidt
 */

case class MatrixRelationProperties (@BeanProperty var to:String,
                                     @BeanProperty var `type`:String) {
  def this() = this(null, null)
}