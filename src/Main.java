import API.HttpTaskServer;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {

        HttpTaskServer httpTaskServer = new HttpTaskServer();
        try {
            httpTaskServer.createServer();
        } catch (IOException e) {
            System.out.println("Исключение");
        }

    }



}
