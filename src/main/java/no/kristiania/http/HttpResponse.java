package no.kristiania.http;

public class HttpResponse extends HttpMessage {
    private final ResponseCode responseCode;
    private final String responseBody;
    private final String contentType;

    public HttpResponse(ResponseCode responseCode, String responseBody, String contentType) {
        this.responseCode = responseCode;
        this.responseBody = responseBody;
        this.contentType = contentType;
    }

    public String getResponseCode() {
        return responseCode.toString();
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

    public String getHeader(String header) {
        return headers.get(header);
    }
}
