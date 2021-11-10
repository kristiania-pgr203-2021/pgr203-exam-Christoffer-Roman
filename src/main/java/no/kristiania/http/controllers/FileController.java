package no.kristiania.http.controllers;

import no.kristiania.http.*;

import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;

public class FileController implements Controller {
    private final HttpServer server;

    public FileController(HttpServer server) {
        this.server = server;
    }

    @Override
    public HttpResponse handle(HttpRequest request) throws IOException {
        String responseMessage = Files.readString(server.getRootPath().resolve(request.getPath().substring(1)));
        String contentType = HttpMessage.getContentType(request.getPath());
        return new HttpResponse(ResponseCode.OK, responseMessage, contentType);
    }
}
