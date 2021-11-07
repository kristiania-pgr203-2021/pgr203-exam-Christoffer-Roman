package no.kristiania.http;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HttpClientTest {

    @Test
    void shouldRespond200OK() throws IOException {
        HttpClient client = new HttpClient("httpbin.org", 80, "/html");
        assertEquals(200, client.getResponseCode());
    }

    @Test
    void shouldRespond404NotFound() throws IOException {
        HttpClient client = new HttpClient("httpbin.org", 80, "/not-there");
        assertEquals(404, client.getResponseCode());
    }

    @Test
    void shouldReadHeaders() throws IOException{
        HttpClient client = new HttpClient("httpbin.org", 80, "/html");
        assertEquals("text/html; charset=utf-8", client.getHeader("Content-Type"));
    }

    @Test
    void shouldReadMessageBody() throws IOException {
        var client = new HttpClient("httpbin.org", 80, "/html");

        var expected = "<h1>Herman Melville - Moby-Dick</h1>\n";

        assertTrue(client.getMessageBody().contains(expected));
    }
}
