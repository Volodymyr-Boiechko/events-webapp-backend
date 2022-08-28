package com.boiechko.eventswebapp.service;

import java.util.Map;
import org.springframework.http.ResponseEntity;

public interface QueryService {

  ResponseEntity<String> makeGetRequest(
      final String url,
      final Map<String, String> requestParams,
      final Map<String, String> headersMap);

  <T> ResponseEntity<String> makePostRequest(
      final String url,
      final Map<String, String> requestParams,
      final Map<String, String> headersMap,
      final T requestBody);

  <T> ResponseEntity<String> makePutRequest(
      final String url,
      final Map<String, String> requestParams,
      final Map<String, String> headersMap,
      final T requestBody);

  <T> ResponseEntity<String> makeDeleteRequest(
      final String url,
      final Map<String, String> requestParams,
      final Map<String, String> headersMap,
      final T requestBody);
}
