package org.sjersey.client

/**
 * REST call helper Trait
 *
 * @author Christopher Schmidt
 */
trait RestHandler {
  self: WebResourceBuilderWrapper =>

  /**
   * rest functions
   */
  type RestRequestEntityUnit = Object => Unit
  type RestUnit = () => Unit

  type RestClassRequestEntity[T] = (Class[T], Object) => T
  type RestClass[T] = Class[T] => T

  protected case class RestCall[T](rreu: RestRequestEntityUnit, ru: RestUnit, rcre: RestClassRequestEntity[T], rc: RestClass[T])

  protected def post[T](implicit b: builderType) = RestCall(o => b.post(o), () => b.post, (c: Class[T], o) => b.post(c, o), (c: Class[T]) => b.post(c))

  protected def delete[T](implicit b: builderType) = RestCall(o => b.delete(o), () => b.delete, (c: Class[T], o) => b.delete(c, o), (c: Class[T]) => b.delete(c))

  protected def put[T](implicit b: builderType) = RestCall(o => b.put(o), () => b.put, (c: Class[T], o) => b.put(c, o), (c: Class[T]) => b.put(c))


  /**
   * handles rest calls that return Unit
   */
  protected class HandlerUnit(rc: RestCall[Unit]) {

    def <=(requestEntity: AnyRef): Unit = wrapException {
      rc.rreu(requestEntity)
    }

    def <=(): Unit = wrapException {
      rc.ru()
    }
  }

  /**
   * handles rest calls that return T
   */
  protected class Handler[T: Manifest](rc: RestCall[T]) {

    def <=(requestEntity: AnyRef): T = wrapException {
      val m = manifest[T]
      rc.rcre(m.erasure.asInstanceOf[Class[T]], requestEntity)
    }

    def <=(): T = wrapException {
      val m = manifest[T]
      rc.rc(m.erasure.asInstanceOf[Class[T]])
    }
  }

}

