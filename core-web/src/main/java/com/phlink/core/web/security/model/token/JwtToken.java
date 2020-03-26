package com.phlink.core.web.security.model.token;

import java.io.Serializable;

public interface JwtToken extends Serializable {
    String getToken();
}
