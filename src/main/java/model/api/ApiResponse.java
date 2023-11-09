package model.api;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ApiResponse<T> {
    private final Map<String, List<String>> headers;
    private final int statusCode;
    private final T body;
    private final ApiError error;

    public ApiResponse(Map<String, List<String>> headers, int statusCode, T body, ApiError error) {
        this.headers = headers;
        this.statusCode = statusCode;
        this.body = body;
        this.error = error;
    }

    public Map<String, List<String>> getHeaders() {
        return headers;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public T getBody() {
        return body;
    }

    public ApiError getError() {
        return error;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ApiResponse<?> that)) return false;
        return getStatusCode() == that.getStatusCode() && Objects.equals(getHeaders(), that.getHeaders()) && Objects.equals(getBody(), that.getBody());
    }

    @Override
    public int hashCode() {
        return Objects.hash(headers, statusCode, body, error);
    }

    @Override
    public String toString() {
        return "ApiResponse{" +
                "headers=" + headers +
                ", statusCode=" + statusCode +
                ", body=" + body +
                ", error=" + error +
                '}';
    }
}
