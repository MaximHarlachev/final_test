package ext;


import api.*;
import model.api.UserInfo;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.extension.*;

import java.io.IOException;

public class EmployeeServiceResolver implements ParameterResolver {
    private final static String DEFAULT_USER = MyProperty.getInstance().getProps().getProperty("my.username");
    private final static String DEFAULT_PASS = MyProperty.getInstance().getProps().getProperty("my.pass");
    public static final String URL = MyProperty.getInstance().getProps().getProperty("base.url");

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().equals(EmployeeService.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        OkHttpClient client = new OkHttpClient.Builder().addNetworkInterceptor(new LogInterceptor()).build();
        EmployeeService service = new EmployeeServiceImpl(client, URL);

        if (parameterContext.isAnnotated(Auth.class)) {
            Auth auth = parameterContext.getParameter().getAnnotation(Auth.class);
            AuthService authorizeService = new AuthServiceImpl(client, URL);
            UserInfo userInfo;
            try {
                if (!auth.username().isBlank()) {
                    userInfo = authorizeService.auth(auth.username(), auth.password());
                } else {
                    userInfo = authorizeService.auth(DEFAULT_USER, DEFAULT_PASS);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            service.setToken(userInfo.getUserToken());
        }

        return service;
    }

}
