package org.sjersey.client

import com.sun.jersey.api.client.WebResource
import RestTypes._

/**
 * basic trait of rest access methods and functions
 *
 * @author Christopher Schmidt
 */
trait Rest extends IRestExceptionWrapper {

  /**
   * override REST Exception Handler still default here
   */
  override def restExceptionHandler: ExceptionHandlerType = {
    t => throw t
  }

  /**
   *  the WebResource instance
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
   * function, if applied with path and settings, returns WebResource#Builder
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
    WebResourceBuilderWrapper(restExceptionHandler, builder, settings, path)

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
    // @TODO implement this example
  }
}