package com.boiechko.eventswebapp.service.impl;

import static com.boiechko.eventswebapp.util.GeneralUtils.compactString;
import static com.boiechko.eventswebapp.util.HttpUtils.buildQueryUrlWithoutEncoding;

import com.boiechko.eventswebapp.exception.CriteriaNotMetException;
import com.boiechko.eventswebapp.service.QueryService;
import com.boiechko.eventswebapp.util.Assert;
import java.util.Map;
import java.util.Objects;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class QueryServiceImpl implements QueryService {

  private final RestTemplate restTemplate;

  public QueryServiceImpl(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  @Override
  public ResponseEntity<String> makeGetRequest(
      final String url,
      final Map<String, String> requestParams,
      final Map<String, String> headersMap) {
    return doGetRequest(url, requestParams, headersMap);
  }

  @Override
  public <T> ResponseEntity<String> makePostRequest(
      final String url,
      final Map<String, String> requestParams,
      final Map<String, String> headersMap,
      final T requestBody) {
    return makeModifyingRequest(url, requestParams, headersMap, requestBody, HttpMethod.POST);
  }

  @Override
  public <T> ResponseEntity<String> makePutRequest(
      final String url,
      final Map<String, String> requestParams,
      final Map<String, String> headersMap,
      final T requestBody) {
    return makeModifyingRequest(url, requestParams, headersMap, requestBody, HttpMethod.PUT);
  }

  @Override
  public <T> ResponseEntity<String> makeDeleteRequest(
      final String url,
      final Map<String, String> requestParams,
      final Map<String, String> headersMap,
      final T requestBody) {
    return makeModifyingRequest(url, requestParams, headersMap, requestBody, HttpMethod.DELETE);
  }

  private ResponseEntity<String> doGetRequest(
      final String url,
      final Map<String, String> requestParams,
      final Map<String, String> headerMap) {
    final HttpHeaders headers = new HttpHeaders();
    if (Objects.nonNull(headerMap)) {
      headerMap.forEach(headers::set);
    }

    final String requestUrl = buildQueryUrlWithoutEncoding(url, requestParams);
    log.debug("Make HTTP GET request by url: {}", requestUrl);
    final ResponseEntity<String> responseEntity =
        restTemplate.exchange(requestUrl, HttpMethod.GET, new HttpEntity<>(headers), String.class);
    log.debug(
        "Get HTTP response status {} body {}",
        responseEntity.getStatusCode(),
        compactString(responseEntity.getBody()));
    return responseEntity;
  }

  private <T> ResponseEntity<String> makeModifyingRequest(
      final String url,
      final Map<String, String> queryParameters,
      final Map<String, String> headerMap,
      final T requestBody,
      @NonNull HttpMethod method) {
    Assert.isTrue(
        Objects.equals(HttpMethod.GET, method),
        () -> new CriteriaNotMetException("Http method GET is not a modifying one"));

    final HttpHeaders headers = new HttpHeaders();
    if (Objects.nonNull(headerMap)) {
      headerMap.forEach(headers::set);
    }

    final HttpEntity<T> httpEntity = new HttpEntity<>(requestBody, headers);

    final String requestUrl = buildQueryUrlWithoutEncoding(url, queryParameters);
    log.debug(
        "Make HTTP " + method.name() + " request by url: {} and body {}", requestUrl, requestBody);
    final ResponseEntity<String> responseEntity =
        restTemplate.exchange(requestUrl, method, httpEntity, String.class);
    log.debug(
        "Get HTTP response status {} body {}",
        responseEntity.getStatusCode(),
        compactString(responseEntity.getBody()));
    return responseEntity;
  }
}
