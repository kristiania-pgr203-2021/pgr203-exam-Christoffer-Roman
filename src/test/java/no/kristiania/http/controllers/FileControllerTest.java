package no.kristiania.http.controllers;

import no.kristiania.HttpClient;
import no.kristiania.http.HttpServer;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileControllerTest {

    HttpServer server = new HttpServer();

    @Test
    void shouldReturnExampleFile() throws IOException {
        String fileContent = "A file created at: " + LocalTime.now();
        String path = "/example-file.txt";
        server.setRoot(Paths.get("target/test-classes"));
        server.addController(path, new FileController());
        Files.write(Paths.get("target/test-classes/" + path), fileContent.getBytes());
        HttpClient client = new HttpClient("localhost", server.getPort(), path);
        assertEquals(fileContent, client.getMessageBody());
    }
}
