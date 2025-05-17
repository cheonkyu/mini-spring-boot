package io.cheonkyu.mini.spring.context.scanner;

import org.reflections.Reflections;

import io.cheonkyu.mini.spring.context.annotation.Bean;

import java.util.Set;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.stream.Collectors;

import org.reflections.scanners.Scanners;
import org.reflections.util.ConfigurationBuilder;

import io.cheonkyu.mini.spring.context.annotation.Configuration;
import io.cheonkyu.mini.spring.stereotype.Component;

public class AnnotationScanner {
  private final Set<Class<?>> clazzes = new HashSet<>();

  public void init(String basePackage) {
    try {
      clazzes.addAll(scanStereotypeComponents(basePackage));
      clazzes.addAll(scanConfigComponents(basePackage));
    } catch (Exception e) {
      System.out.println(e);
      throw new RuntimeException("AnnotationScanner init failed", e);
    }
  }

  private Set<Class<?>> scanStereotypeComponents(String basePackage) throws Exception {
    Reflections reflections = new Reflections(basePackage);
    Set<Class<?>> components = reflections.getTypesAnnotatedWith(Component.class);

    Set<Class<?>> result = new HashSet<>();
    for (Class<?> clazz : components) {
      if (clazz.isInterface() || Modifier.isAbstract(clazz.getModifiers())) {
        continue;
      }
      result.add(clazz);
    }
    System.out.println("[components 등록] " + result.size() + "개");
    return result;
  }

  private Set<Class<?>> scanConfigComponents(String basePackage) throws Exception {
    Reflections reflections = new Reflections(
        new ConfigurationBuilder()
            .forPackage(basePackage)
            .addScanners(Scanners.MethodsAnnotated, Scanners.TypesAnnotated));

    // 1. @Configuration이 붙은 클래스만 필터링
    Set<Class<?>> configClasses = reflections.getTypesAnnotatedWith(Configuration.class);

    // 2. 모든 @Bean 메서드 중, 선언 클래스가 @Configuration인 것만 필터링
    Set<Method> beanMethods = reflections.getMethodsAnnotatedWith(Bean.class).stream()
        .filter(method -> configClasses.contains(method.getDeclaringClass()))
        .collect(Collectors.toSet());

    System.out.println("[bean 등록] " + beanMethods.size() + "개");

    Set<Class<?>> result = new HashSet<>();
    for (Method method : beanMethods) {
      Class<?> declaringClass = method.getDeclaringClass();
      result.add(declaringClass);
    }
    return result;
  }

  public Set<Class<?>> getClazzes() {
    return this.clazzes;
  }
}
