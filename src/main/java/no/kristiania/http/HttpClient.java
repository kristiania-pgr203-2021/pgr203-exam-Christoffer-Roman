package no.kristiania.http;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;

public class HttpClient {

    private Socket socket;
    private final String host;
    private final int port;
    private int responseCode;
    private final HashMap<String, String> headerFields = new HashMap<>();
    private String messageBody;
    private String requestBody = "";

    public HttpClient(String host, int port, String target) throws IOException {
        this.host = host;
        this.port = port;
        this.socket = new Socket(host, port);
        executeGetRequest(target);
    }
    public HttpClient(String host, int port, String target, HttpMethod method, String requestBody) throws IOException {
        this.socket = new Socket(host, port);
        this.host = host;
        this.port = port;
        this.requestBody = requestBody;
        if (method.equals(HttpMethod.POST)) {
            executePostRequest(target);
        } else {
            executeGetRequest(target);
        }
    }

    private void executePostRequest(String target) throws IOException {
        String request =
                "POST " + target + " HTTP/1.1\r\n" +
                        "Host: " + host + "\r\n" +
                        "Content-Length: " + requestBody.getBytes().length + "\r\n" +
                        "Connection: keep-alive\r\n" +
                        "\r\n" +
                        requestBody;

        socket.getOutputStream().write(request.getBytes());


        readResponse();
    }

    private void executeGetRequest(String target) throws IOException {

        String request =
                "GET " + target + " HTTP/1.1\r\n" +
                        "Host: " + host + "\r\n" +
                        "Connection: keep-alive\r\n" +
                        "\r\n";

        socket.getOutputStream().write(request.getBytes());
        readResponse();
        headerFields.put("Target", target);
    }

    private void readResponse() throws IOException {
        String statusLine = HttpMessage.readLine(socket);
        this.responseCode = Integer.parseInt(statusLine.split(" ")[1]);

        String headerLine;
        while(!(headerLine = HttpMessage.readLine(socket)).isBlank()){
            int colonPos = headerLine.indexOf(":");
            String headerName = headerLine.substring(0, colonPos);
            String headerValue = headerLine.substring(colonPos+1).trim();
            headerFields.put(headerName, headerValue);
        }
        this.messageBody = HttpMessage.readBytes(socket, getContentLength());

        if (responseCode == 303) {
            socket = new Socket(host, port);
            executeGetRequest(headerFields.get("Location"));
        }
    }

    public int getResponseCode() {
        return responseCode;
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
