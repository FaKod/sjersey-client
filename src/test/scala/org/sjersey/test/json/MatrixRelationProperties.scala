package org.sjersey.test.json

import javax.xml.bind.annotation.{XmlAccessType, XmlAccessorType, XmlRootElement}

/**
 * Created by IntelliJ IDEA.
 * User: christopherschmidt
 * Date: 10.12.10
 * Time: 14:40
 * To change this template use File | Settings | File Templates.
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
class MatrixRelationProperties {
  var to:String = _
  var `type`:String = _
}

object MatrixRelationProperties {
  def apply (to:String, `type`:String) = {
    val mrp = new MatrixRelationProperties
    mrp.to = to
    mrp.`type` = `type`
    mrp
  }
}