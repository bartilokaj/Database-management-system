package pl.blokaj.dbms.app;

import java.io.IOException;

public class App {
    public static void main(String[] args) throws IOException {
        try (Server server = new Server()) {
            server.runServer();
        }
    }
}
