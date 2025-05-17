package io.cheonkyu.mini.spring.beans;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BeanFactory {
  private final Map<Class<?>, Object> beanMap = new HashMap<>();

  public Object createBean(Class<?> clazz) throws Exception {
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
