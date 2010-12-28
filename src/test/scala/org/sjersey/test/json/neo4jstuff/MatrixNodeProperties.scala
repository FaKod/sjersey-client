package org.sjersey.test.json.neo4jstuff

import javax.xml.bind.annotation.{XmlAccessType, XmlAccessorType, XmlRootElement}
import reflect.BeanProperty

/**
 * @author Christopher Schmidt
 */

case class MatrixNodeProperties(@BeanProperty var name:String,
                                @BeanProperty var profession:String) {
  def this() = this(null, null)
}