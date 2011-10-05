package org.sjersey

import com.sun.jersey.api.client.{WebResource}

/**
 *
 * @author Christopher Schmidt
 * Date: 29.09.11
 * Time: 06:14
 */

package object client {

  /**
   * class WebResource containes class Builder
   */
  type builderType = WebResource#Builder

  /**
   * function type of creating a instance of class Builder
   */
  type BuilderFuncType = (String, RestCallContext, Boolean) => builderType

  /**
   * function type of REST exception handler
   */
  type ExceptionHandlerType = (Throwable => Unit)
}