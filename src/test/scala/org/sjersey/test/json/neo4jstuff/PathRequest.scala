package org.sjersey.test.json.neo4jstuff

import javax.xml.bind.annotation.{XmlAccessorType, XmlAccessType, XmlRootElement}
import java.{lang => jl}

/**
 * @author Christopher Schmidt
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
case class PathRequest (order: String,max_depth:jl.Integer, uniqueness: String) {
  def this() = this(null, 0, null)
}

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
class GetPath {

  //var array:Array[TraversePath] = _
}

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="")
class TraversePath {
  var start:String = _
  //var nodes:Array[String] = _
  var length:jl.Integer = _
  //var relationships:Array[String] = _
  var end:String = _
}