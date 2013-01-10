package eu.fakod.unittest

import javax.ws.rs.core.MediaType
import org.specs2.mutable.SpecificationWithJUnit
import eu.fakod.sjerseyclient.{RestImplicits, SimpleWebResourceProvider, Rest}
import com.sun.jersey.api.client.ClientResponse


abstract class Test extends Rest with SimpleWebResourceProvider {

  override val mediaType = MediaType.APPLICATION_JSON :: Nil

  override def enableLogFilter = true
}


class MultiURITest extends SpecificationWithJUnit with RestImplicits {

  sequential

  "A DELETE / POST" should {

    "create new nodes (POST)" in {

      val uriList = "http://spiegel.de" :: "http://welt.de" :: Nil

      val restClientList = uriList.map(u => new Test {
        def baseUriAsString = u
      })

      restClientList.foreach {
        _.rest {
          implicit s =>
            "".GET[ClientResponse]
        }
      }
      success
    }
  }

}
