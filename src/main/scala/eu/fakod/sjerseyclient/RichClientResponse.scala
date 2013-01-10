package eu.fakod.sjerseyclient

import com.sun.jersey.api.client.ClientResponse
import collection.JavaConversions._
import java.{util => ju}
import collection.mutable

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