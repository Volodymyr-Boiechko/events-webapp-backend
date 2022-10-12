package com.boiechko.eventswebapp.controller;

import static com.boiechko.eventswebapp.util.HttpUtils.buildQueryUrlWithoutEncoding;

import com.boiechko.eventswebapp.config.AppConstants;
import com.boiechko.eventswebapp.dto.ApiAuthUrlDto;
import com.boiechko.eventswebapp.dto.JwtTokenDto;
import com.boiechko.eventswebapp.service.impl.apisocialmedia.SocialMediaService;
import com.boiechko.eventswebapp.util.JacksonUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/facebook")
public class FacebookController {

  private final SocialMediaService facebookService;

  public FacebookController(
      @Qualifier("facebookServiceImpl") final SocialMediaService facebookService) {
    this.facebookService = facebookService;
  }

  @GetMapping("/create-authorization")
  public ResponseEntity<ApiAuthUrlDto> createAuthorization(
      @RequestParam(value = "redirectUrl", required = false) final String redirectUrl) {
    return ResponseEntity.ok(facebookService.createAuthorization(redirectUrl));
  }

  @GetMapping
  public ResponseEntity<String> authenticateInSystem(
      @RequestParam("state") final String state,
      @RequestParam("code") final String code,
      @RequestParam(value = "scope", required = false) final String scope) {

    final Map<String, String> stateMap =
        JacksonUtils.deserialize(state, new TypeReference<Map<String, String>>() {});

    final JwtTokenDto jwtTokenDto = facebookService.authenticateInSystem(code, stateMap);

    if (Objects.isNull(stateMap) || Objects.isNull(stateMap.get(AppConstants.REDIRECT_URL))) {
      return ResponseEntity.ok(jwtTokenDto.getJwtToken());
    }
    final Map<String, String> queryParams = new HashMap<>();
    queryParams.put("accessToken", jwtTokenDto.getJwtToken());

    final String locationUrl =
        buildQueryUrlWithoutEncoding(stateMap.get(AppConstants.REDIRECT_URL), queryParams);
    return ResponseEntity.status(HttpStatus.FOUND).header("location", locationUrl).build();
  }
}
