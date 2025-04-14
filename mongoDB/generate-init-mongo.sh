#!/bin/bash

# Genera el archivo init-mongo.js en /tmp
cat <<EOF > /tmp/init-mongo.js
db = db.getSiblingDB('admin');

db.createUser({
  user: "${MONGO_INITDB_ROOT_USERNAME}",
  pwd: "${MONGO_INITDB_ROOT_PASSWORD}",
  roles: [
    { role: "readWrite", db: "${MONGO_INITDB_DATABASE}" },
    { role: "dbAdmin", db: "${MONGO_INITDB_DATABASE}" }
  ]
});

db = db.getSiblingDB('${MONGO_INITDB_DATABASE}');
db.createCollection("products");
db.createCollection("orders");
db.createCollection("users");

print("Database '${MONGO_INITDB_DATABASE}' and user '${MONGO_INITDB_ROOT_USERNAME}' initialized successfully.");
EOF

# Mueve el archivo a /docker-entrypoint-initdb.d/
mv /tmp/init-mongo.js /docker-entrypoint-initdb.d/init-mongo.js