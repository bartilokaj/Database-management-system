package pl.blokaj.dbms.app;

import java.io.IOException;

public class App {
    public static void main(String[] args) throws IOException {
        try (Server server = new Server()) {
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    server.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }));

            server.runServer();
        }
    }
}
