import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {


    ServerSocket serverSocket;
    String webSiteAddress;
    int webSitePort;
    String ESP8266_Addr;
    int ESP8266_port;
    String RPI_Addr;
    int RPI_port;
    String mySQL_user;
    String mySQL_password;
    Socket rpiSocket;
    Socket ESP8266_Socket;


    public Server(String RPI_Addr, int RPI_port) {
        this.RPI_Addr = RPI_Addr;
        this.RPI_port = RPI_port;
    }
//    public Server(String webSiteAddress, int webSitePort) {
//        this.webSiteAddress = webSiteAddress;
//        this.webSitePort = webSitePort;
//    }

    public Server(String webSiteAddress, int webSitePort, String ESP8266_Addr, int ESP8266_port) {
        this(webSiteAddress, webSitePort);
        try {
             serverSocket = new ServerSocket(55554);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.ESP8266_Addr = ESP8266_Addr;
        this.ESP8266_port = ESP8266_port;
    }

    public Server(String webSiteAddress, int webSitePort, String ESP8266_Addr, int ESP8266_port, String RPI_Addr, int RPI_port) {
        this(webSiteAddress, webSitePort, ESP8266_Addr, ESP8266_port);
        try {
            serverSocket = new ServerSocket(55554);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.RPI_Addr = RPI_Addr;
        this.RPI_port = RPI_port;
    }

    public Server(String webSiteAddress, int webSitePort, String RPI_Addr, int RPI_port, String mySQL_user, String mySQL_password) {
        this.webSiteAddress = webSiteAddress;
        this.webSitePort = webSitePort;
        this.RPI_Addr = RPI_Addr;
        this.RPI_port = RPI_port;
        this.webSiteAddress = webSiteAddress;
        this.webSitePort = webSitePort;
        this.mySQL_user = mySQL_user;
        this.mySQL_password = mySQL_password;
    }



    public void runMain() {
        File file = new File("/home/aditya/pic/img.jpg");
        DataSender dataSender = new DataSender(webSiteAddress, webSitePort, mySQL_user, mySQL_password, RPI_Addr, RPI_port);
        DataHandler dataHandler = new DataHandler(rpiSocket, ESP8266_Socket);

        //dataHandler.recieveCarStatus();
        while (true) {

            if (dataHandler.getMotionConfirmation()) {
                dataSender.sendPromptToRPI();
                dataHandler.recieveImage(file);
                dataSender.commitToDatabase("Car there");
                dataSender.sendImageToWebsite(file);

            }
        }
    }
}
