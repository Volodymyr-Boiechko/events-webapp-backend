package com.boiechko.eventswebapp.controller;

import static com.boiechko.eventswebapp.util.HttpUtils.buildQueryUrlWithoutEncoding;

import com.boiechko.eventswebapp.config.AppConstants;
import com.boiechko.eventswebapp.dto.ApiAuthUrlDto;
import com.boiechko.eventswebapp.dto.JwtTokenDto;
import com.boiechko.eventswebapp.service.GoogleService;
import com.boiechko.eventswebapp.util.JacksonUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/google")
@Slf4j
public class GoogleController {

  private final GoogleService googleService;

  public GoogleController(final GoogleService googleService) {
    this.googleService = googleService;
  }

  @GetMapping("/create-authorization")
  public ResponseEntity<ApiAuthUrlDto> createAuthorization(
      @RequestParam(value = "redirectUrl", required = false) final String redirectUrl) {
    return ResponseEntity.ok(googleService.createGoogleAuthorization(redirectUrl));
  }

  @GetMapping
  public ResponseEntity<String> authenticateInSystem(
      @RequestParam("state") final String state,
      @RequestParam("code") final String code,
      @RequestParam("scope") final String scope) {

    final Map<String, String> stateMap =
        JacksonUtils.deserialize(state, new TypeReference<Map<String, String>>() {});

    final JwtTokenDto jwtTokenDto = googleService.authenticateInSystem(code, stateMap);

    if (Objects.isNull(stateMap) || Objects.isNull(stateMap.get(AppConstants.REDIRECT_URL))) {
      return ResponseEntity.ok(jwtTokenDto.getJwtToken());
    } else {

      final Map<String, String> queryParams =
          new HashMap<String, String>() {
            {
              put("accessToken", jwtTokenDto.getJwtToken());
            }
          };

      final String locationUrl =
          buildQueryUrlWithoutEncoding(stateMap.get(AppConstants.REDIRECT_URL), queryParams);
      return ResponseEntity.status(HttpStatus.FOUND).header("location", locationUrl).build();
    }
  }
}
