package org.sjersey.test.json.polymorphic

import org.codehaus.jackson.annotate.JsonTypeInfo
import reflect.BeanProperty
import java.{lang => jl}

/**
 * test of a polymorphic class like shown here:
 * http://wiki.fasterxml.com/JacksonPolymorphicDeserialization
 *
 * the JsonTypeInfo annotation here means:
 * Type identifier is to be included as a (meta-)property, along with regular data properties
 * MINIMAL_CLASS (relative Java class name, if base class and sub-class are in same package, leave out package name)
 *
 * @author Christopher Schmidt
 */

@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
abstract class Animal

case class Dog(@BeanProperty var barkVolume: Double) extends Animal {
  def this() = this (0)
}

case class Cat(@BeanProperty var likesCream: Boolean,
               @BeanProperty var lives: jl.Integer) extends Animal {
  def this() = this (false, 0)
}
