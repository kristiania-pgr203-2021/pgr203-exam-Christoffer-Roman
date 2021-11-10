package no.kristiania.http.controllers;

import no.kristiania.http.HttpRequest;
import no.kristiania.http.HttpResponse;

import java.io.IOException;
import java.sql.SQLException;

public class SingleAnswerController implements Controller {

    @Override
    public HttpResponse handle(HttpRequest request) throws SQLException, IOException {
        return null;
    }
}
