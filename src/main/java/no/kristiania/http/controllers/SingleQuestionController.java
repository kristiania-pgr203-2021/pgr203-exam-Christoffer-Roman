package no.kristiania.http.controllers;

import no.kristiania.http.HttpRequest;
import no.kristiania.http.HttpResponse;

import java.io.IOException;
import java.sql.SQLException;

public class SingleQuestionController implements Controller{

    @Override
    public HttpResponse handle(HttpRequest request) throws SQLException, IOException {
        return null;
    }

    // TODO: implement methods

// notes for later:
//<input type="hidden" name="id" value="id">
//<p><label>Title: <input type="text" name="questionTitle"></label></p>
//<p><label>Question: <input type="text" name="questionText"></label></p>
}
