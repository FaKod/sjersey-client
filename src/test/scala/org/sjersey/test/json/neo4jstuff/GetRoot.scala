package org.sjersey.test.json.neo4jstuff

import javax.xml.bind.annotation.{XmlAccessorType, XmlAccessType, XmlRootElement}

/**
 * @author Christopher Schmidt
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
case class GetRoot(index:String, node:String, reference_node:String) {
  def this() = this(null, null, null)
}