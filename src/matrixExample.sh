#!/bin/sh

curl -X POST -H Accept:application/json -H Content-Type:application/json -d '{"name":"Mr. Andersson"}' -v http://localhost:7474/db/data/node
curl -X POST -H Accept:application/json -H Content-Type:application/json -d '{"name":"Morpheus"}' -v http://localhost:7474/db/data/node
curl -X POST -H Accept:application/json -H Content-Type:application/json -d '{"name":"Trinity"}' -v http://localhost:7474/db/data/node
curl -X POST -H Accept:application/json -H Content-Type:application/json -d '{"name":"Cypher"}' -v http://localhost:7474/db/data/node
curl -X POST -H Accept:application/json -H Content-Type:application/json -d '{"name":"Agent Smith"}' -v http://localhost:7474/db/data/node
curl -X POST -H Accept:application/json -H Content-Type:application/json -d '{"name":"The Architect"}' -v http://localhost:7474/db/data/node

curl -X POST -H Accept:application/json -H Content-Type:application/json -d '{"to":"http://localhost:7474/db/data/node/1","type":"ROOT"}' -v http://localhost:7474/db/data/node/0/relationships
curl -X POST -H Accept:application/json -H Content-Type:application/json -d '{"to":"http://localhost:7474/db/data/node/2","type":"KNOWS"}' -v http://localhost:7474/db/data/node/1/relationships
curl -X POST -H Accept:application/json -H Content-Type:application/json -d '{"to":"http://localhost:7474/db/data/node/3","type":"KNOWS"}' -v http://localhost:7474/db/data/node/2/relationships
curl -X POST -H Accept:application/json -H Content-Type:application/json -d '{"to":"http://localhost:7474/db/data/node/4","type":"KNOWS"}' -v http://localhost:7474/db/data/node/2/relationships
curl -X POST -H Accept:application/json -H Content-Type:application/json -d '{"to":"http://localhost:7474/db/data/node/5","type":"KNOWS"}' -v http://localhost:7474/db/data/node/4/relationships
curl -X POST -H Accept:application/json -H Content-Type:application/json -d '{"to":"http://localhost:7474/db/data/node/6","type":"CODED BY"}' -v http://localhost:7474/db/data/node/5/relationships
curl -X POST -H Accept:application/json -H Content-Type:application/json -d '{"to":"http://localhost:7474/db/data/node/1","type":"LOVES"}' -v http://localhost:7474/db/data/node/3/relationships