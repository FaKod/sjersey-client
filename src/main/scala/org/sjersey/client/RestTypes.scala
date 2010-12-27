package org.sjersey.client

import com.sun.jersey.api.client.WebResource

/**
 * common type aliases for use in this package
 *
 * @author Christopher Schmidt
 */
private[client] object RestTypes {
  /**
   * class WebResource containes class Builder
   */
  type builderType = WebResource#Builder

  /**
   * function type of creating a instance of class Builder
   */
  type BuilderFuncType = (String, RestCallSettings, Boolean) => builderType

  /**
   * function type of REST exception handler
   */
  type ExceptionHandlerType = (Throwable => Unit)
}