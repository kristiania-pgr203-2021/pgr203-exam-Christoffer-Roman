package no.kristiania.http;

import no.kristiania.http.controllers.FileController;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HttpServerTest {

    HttpServer server = new HttpServer();

    @Test
    void shouldReturn404() throws IOException {
        HttpClient client = new HttpClient("localhost", server.getPort(), "/not-there");
        assertEquals(404, client.getResponseCode());
    }

    @Test
    void shouldReturn404WithRequestTarget() throws IOException {
        HttpClient client = new HttpClient("localhost", server.getPort(), "/not-there");
        assertEquals("not found!", client.getMessageBody().toLowerCase());
    }

    @Test
    void shouldReturnFile() throws IOException {
        String fileContent = "A file created at: " + LocalTime.now();
        String path = "/example-file.txt";
        server.setRoot(Paths.get("target/test-classes"));
        server.addController(path, new FileController(server));
        Files.write(Paths.get("target/test-classes/" + path), fileContent.getBytes());
        HttpClient client = new HttpClient("localhost", server.getPort(), path);
        assertEquals(fileContent, client.getMessageBody());
    }

    @Test
    void shouldHandleMultipleRequest() throws IOException {
        String target = "/index.html";
        server.addController(target, new FileController(server));
        HttpClient client = new HttpClient("localhost", server.getPort(), target);
        assertEquals(200, client.getResponseCode());

        client = new HttpClient("localhost", server.getPort(), "/notavailable");
        assertEquals(404, client.getResponseCode());
        client = new HttpClient("localhost", server.getPort(), "/");
        assertEquals(200, client.getResponseCode());
    }
}