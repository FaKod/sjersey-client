package org.sjersey.unittest.json.neo4jstuff

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
 * this case classes used by REST method:
 * http://components.neo4j.org/neo4j-server/0.5-1.2/rest.html#Create_relationship
 *
 * @author Christopher Schmidt
 */

case class MatrixRelationProperties(to: String, `type`: String)

/**
 * this case classes used by REST method:
 * http://components.neo4j.org/neo4j-server/0.5-1.2/rest.html#Listing_node_indexes
 *
 * @author Christopher Schmidt
 */

case class CreateNodeIndex(name: String)

@JsonIgnoreProperties(ignoreUnknown = true)
case class GetIndex(my_nodes: MyNodes)

case class MyNodes(template: String,
                   provider: String,
                   `type`: String)

/**
 * this case classes used by REST method:
 * http://components.neo4j.org/neo4j-server/0.5-1.2/rest.html#Get_root
 *
 * @author Christopher Schmidt
 */
@JsonIgnoreProperties(ignoreUnknown = true)
case class GetRoot(relationship_index: String,
                   node: String,
                   extensions_info: String,
                   node_index: String,
                   reference_node: String,
                   extensions: AnyRef)

/**
 * this case classes used by REST method:
 * http://components.neo4j.org/neo4j-server/0.5-1.2/rest.html#Create_node_with_properties
 *
 * @author Christopher Schmidt
 */

case class MatrixNodeProperties(name: String, profession: String)

/**
 * this case classes used by REST method:
 * http://components.neo4j.org/neo4j-server/0.5-1.2/rest.html#Traverse
 *
 * @author Christopher Schmidt
 */

case class PathRequest(order: String,
                       max_depth: Int,
                       uniqueness: String)

/**
 * path traversal response
 */
case class TraversePath(start: String,
                        nodes: List[String],
                        length: Int,
                        relationships: List[String],
                        end: String)