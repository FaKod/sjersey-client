package org.sjersey.client

import com.sun.jersey.api.client.Client
import javax.ws.rs.core.UriBuilder
import com.sun.jersey.api.client.filter.LoggingFilter

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