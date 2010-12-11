package org.sjersey.client

import javax.ws.rs.core.UriBuilder
import com.sun.jersey.api.client.{Client, WebResource}
import com.sun.jersey.api.client.filter.LoggingFilter

/**
 * common type aliases for use in this package
 *
 * @author Christopher Schmidt
 */
private[client] object RestTypes {
  type builderType = WebResource#Builder
  type BuilderFuncType = (String, RestCallSettings, Boolean) => builderType
}

import RestTypes._


/**
 * WebResource#Builder wrapper factory
 *
 * @author Christopher Schmidt
 */
private[client] object WebResourceBuilderWrapper {
  /**
   * see WebResourceBuilderWrapper
   */
  def apply(builder: BuilderFuncType, settings: RestCallSettings, path: String = "") =
    new WebResourceBuilderWrapper(builder, settings, path)
}


/**
 * WebResource#Builder wrapper with settings and path that is added to basePath from settings
 * and to provide ClassManifest functionality to omit these annoying .class Java stuff
 *
 * @param builder function for be applied on every REST method call
 * @param settings the settings for <code>every rest {}</code> block
 * @param path path to be applied
 *
 * @author Christopher Schmidt
 */
class WebResourceBuilderWrapper(builder: BuilderFuncType, settings: RestCallSettings, path: String = "") {

  private var absPath = false

   /**
   * ! sets the flag for absolute path usage
   */
  def unary_!  = {
    absPath = true
    this
  }

  /**
   * PUT method call. For PUT methods without returning an object T has to be Unit
   */
  def PUT[T: ClassManifest](requestEntity: AnyRef): T = {
    val m = implicitly[ClassManifest[T]]

    if (m.erasure.isInstanceOf[Class[Unit]])
      builder(path, settings, absPath).put(requestEntity.asInstanceOf[Object]).asInstanceOf[T]
    else
      builder(path, settings, absPath).put(m.erasure.asInstanceOf[Class[T]], requestEntity)
  }


  /**
   * GET method call
   */
  def GET[T: ClassManifest]: T = {
    val m = implicitly[ClassManifest[T]]
    builder(path, settings, absPath).get(m.erasure.asInstanceOf[Class[T]])
  }

  /**
   * DELETE method call
   */
  def DELETE[T: ClassManifest]: T = {
    val m = implicitly[ClassManifest[T]]
    builder(path, settings, absPath).delete(m.erasure.asInstanceOf[Class[T]])
  }

  /**
   * POST method call
   */
  def POST[T: ClassManifest](requestEntity: AnyRef): T = {
    val m = implicitly[ClassManifest[T]]
    builder(path, settings, absPath).post(m.erasure.asInstanceOf[Class[T]], requestEntity)
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
 */
case class RestCallSettings(basePath: String, header: List[(String, String)])

/**
 * basic trait of rest access methods and functions
 *
 * @author Christopher Schmidt
 */
trait Rest {

  /**
   * the WebResource instance
   */
  protected val webResource: WebResource

  /**
   * to create a new WebResource from an absolute Path
   */
  protected def getWebResourceFromAbsURI(absPath:String): WebResource

  /**
   *  Media Type String
   */
  protected val mediaType: Option[String] = None

  /**
   *   function, if applied with path and settings, returns WebResource#Builder
   */
  private def builder: BuilderFuncType = {
    (path, settings, absPath) =>
      val wr =
        if(absPath)
          getWebResourceFromAbsURI(path).getRequestBuilder
        else
          webResource.path(settings.basePath).path(path).getRequestBuilder

      mediaType match {
        case Some(x) => wr.accept(x).`type`(x)
        case _ =>
      }
      settings.header.foreach{
        x => wr.header(x._1, x._2)
      }
      wr
  }

  /**
   * implicit conversion to support "path".method(...) stuff
   * @param path path to add to this specific REST call
   */
  implicit def restPathStringToWRM(path: String)(implicit settings: RestCallSettings): WebResourceBuilderWrapper =
    WebResourceBuilderWrapper(builder, settings, path)

  /**
   * main method enclosing the REST calls
   * @param header header name value field to add to HTTP header
   * @param basePath path to add to all subsequent rest calls
   */
  def rest[A](header: List[(String, String)] = Nil, basePath: String = "")(f: (RestCallSettings) => A): A = {
    f(new RestCallSettings(basePath, header))
  }

  /**
   * needed to allow omitting of parenthesis
   */
  def rest[A](f: (RestCallSettings) => A): A = rest()(f)

  /**
   *  these are here to support direct GET calls (without "path"... before)
   *
   * @TODO make this work ;-) Seems to be double work for now
   */
  /*def GET[T](implicit t: ClassManifest[T], settings: RestCallSettings): T =
    WebResourceBuilderWrapper(builder, settings).GET

  def DELETE[T](implicit t: ClassManifest[T], settings: RestCallSettings): T =
    WebResourceBuilderWrapper(builder, settings).DELETE

  def POST[T](requestEntity: AnyRef)(implicit t: ClassManifest[T], settings: RestCallSettings): T =
    WebResourceBuilderWrapper(builder, settings).POST(requestEntity)

  def PUT[T](requestEntity: AnyRef)(implicit t: ClassManifest[T], settings: RestCallSettings): T =
    WebResourceBuilderWrapper(builder, settings).PUT(requestEntity)

  */

  /**
   * helper methods
   */

  def getLastStringFromPath(s:String) = {
    // @TODO implement this
  }
}

/**
 * provider trait for the instance of WebResource used in class rest
 *
 * @author Christopher Schmidt
 */
trait SimpleWebResourceProvider {

  /**
   * has to be implemented to return the base URI (host, port, path) as String
   */
  def baseUriAsString: String

  /**
   * has to be overwritten so disable the HTTP logging filter
   */
  def enableLogFilter = true

  private val baseUri = UriBuilder.fromUri(baseUriAsString).build()
  private val client = Client create

  /**
   * instance of WebResource created with baseUri
   */
  protected val webResource = client resource baseUri


  if (enableLogFilter)
    client.addFilter(new LoggingFilter())

  /**
   *    creates a new WebResource from an absolute URI
   */
  def getWebResourceFromAbsURI(absPath:String) = {
    val uri = UriBuilder.fromUri(absPath).build()
    client resource uri
  }
}