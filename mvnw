#!/bin/sh
# Maven Wrapper script
# https://github.com/apache/maven-wrapper

set -euf

# OS specific support
cygwin=false
darwin=false
mingw=false
case "$(uname)" in
  CYGWIN*) cygwin=true ;;
  MINGW*)  mingw=true ;;
  Darwin*) darwin=true ;;
esac

MAVEN_PROJECTBASEDIR="${MAVEN_BASEDIR:-$(cd "$(dirname "$0")" && pwd)}"
MAVEN_WRAPPER_JAR_PATH="${MAVEN_PROJECTBASEDIR}/.mvn/wrapper/maven-wrapper.jar"

# Download Maven Wrapper if needed
if [ ! -f "$MAVEN_WRAPPER_JAR_PATH" ]; then
  echo "Downloading Maven Wrapper..."
  WRAPPER_URL="https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar"

  if command -v curl > /dev/null 2>&1; then
    curl -fsSL -o "$MAVEN_WRAPPER_JAR_PATH" "$WRAPPER_URL"
  elif command -v wget > /dev/null 2>&1; then
    wget -q -O "$MAVEN_WRAPPER_JAR_PATH" "$WRAPPER_URL"
  else
    echo "Error: Neither curl nor wget found. Cannot download Maven Wrapper."
    exit 1
  fi
fi

# Find Java
if [ -n "${JAVA_HOME:-}" ]; then
  JAVA_EXE="$JAVA_HOME/bin/java"
else
  JAVA_EXE="$(which java)"
fi

if [ ! -x "$JAVA_EXE" ]; then
  echo "Error: JAVA_HOME is not set and Java is not available in PATH."
  exit 1
fi

# Run Maven Wrapper
exec "$JAVA_EXE" \
  -classpath "$MAVEN_WRAPPER_JAR_PATH" \
  org.apache.maven.wrapper.MavenWrapperMain "$@"
