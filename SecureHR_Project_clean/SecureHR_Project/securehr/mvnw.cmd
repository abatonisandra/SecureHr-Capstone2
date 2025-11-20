@ECHO OFF
SETLOCAL

SET WRAPPER_JAR=.mvn\wrapper\maven-wrapper.jar
SET PROPS_FILE=.mvn\wrapper\maven-wrapper.properties

IF NOT EXIST "%WRAPPER_JAR%" (
  ECHO Downloading Maven Wrapper...
  powershell -Command "New-Item -ItemType Directory -Force .mvn/wrapper | Out-Null"
  powershell -Command "Invoke-WebRequest -Uri https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar -OutFile %WRAPPER_JAR%"
)

IF NOT EXIST "%PROPS_FILE%" (
  >"%PROPS_FILE%" ECHO wrapperUrl=https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar
  >>"%PROPS_FILE%" ECHO distributionUrl=https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/3.9.6/apache-maven-3.9.6-bin.zip
)

SET JAVA_EXE=%JAVA_HOME%\bin\java.exe
IF NOT EXIST "%JAVA_EXE%" (
  SET JAVA_EXE=java
)

"%JAVA_EXE%" %JAVA_OPTS% -classpath "%WRAPPER_JAR%" -Dmaven.multiModuleProjectDirectory=%CD% org.apache.maven.wrapper.MavenWrapperMain %*
