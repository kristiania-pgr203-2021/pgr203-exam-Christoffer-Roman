package no.kristiania.http.controllers;

import no.kristiania.http.HttpMessage;
import no.kristiania.http.HttpRequest;
import no.kristiania.http.HttpResponse;
import no.kristiania.http.HttpServer;

import java.io.IOException;
import java.sql.SQLException;

public abstract class Controller {

    protected final HttpServer server;

    public Controller(HttpServer server) {
        this.server = server;
    }

    public abstract HttpResponse handle(HttpRequest request) throws SQLException, IOException;
}
