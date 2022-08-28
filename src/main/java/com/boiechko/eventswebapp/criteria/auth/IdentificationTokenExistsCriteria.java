package com.boiechko.eventswebapp.criteria.auth;

import com.boiechko.eventswebapp.criteria.Criteria;
import com.boiechko.eventswebapp.dto.IdentificationTokenDto;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class IdentificationTokenExistsCriteria implements Criteria<String> {

  private final HttpSession httpSession;

  public IdentificationTokenExistsCriteria(final HttpSession httpSession) {
    this.httpSession = httpSession;
  }

  @Override
  public boolean criteriaMet(final String identificationToken) {
    final Object identificationTokenObject = httpSession.getAttribute("identificationToken");

    if (identificationTokenObject instanceof IdentificationTokenDto) {
      final IdentificationTokenDto dto = (IdentificationTokenDto) identificationTokenObject;
      return StringUtils.equals(dto.getTemporaryToken(), identificationToken);
    }
    return false;
  }
}
