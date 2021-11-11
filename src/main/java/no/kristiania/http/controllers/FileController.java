package no.kristiania.http.controllers;

import no.kristiania.http.HttpMessage;
import no.kristiania.http.HttpRequest;
import no.kristiania.http.HttpResponse;
import no.kristiania.http.ResponseCode;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class FileController implements Controller {
    private static final List<String> PATHS = List.of("/index.html", "/addAnswer.html", "/addQuestion.html",
            "/allAnswers.html", "/allQuestions.html", "/editQuestion.html", "/style.css");


    public static List<String> PATHS() {
        return PATHS;
    }

    @Override
    public HttpResponse handle(HttpRequest request) throws IOException {
        InputStream fileResource = getClass().getResourceAsStream(request.getPath());
        if (fileResource != null) {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            fileResource.transferTo(buffer);
            String responseMessage = buffer.toString();
            String contentType = HttpMessage.getContentType(request.getPath());
            return new HttpResponse(ResponseCode.OK, responseMessage, contentType);
        }
        return new HttpResponse(ResponseCode.ERROR, ResponseCode.ERROR.toString(), "text/plain");
    }
}
