package io.cheonkyu.mini.spring.context.scanner;

import org.reflections.Reflections;

import io.cheonkyu.mini.spring.context.annotation.Bean;

import java.util.Set;

public class AnnotationScanner {
  public static void load(String[] args) {
    Reflections reflections = new Reflections("com.example"); // 스캔할 패키지

    Set<Class<?>> annotatedClasses = reflections.getTypesAnnotatedWith(Bean.class);

    for (Class<?> clazz : annotatedClasses) {
      System.out.println("Found class: " + clazz.getName());
    }
  }
}
