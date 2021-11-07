package no.kristiania.http;

import no.kristiania.dao.QuestionDao;
import no.kristiania.http.controllers.Controller;
import no.kristiania.http.controllers.QuestionsController;
import org.flywaydb.core.internal.jdbc.DriverDataSource;
import org.postgresql.ds.PGSimpleDataSource;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;
import java.util.HashMap;


public class HttpServer implements Runnable {
    private ServerSocket serverSocket;
    private final int port;
    private boolean running;
    private Path rootPath = Path.of("src/main/resources");
    private HashMap<String, Controller> controllers = new HashMap<>();

    public HttpServer(int port) {
        this.port = port;
        start();
    }
    public HttpServer() {
        this.port = 0;
        start();
    }

    public static void main(String[] args) {
        HttpServer server = new HttpServer(8000);
        QuestionDao dao = new QuestionDao(new PGSimpleDataSource());
        server.addController("/questions.html", new QuestionsController(dao, server));
    }

    public boolean containsPath(String path) {
        return controllers.containsKey(path);
    }

    public void run() {
        openServerSocket();
        Socket socket = null;
        while (running) {
            try {
                socket = serverSocket.accept();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            new Thread(new HttpWorker(this, socket)).start();
        }
    }

    private void openServerSocket() {
        try {
            serverSocket = new ServerSocket(port);
            running = true;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public int getPort() {
        return serverSocket.getLocalPort();
    }

    public void start() {
        new Thread(this).start();
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }

    }

    public void setRoot(Path rootPath) {
        this.rootPath = rootPath;
    }

    public Path getRootPath(){
        return this.rootPath;
    }

    public void addController(String path, Controller controller) {
        controllers.put(path, controller);
    }

    public Controller GetController(String path) {
        return controllers.get(path);
    }

}