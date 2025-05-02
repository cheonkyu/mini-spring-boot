package io.cheonkyu.mini.spring.context;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ConfigurationBuilder;

import io.cheonkyu.mini.spring.context.annotation.Bean;
import io.cheonkyu.mini.spring.context.annotation.Configuration;
import io.cheonkyu.mini.spring.stereotype.Component;

public class ApplicationContext {
  private final Map<Class<?>, Object> beanMap = new HashMap<>();

  public ApplicationContext(String basePackage) {
    try {
      scanComponents(basePackage);
      scanBeans(basePackage);

      System.out.println("beanMap" + beanMap.size());
    } catch (Exception e) {
      System.out.println(e);
      throw new RuntimeException("Context init failed", e);
    }
  }

  private void scanComponents(String basePackage) throws Exception {
    Reflections reflections = new Reflections(basePackage);
    Set<Class<?>> components = reflections.getTypesAnnotatedWith(Component.class);

    for (Class<?> clazz : components) {
      //
      if (clazz.isInterface() || Modifier.isAbstract(clazz.getModifiers())) {
        continue;
      }
      System.out.println(clazz);
      createBean(clazz);
    }
  }

  private void scanBeans(String basePackage) throws Exception {
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

    for (Method method : beanMethods) {
      Class<?> declaringClass = method.getDeclaringClass();
      Object configInstance = declaringClass.getDeclaredConstructor().newInstance();
      Object bean = method.invoke(configInstance);
      beanMap.put(method.getReturnType(), bean);
      System.out.println("[bean 등록됨] " + method.getReturnType().getSimpleName());
    }
  }

  private Object createBean(Class<?> clazz) throws Exception {
    if (beanMap.containsKey(clazz)) {
      return beanMap.get(clazz);
    }

    Constructor<?>[] constructors = clazz.getDeclaredConstructors();
    if (constructors.length == 0) {
      throw new RuntimeException("No constructor found in: " + clazz);
    }

    Constructor<?> constructor = constructors[0];
    List<Object> parameters = new ArrayList<>();

    for (Class<?> paramType : constructor.getParameterTypes()) {
      Object dependency = beanMap.get(paramType);
      if (dependency == null) {
        // 의존성도 먼저 생성
        dependency = createBean(paramType);
      }
      parameters.add(dependency);
    }

    Object instance = constructor.newInstance(parameters.toArray());
    beanMap.put(clazz, instance);
    return instance;
  }

  public <T> T getBean(Class<T> clazz) {
    return clazz.cast(beanMap.get(clazz));
  }
}
