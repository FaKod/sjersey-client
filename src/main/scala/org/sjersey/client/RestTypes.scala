package org.sjersey.client

import com.sun.jersey.api.client.WebResource

/**
 * common type aliases for use in this package
 *
 * @author Christopher Schmidt
 */
private[client] object RestTypes {
  type builderType = WebResource#Builder
  type BuilderFuncType = (String, RestCallSettings, Boolean) => builderType
}