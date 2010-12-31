package org.sjersey.client

import com.sun.jersey.api.client.ClientResponse
import collection.JavaConversions._
import java.{util => ju}
import collection.mutable

/**
 * converts to RichClientResponse
 *
 * @author Christopher Schmidt
 */

object RichClientResponse {

  implicit def toRichClientResponse(cr: ClientResponse) = new RichClientResponse(cr)

}

/**
 * to ease the access to Scala like Collections
 *
 * @author Christopher Schmidt
 */
class RichClientResponse(cr: ClientResponse) {

  /**
   * Get the HTTP headers of the response.
   *
   * @return the HTTP headers of the response.
   */
  def headers:mutable.Map[String, String] = cr.getHeaders.asInstanceOf[ju.Map[String, String]]

  //@TODO implement the rest...

}