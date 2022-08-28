package com.boiechko.eventswebapp.config;

import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

  @Value("${rest-template.read-timeout-in-minutes}")
  private int readTimeout;

  @Value("${rest-template.connect-timeout-in-minutes}")
  private int connectTimeout;

  @Override
  public void addViewControllers(final ViewControllerRegistry registry) {
    registry.addViewController("/notFound").setViewName("forward:/index.html");
  }

  @Bean
  public RestTemplate restTemplate(final RestTemplateBuilder builder) {
    return builder
        .setReadTimeout(Duration.ofMinutes(readTimeout))
        .setConnectTimeout(Duration.ofMinutes(connectTimeout))
        .build();
  }
}
