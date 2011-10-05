package org.sjersey.client

import javax.ws.rs.core.UriBuilder
import com.sun.jersey.api.client.filter.LoggingFilter
import com.sun.jersey.client.apache.ApacheHttpClient
import com.codahale.jersey.providers.{JValueProvider, ArrayProvider, JsonCaseClassProvider}
import com.sun.jersey.client.apache.config.{ApacheHttpClientConfig, DefaultApacheHttpClientConfig}

/**
 * provider trait for the instance of WebResource used in class rest
 *
 * @author Christopher Schmidt
 */
trait SimpleWebResourceProvider {

  /**
   * client configuration parameter like
   * (ApacheHttpClientConfig.PROPERTY_PREEMPTIVE_AUTHENTICATION, java.lang.Boolean.TRUE)
   * (ApacheHttpClientConfig.PROPERTY_HANDLE_COOKIES, java.lang.Boolean.TRUE)
   */
  def getApacheHttpClientConfig: List[(String, AnyRef)] = Nil

  /**
   * allows some custom configuration
   * called after getApacheHttpClientConfig config and
   * before creation of the client
   */
  def doConfig(c: DefaultApacheHttpClientConfig): Unit = {}

  /**
   * has to be implemented to return the base URI (host, port, path) as String
   */
  def baseUriAsString: String

  /**
   * has to be overwritten so disable the HTTP logging filter
   */
  def enableLogFilter = true

  private val baseUri = UriBuilder.fromUri(baseUriAsString).build()


  /**
   * creating the configuration for apache client
   * basic authentication and timeouts
   */
  private val config = new DefaultApacheHttpClientConfig()

  /**
   * adding the Case Class Provider
   */
  config.getClasses.add(classOf[JsonCaseClassProvider])
  config.getClasses.add(classOf[ArrayProvider[_]])
  config.getClasses.add(classOf[JValueProvider])

  /**
   * setting up Apache client parameter
   */
  getApacheHttpClientConfig.foreach {
    p =>
      val (k, v) = p
      config.getProperties.put(k, v)
  }

  doConfig(config)

  /**
   * lazy because of configuring with config
   */
  private lazy val client = ApacheHttpClient.create(config)

  /**
   * instance of WebResource created with baseUri
   */
  protected val webResource = client resource baseUri


  if (enableLogFilter)
    client.addFilter(new LoggingFilter())

  /**
   *    creates a new WebResource from an absolute URI
   */
  def getWebResourceFromAbsURI(absPath: String) = {
    val uri = UriBuilder.fromUri(absPath).build()
    client resource uri
  }
}