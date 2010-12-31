package org.sjersey.client

import RestTypes._
import com.sun.jersey.api.client.ClientResponse


/**
 * WebResource#Builder wrapper factory
 *
 * @author Christopher Schmidt
 */
private[client] object WebResourceBuilderWrapper {
  /**
   * @see WebResourceBuilderWrapper
   */
  def apply(restExceptionHandler: ExceptionHandlerType, builder: BuilderFuncType, settings: RestCallSettings, path: String = "") =
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
class WebResourceBuilderWrapper(restExcHandler: ExceptionHandlerType,
                                builder: BuilderFuncType,
                                settings: RestCallSettings,
                                path: String = "") extends RestExceptionWrapper {

  /**
   * overwriting restExceptionHandler method of RestExceptionWrapper
   */
  override def restExceptionHandler = restExcHandler

  // local store to use absolute path @see unary_!
  private var absPath = false

  // applying builder function
  private def b = builder(path, settings, absPath)

  /**
   * ! sets the flag for absolute path usage
   */
  def unary_! = {
    absPath = true
    this
  }

  /**
   * PUT method call. For PUT methods without returning an object T has to be Unit
   *
   * @param requestEntity request entity to send to server
   */
  def PUT[T: ClassManifest](requestEntity: AnyRef): T = {
    wrapException{
      val m = implicitly[ClassManifest[T]]

      if (m.erasure.isInstanceOf[Class[Unit]])
        b.put(requestEntity.asInstanceOf[Object]).asInstanceOf[T]
      else
        b.put(m.erasure.asInstanceOf[Class[T]], requestEntity)
    }
  }

  /**
   * GET method call
   *
   * @throws UniformInterfaceException if the status of the HTTP response is
   *         greater than or equal to 300 and <code>c</code> is not the type
   */
  def GET[T: ClassManifest]: T = {
    wrapException{
      val m = implicitly[ClassManifest[T]]
      b.get(m.erasure.asInstanceOf[Class[T]])
    }
  }

  /**
   * GET method call using ClientResponse
   * means that no exception is thrown on non 200 status codes
   * check ClientResponse.getStatus manually
   *
   * @throws UniformInterfaceException if the status of the HTTP response is
   *         greater than or equal to 300 and <code>T</code> is not the type
   * @throws ClientHandlerException if there is an error processing the response.
   * @throws UniformInterfaceException if the response status is 204 (No Contnet).
   */
  def GETcr[T: ClassManifest]: (ClientResponse, T) = {
    wrapException{
      val m = implicitly[ClassManifest[T]]
      val cr = b.get(classOf[ClientResponse])
      (cr, cr.getEntity(m.erasure.asInstanceOf[Class[T]]))
    }
  }

  /**
   * DELETE method call
   */
  def DELETE[T: ClassManifest]: T = {
    wrapException{
      val m = implicitly[ClassManifest[T]]
      b.delete(m.erasure.asInstanceOf[Class[T]])
    }
  }

  /**
   * POST method call
   *
   * @param requestEntity request entity to send to server
   */
  def POST[T: ClassManifest](requestEntity: AnyRef): T = {
    wrapException{
      val m = implicitly[ClassManifest[T]]
      b.post(m.erasure.asInstanceOf[Class[T]], requestEntity)
    }
  }

  /**
   * methods to allow the <= "operator" to attach Request Entities
   */
  def POST[T: ClassManifest]: POSTHelper[T] = new POSTHelper[T](this)

  def PUT[T: ClassManifest]: PUTHelper[T] = new PUTHelper[T](this)

  def PUT: PUTHelper[Unit] = new PUTHelper[Unit](this)

  /**
   *  POST helper to allow the use of the <= "operator"
   */
  class POSTHelper[T: ClassManifest](w: WebResourceBuilderWrapper) {
    def <=(ar: AnyRef) = w.POST[T](ar)
  }


  /**
   * PUT helper to allow the use of the <= "operator"
   */
  class PUTHelper[T: ClassManifest](w: WebResourceBuilderWrapper) {
    def <=(ar: AnyRef) = w.PUT[T](ar)
  }

}


/**
 * case class to store all settings while in a <code>rest  { } </code> loop
 *
 * @author Christopher Schmidt
 *
 * @param basePath to be appended to all subsequent rest calls
 * @param header List of header parameter
 */
case class RestCallSettings(basePath: String, header: List[(String, String)])

