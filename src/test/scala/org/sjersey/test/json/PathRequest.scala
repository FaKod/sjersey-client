package org.sjersey.test.json

import javax.xml.bind.annotation.{XmlAccessorType, XmlAccessType, XmlRootElement}

/**
 * User: FaKod
 * Date: 08.12.2010
 * Time: 11:55:23
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
class PathRequest {
  var order: String = _
  var max_depth:Integer = _
  var uniqueness: String = _
}

object PathRequest {
  def apply(order:String, max_depth:Integer, uniqueness: String) = {
    val pr = new PathRequest
    pr.order = order
    pr.max_depth = max_depth
    pr.uniqueness = uniqueness
    pr
  }
}

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
class GetPath {

  var array:Array[TraversePath] = _
}

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
class TraversePath {
  var start:String = _
  var nodes:Array[String] = _
  var length:Integer = _
  var relationships:Array[String] = _
  var end:String = _
}