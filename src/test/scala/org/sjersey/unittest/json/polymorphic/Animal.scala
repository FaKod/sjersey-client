package org.sjersey.test.json.polymorphic

import java.{lang => jl}

/**
 * test of a polymorphic class like shown here:
 * http://wiki.fasterxml.com/JacksonPolymorphicDeserialization
 *
 * the JsonTypeInfo annotation here means:
 * Type identifier is to be included as a (meta-)property, along with regular data properties
 * MINIMAL_CLASS (relative Java class name, if base class and sub-class are in same package, leave out package name)
 *
 * Using traits as base "class" for Scalas case classes
 *
 * @author Christopher Schmidt
 */

trait Creature

trait Animal extends Creature {
  val name:String
}

case class Dog(name: String,
               barkVolume: Double) extends Animal

case class Cat(name: String,
               likesCream: Boolean,
               lives: jl.Integer) extends Animal
