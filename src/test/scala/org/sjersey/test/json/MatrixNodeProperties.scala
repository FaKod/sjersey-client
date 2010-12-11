package org.sjersey.test.json

import javax.xml.bind.annotation.{XmlAccessType, XmlAccessorType, XmlRootElement}

/**
 * Created by IntelliJ IDEA.
 * User: christopherschmidt
 * Date: 10.12.10
 * Time: 09:14
 * To change this template use File | Settings | File Templates.
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
class MatrixNodeProperties {
  var name:String = _
 var profession:String = _
}

object MatrixNodeProperties {
  def apply(name:String, profession:String) = {
    val np = new MatrixNodeProperties
    np.name = name
    np.profession = profession
    np
  }
}