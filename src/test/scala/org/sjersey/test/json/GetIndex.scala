package org.sjersey.test.json

import javax.xml.bind.annotation.{XmlElement, XmlAccessorType, XmlAccessType, XmlRootElement}

/**
 * User: FaKod
 * Date: 07.12.2010
 * Time: 14:16:28
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