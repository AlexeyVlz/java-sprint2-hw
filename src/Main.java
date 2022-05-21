import API.HttpTaskServer;
import API.KVServer;
import service.HTTPTaskManager;
import service.Managers;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {

        KVServer kvServer = new KVServer();
        kvServer.start();
        HTTPTaskManager manager = Managers.getDefault(kvServer.getServerURL());









    }



}
