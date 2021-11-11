package no.kristiania.http;

import no.kristiania.http.controllers.Controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;
import java.util.HashMap;


public class HttpServer implements Runnable {
    private ServerSocket serverSocket;
    private final int port;
    private boolean running;
    private Path rootPath;
    private HashMap<String, Controller> controllers = new HashMap<>();

    public HttpServer(int port) {
        this.port = port;
        start();
    }
    public HttpServer() {
        this.port = 0;
        start();
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
        // Need thread to sleep for short while to let server start on separate thread.
        // Tests with maven fails otherwise.
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

    public Controller getController(String path) {
        return controllers.get(path);
    }


}