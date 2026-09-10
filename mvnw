#!/bin/sh
if [ -z "$MAVEN_HOME" ]; then
  MAVEN_HOME="$(dirname "$0")/../.m2"
fi

if [ -x "$MAVEN_HOME/bin/mvn" ]; then
  exec "$MAVEN_HOME/bin/mvn" "$@"
fi

exec mvn "$@"
