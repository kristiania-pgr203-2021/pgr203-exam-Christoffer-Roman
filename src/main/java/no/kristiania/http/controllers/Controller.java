package no.kristiania.http.controllers;

import no.kristiania.http.HttpMessage;
import no.kristiania.http.HttpRequest;
import no.kristiania.http.HttpResponse;
import no.kristiania.http.HttpServer;

import java.io.IOException;
import java.sql.SQLException;

public interface  Controller {

    HttpResponse handle(HttpRequest request) throws SQLException, IOException;
}
