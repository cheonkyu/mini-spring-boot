package io.cheonkyu.mini.spring.boot;

import java.net.InetSocketAddress;

import io.cheonkyu.mini.spring.context.ApplicationContext;
import io.cheonkyu.mini.spring.web.servlet.DispatcherServlet;
import com.sun.net.httpserver.HttpServer;

public class SpringApplication {
  public static ApplicationContext run(Class<?> primarySource, String... args) {
    return run(new Class<?>[] { primarySource }, args);
  }

  public static ApplicationContext run(Class<?>[] primarySources, String[] args) {
    return new SpringApplication().run(args);
  }

  public ApplicationContext run(String... args) {
    final var context = createApplicationContext();
    start(context);
    return context;
  }

  public ApplicationContext createApplicationContext() {
    return new ApplicationContext("io.cheonkyu");
  }

  public void start(ApplicationContext context) {
    try {
      DispatcherServlet dispatcher = new DispatcherServlet("io.cheonkyu", context);

      HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
      server.createContext("/", exchange -> {
        String path = exchange.getRequestURI().getPath();
        String response;
        try {
          response = dispatcher.handleRequest(path);
          exchange.sendResponseHeaders(200, response.getBytes().length);
          exchange.getResponseBody().write(response.getBytes());
          exchange.getResponseBody().close();
        } catch (Exception e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      });
      server.start();
      System.out.println("🚀 Server started on http://localhost:8080");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
