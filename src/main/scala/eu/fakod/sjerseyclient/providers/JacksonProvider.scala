package eu.fakod.sjerseyclient.providers

import javax.ws.rs.ext.Provider
import com.sun.jersey.core.provider.AbstractMessageReaderWriterProvider
import java.lang.reflect.{Type}
import java.lang.annotation.Annotation
import java.io.{IOException, InputStream, OutputStream}
import org.slf4j.LoggerFactory
import javax.ws.rs.{WebApplicationException, Consumes, Produces}
import javax.ws.rs.core.{Response, MultivaluedMap, MediaType}
import javax.ws.rs.core.Response.Status
import scala.reflect.Manifest
import com.fasterxml.jackson.core.JsonParseException
import eu.fakod.sjerseyclient.util.JacksonDeAndSerializer


@Provider
@Produces(Array(MediaType.APPLICATION_JSON))
@Consumes(Array(MediaType.APPLICATION_JSON))
class JacksonProvider[A] extends AbstractMessageReaderWriterProvider[A] with JacksonDeAndSerializer {

  private val logger = LoggerFactory.getLogger(classOf[JacksonProvider[_]])

  def readFrom(klass: Class[A],
               genericType: Type,
               annotations: Array[Annotation],
               mediaType: MediaType,
               httpHeaders: MultivaluedMap[String, String],
               entityStream: InputStream) = {
    try {
      deserialize(entityStream)(Manifest.classType(klass))
    } catch {
      case e: JsonParseException => {
        throw new WebApplicationException(Response.status(Status.BAD_REQUEST)
          .entity(e.getMessage)
          .build)
      }
    }
  }

  def isReadable(klass: Class[_],
                 genericType: Type,
                 annotations: Array[Annotation],
                 mediaType: MediaType) = mediaType.isCompatible(MediaType.APPLICATION_JSON_TYPE)

  def writeTo(t: A,
              klass: Class[_],
              genericType: Type,
              annotations: Array[Annotation],
              mediaType: MediaType,
              httpHeaders: MultivaluedMap[String, AnyRef],
              entityStream: OutputStream) {
    try {
      serialize(t, entityStream)
    } catch {
      case e: IOException => logger.debug("Error writing to stream", e)
      case e => logger.error("Error encoding %s as JSON".format(t, e))
    }
  }

  def isWriteable(klass: Class[_],
                  genericType: Type,
                  annotations: Array[Annotation],
                  mediaType: MediaType) = mediaType.isCompatible(MediaType.APPLICATION_JSON_TYPE)
}
