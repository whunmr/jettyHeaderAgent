import java.lang.instrument.*;
import java.util.*;
import javassist.*;

public class HeaderAgent implements ClassFileTransformer {
  public static void premain(String agentArgument,
      Instrumentation instrumentation)  {
    instrumentation.addTransformer(new HeaderAgent());
  }

  public byte[] transform(ClassLoader loader, String className,
      Class clazz, java.security.ProtectionDomain domain,
      byte[] bytes) {

    if (!className.equals("org/mortbay/jetty/Request") && !className.equals("org/apache/catalina/connector/Request")) {
      return bytes;
    }
    System.out.println("[transform jetty class]: " + className);   
    return doClass(className, clazz, bytes);
  }

  private byte[] doClass(String name, Class clazz, byte[] b) {
    ClassPool pool = ClassPool.getDefault();
    CtClass cl = null;
    try {
      cl = pool.makeClass(new java.io.ByteArrayInputStream(b));
      if (cl.isInterface() == false) {
        CtBehavior[] methods = cl.getDeclaredBehaviors();
        for (int i = 0; i < methods.length; i++) {
	    if (methods[i].getName().equals("getHeader")) {
		doMethod(methods[i]);
	    }
        }
        b = cl.toBytecode();
      }
    } catch (Exception e) {
      System.err.println("Could not instrument  " + name
          + ",  exception : " + e.getMessage());
    } finally {
      if (cl != null) {
        cl.detach();
      }
    }
    return b;
  }

  private void doMethod(CtBehavior method)
      throws NotFoundException, CannotCompileException {
      System.out.println("[start to transform method]: " + method.getName());   
      method.insertBefore("if (\"FRONT_END_HTTPS\".equals($1) && \"https\".equalsIgnoreCase(getScheme())) { return \"ON\"; }");
      System.out.println("[transform method finished]: " + method.getName());   
  }
}
