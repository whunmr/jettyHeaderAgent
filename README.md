jettyHeaderAgent
================

Modify Http Header contents in Jetty

Sometimes we need Adding some customized HTTP header in Jetty's HttpServletRequest.
Here is a way to change Request.getHeader() using java.lang.instrument and javassist.

**command to compile and pack jar:**
javac -classpath ./javassist.jar HeaderAgent.java 
jar -cmf MANIFEST.MF sslagent.jar HeaderAgent.class 

**how to use:**
adding following option to your java command, then Request.getHeader() method will be customized after Request.class loaded.
-javaagent:$HOME/bin/jettySSLPlugin/sslagent.jar 
