package no.kristiania.http;

public class HttpResponse extends HttpMessage {
    private final String responseLine;
    private final String responseBody;
    private final String contentType;

    public HttpResponse(String responseLine, String responseBody, String contentType) {
        this.responseLine = responseLine;
        this.responseBody = responseBody;
        this.contentType = contentType;
    }

    public String getResponseLine() {
        return responseLine;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public String getContentType() {
        return contentType;
    }

    public void addHeader(String key, String value) {
        this.headers.put(key, value);
    }
}
