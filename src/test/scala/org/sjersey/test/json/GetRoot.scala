package org.sjersey.test.json

import javax.xml.bind.annotation.{XmlAccessorType, XmlAccessType, XmlRootElement}

/**
 * User: FaKod
 * Date: 06.12.2010
 * Time: 15:18:54
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
class GetRoot {
  var index:String = _
  var node:String = _
  var reference_node:String = _
}