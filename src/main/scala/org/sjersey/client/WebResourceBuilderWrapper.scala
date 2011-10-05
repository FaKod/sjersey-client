package org.sjersey.client

import com.sun.jersey.api.client.ClientResponse
import java.net.URI


/**
 * WebResource#Builder wrapper factory
 *
 * @author Christopher Schmidt
 */
private[client] object WebResourceBuilderWrapper {
  /**
   * @see WebResourceBuilderWrapper
   */
  def apply(restExceptionHandler: ExceptionHandlerType, builder: BuilderFuncType, settings: RestCallContext, path: String = "") =
    new WebResourceBuilderWrapper(restExceptionHandler: ExceptionHandlerType, builder, settings, path)
}


/**
 * WebResource#Builder wrapper with settings and path that is added to basePath from settings
 * and to provide ClassManifest functionality to omit these annoying .class Java stuff
 *
 * @author Christopher Schmidt
 *
 * @param restExcHandler default rest exception handler @see RestExceptionWrapper
 * @param builder function for be applied on every REST method call
 * @param settings the settings for <code>every rest {}</code> block
 * @param path path to be applied
 *
 */
private[client] class WebResourceBuilderWrapper(restExcHandler: ExceptionHandlerType,
                                                builder: BuilderFuncType,
                                                settings: RestCallContext,
                                                path: String) extends RestExceptionWrapper with RestHandler {

  // stores path as URI
  private val uri = new URI(path)

  /**
   * overwriting restExceptionHandler method of RestExceptionWrapper
   */
  override def restExceptionHandler = restExcHandler

  /**
   * applying builder function
   */
  private implicit def b = builder(path, settings, uri.isAbsolute)

  def POST[T: Manifest]() = new Handler[T](post)

  def POST = new HandlerUnit(post)

  def PUT[T: Manifest]() = new Handler[T](put)

  def PUT = new HandlerUnit(put)

  def DELETE[T: Manifest]() = new Handler[T](delete)

  def DELETE = new HandlerUnit(delete)

  def GET[T: Manifest](): T = wrapException {
    val m = manifest[T]
    b.get(m.erasure.asInstanceOf[Class[T]])
  }

  def GET: ClientResponse = wrapException {
    b.get(classOf[ClientResponse])
  }

}


/**
 * case class to store all settings while in a <code>rest  { } </code> loop
 *
 * @param basePath to be appended to all subsequent rest calls
 * @param header List of header parameter
 */
case class RestCallContext(basePath: String, header: List[(String, String)])

