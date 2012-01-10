package org.sjersey.client

import com.sun.jersey.api.client.{ClientResponse, WebResource}
import java.net.URI

/**
 *
 * @author Christopher Schmidt
 * Date: 28.09.11
 * Time: 06:29
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
  protected def getWebResourceFromAbsURI(absPath: String): WebResource

  /**
   *  multiple Media Types as List of Strings
   */
  protected val mediaType: List[String] = Nil

  /**
   * function, if applied with path and settings, returns WebResource#Builder
   */
  private def builder: BuilderFuncType = {
    (path, settings, absPath) =>
      var wr =
        if (absPath)
          getWebResourceFromAbsURI(path)
        else
          webResource.path(settings.basePath).path(path)

      settings.query.foreach {
        case (k, v) => wr = wr.queryParam(k, v)
      }

      val requestBuilder = wr.getRequestBuilder

      mediaType.foreach(x => requestBuilder.accept(x).`type`(x))

      settings.header.foreach(x => requestBuilder.header(x._1, x._2))

      requestBuilder
  }

  /**
   * implicit conversion to support "path".method(...) stuff
   * @param path path to add to this specific REST call
   */
  implicit def restPathStringToWRM(path: String)(implicit settings: RestCallContext): WebResourceBuilderWrapper =
    WebResourceBuilderWrapper(restExceptionHandler, builder, settings, path)

  /**
   * implicit conversion to convert a ClientResponse to an Entity
   * if none T is given java.lang.Object is used
   * @param cr ClientResponse instance
   */
  implicit def clientResponseToEntity(cr: ClientResponse) = new {
    def toEntity[T: Manifest] = cr.getEntity(manifest[T].erasure.asInstanceOf[Class[T]])
  }

  /**
   * converts to RichClientResponse
   *
   */
  implicit def toRichClientResponse(cr: ClientResponse) = new RichClientResponse(cr)

  /**
   * main method enclosing the REST calls
   * @param header header name value field to add to HTTP header
   * @param basePath path to add to all subsequent rest calls
   */
  def rest[A](header: List[(String, String)] = Nil, basePath: String = "", query: List[(String, String)] = Nil)(f: (RestCallContext) => A): A = {
    f(RestCallContext(basePath, header, query))
  }

  /**
   * needed to allow omitting of parenthesis
   */
  def rest[A](f: (RestCallContext) => A): A = rest()(f)

  /**
   * implicit call of ClientResponse.getLocation:URI
   * @param ClientResponse f.e. returned from a POST call
   * @returned URI URI of the newly created entity
   */
  implicit def clientResponseToLocationURI(cr: ClientResponse): URI = cr.getLocation
}