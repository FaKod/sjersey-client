package org.sjersey.test.json.neo4jstuff

import reflect.BeanProperty

/**
 * @author Christopher Schmidt
 */

case class MatrixRelationProperties (@BeanProperty var to:String,
                                     @BeanProperty var `type`:String) {
  def this() = this(null, null)
}