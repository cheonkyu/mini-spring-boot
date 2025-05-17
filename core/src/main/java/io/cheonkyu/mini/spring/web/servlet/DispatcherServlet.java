package io.cheonkyu.mini.spring.web.servlet;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;

import io.cheonkyu.mini.spring.context.ApplicationContext;
import io.cheonkyu.mini.spring.stereotype.Controller;
import io.cheonkyu.mini.spring.web.bind.annotation.GetMapping;

public class DispatcherServlet {
  private Map<String, Method> routeMap = new HashMap<>();
  private Map<Class<?>, Object> controllerInstances = new HashMap<>();

  public DispatcherServlet(String packages, ApplicationContext context) throws Exception {
    Reflections reflections = new Reflections(packages);
    Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);

    for (Class<?> controller : controllers) {
      Object instance = context.getBean(controller);
      controllerInstances.put(controller, instance);

      for (Method method : controller.getDeclaredMethods()) {
        if (method.isAnnotationPresent(GetMapping.class)) {
          String path = method.getAnnotation(GetMapping.class).value();
          routeMap.put(path, method);
        }
      }
    }
  }

  public String handleRequest(String path) throws Exception {
    Method method = routeMap.get(path);
    if (method != null) {
      Object instance = controllerInstances.get(method.getDeclaringClass());
      return (String) method.invoke(instance);
    } else {
      return "404 Not Found";
    }
  }
}
