package no.kristiania.http;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;

public class HttpClient {

    private final Socket socket;
    private int statusCode;
    private final HashMap<String, String> headerFields = new HashMap<>();
    private String messageBody;
    private String requestBody = "";

    public HttpClient(String host, int port, String target) throws IOException {
        this.socket = new Socket(host, port);
        executeGetRequest(host, target);
    }
    public HttpClient(String host, int port, String target, HttpMethod method, String requestBody) throws IOException {
        this.socket = new Socket(host, port);
        this.requestBody = requestBody;
        if (method.equals(HttpMethod.POST)) {
            executePostRequest(host, target);
        } else {
            executeGetRequest(host, target);
        }
    }

    private void executePostRequest(String host, String target) throws IOException {
        String request =
                "POST " + target + " HTTP/1.1\r\n" +
                        "Host: " + host + "\r\n" +
                        "Content-Length: " + requestBody.getBytes().length + "\r\n" +
                        "Connection: close\r\n" +
                        "\r\n" +
                        requestBody;

        socket.getOutputStream().write(request.getBytes());

        readResponse();
    }

    private void executeGetRequest(String host, String target) throws IOException {

        String request =
                "GET " + target + " HTTP/1.1\r\n" +
                        "Host: " + host + "\r\n" +
                        "Connection: close\r\n" +
                        "\r\n";

        socket.getOutputStream().write(request.getBytes());

        readResponse();
    }

    private void readResponse() throws IOException {
        String statusLine = HttpMessage.readLine(socket);
        this.statusCode = Integer.parseInt(statusLine.split(" ")[1]);

        String headerLine;
        while(!(headerLine = HttpMessage.readLine(socket)).isBlank()){
            int colonPos = headerLine.indexOf(":");
            String headerName = headerLine.substring(0, colonPos);
            String headerValue = headerLine.substring(colonPos+1).trim();
            headerFields.put(headerName, headerValue);
        }
        this.messageBody = HttpMessage.readBytes(socket, getContentLength());
    }

    public int getResponseCode() {
        return statusCode;
    }

    public String getHeader(String fieldName){
        return headerFields.get(fieldName);
    }

    public int getContentLength(){
        return Integer.parseInt(getHeader("Content-Length"));
    }

    public String getMessageBody(){
        return messageBody;
    }
}