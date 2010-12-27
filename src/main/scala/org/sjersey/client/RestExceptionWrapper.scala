package org.sjersey.client

import RestTypes._

/**
 * @author Christopher Schmidt
 */
private[client] trait IRestExceptionWrapper {

  /**
   * partial function used as exception handler
   */
  def restExceptionHandler: ExceptionHandlerType
}


/**
 *  Wraps Exceptions raised by ResourceBuilder REST method calls
 *
 * @author Christopher Schmidt
 */

private[client] trait RestExceptionWrapper extends IRestExceptionWrapper {

  /**
   * calls restExceptionHandler as default handler
   *
   * @param f function to be applied and wrapped
   */
  def wrapException[T](f: => T):T = {
    try {
      f
    }
    catch {
      case x: Throwable => restExceptionHandler(x).asInstanceOf[T] // asInstanceOf due to type checking error
    }
  }
}