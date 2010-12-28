package org.sjersey.test.json.neo4jstuff

import javax.xml.bind.annotation.{XmlAccessType, XmlAccessorType, XmlRootElement}

/**
 * @author Christopher Schmidt
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
case class MatrixRelationProperties (to:String,`type`:String) {
  def this() = this(null, null)
}