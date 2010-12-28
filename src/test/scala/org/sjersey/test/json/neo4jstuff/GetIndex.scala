package org.sjersey.test.json.neo4jstuff

import javax.xml.bind.annotation.{XmlElement, XmlAccessorType, XmlAccessType, XmlRootElement}

/**
 * @author Christopher Schmidt
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
class GetIndex {

  var node:Array[Node] = _
}

@XmlAccessorType(XmlAccessType.FIELD)
class Node {

  var template:String = _
  var `type`:String = _
}