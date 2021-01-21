package com.github.yituhealthcare.arcgraphqlsample.biz;

import org.springframework.expression.AccessException;
import org.springframework.stereotype.Service;

/**
 * @author wangyuheng
 */
@Service
public class AuthService {

    public void valid(String targetAuthRole, String authorization) throws AccessException {
        String role = resolveRoleByToken(authorization);
        if (!targetAuthRole.equalsIgnoreCase(role)) {
            throw new AccessException("auth fail!");
        }
    }

    private String resolveRoleByToken(String token) {
        return token;
    }

}
