package no.kristiania.http.controllers;

import no.kristiania.http.HttpMessage;
import no.kristiania.http.HttpRequest;

import java.sql.SQLException;

public interface Controller {

    HttpMessage handle(HttpRequest request) throws SQLException;
}
