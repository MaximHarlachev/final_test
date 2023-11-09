package api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.Step;
import model.api.*;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

public class CompanyServiceImpl implements CompanyService{
    public static final MediaType APPLICATION_JSON = MediaType.parse("application/json; charset=UTF-8");
    private static final String PATH = "company";
    private final String BASE_PATH;
    private final OkHttpClient client;
    private final ObjectMapper mapper;
    private static final String DELETE = "delete";
    private String token;

    public CompanyServiceImpl(OkHttpClient client, String url) {
        this.client = client;
        this.BASE_PATH = url;
        this.mapper = new ObjectMapper();
    }

    @Override
    public void setToken(String token) {
        this.token = token;
    }

    @NotNull
    private HttpUrl.Builder getUrl() {
        return HttpUrl.parse(BASE_PATH).newBuilder().addPathSegment(PATH);
    }

    @Step("Получение компании по её id")
    @Override
    public Company getById(int id) throws IOException {
        HttpUrl url = getUrl()
                .addPathSegment(Integer.toString(id))
                .build();
        Request request = new Request.Builder().url(url).get().build();
        Response response = client.newCall(request).execute();
        return mapper.readValue(response.body().string(), Company.class);
    }

    @Override
    public ApiResponse<CreateCompanyResponse> create(String name, String description) throws IOException {
        HttpUrl url = getUrl().build();
        CreateCompanyRequest body = new CreateCompanyRequest(name, description);
        RequestBody jsonBody = RequestBody.create(mapper.writeValueAsString(body), APPLICATION_JSON);
        Request.Builder request = new Request.Builder().post(jsonBody).url(url);

        if (token != null) {
            request.addHeader("x-client-token", token);
        }

        Response response = this.client.newCall(request.build()).execute();
        if (response.code() >= 400) {
            ApiError body1 = mapper.readValue(response.body().string(), ApiError.class);
            return new ApiResponse<>(response.headers().toMultimap(), response.code(), null, body1);
        } else {
            CreateCompanyResponse body1 = mapper.readValue(response.body().string(), CreateCompanyResponse.class);
            return new ApiResponse<>(response.headers().toMultimap(), response.code(), body1, null);
        }
    }

    @Step("Удаление компании по её id")
    @Override
    public void deleteById(int id) throws IOException {
        HttpUrl url = getUrl().addPathSegment(DELETE).addPathSegment(Integer.toString(id)).build();
        Request.Builder request = new Request.Builder().get().url(url);
        if (token != null) {
            request.addHeader("x-client-token", token);
        }
        this.client.newCall(request.build()).execute();
    }

    @Step("Получение списка всех компаний")
    @Override
    public List<Company> getAll() throws IOException {
        HttpUrl url = getUrl().build();
        Request request = new Request.Builder().url(url).get().build();
        Response response = client.newCall(request).execute();
        return  mapper.readValue(response.body().string(), new TypeReference<>() {});
    }

    @Step("Получение списка всех активных компаний")
    @Override
    public List<Company> getAll(boolean isActive) throws IOException {
        HttpUrl url = getUrl()
                .addQueryParameter("active", Boolean.toString(isActive))
                .build();
        Request request = new Request.Builder().url(url).get().build();
        Response response = client.newCall(request).execute();
        return  mapper.readValue(response.body().string(), new TypeReference<>() {});
    }
}
