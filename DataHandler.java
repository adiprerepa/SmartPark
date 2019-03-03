import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class DataHandler {

    private Socket rpiSocket;
    private Socket ESP8266_Socket;
    private ServerSocket serverSocket;

    public DataHandler(Socket rpiSocket, Socket ESP8266_Socket) {
        this.rpiSocket = rpiSocket;
        this.ESP8266_Socket = ESP8266_Socket;
    }

    public void recieveImage(File saveTo) {
        BufferedImage image;
        try {
            serverSocket = new ServerSocket(55553);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            rpiSocket = serverSocket.accept();

            System.out.println("Recieved Image");

            image = ImageIO.read(ImageIO.createImageInputStream(rpiSocket.getInputStream()));
            ImageIO.write(image, "jpg", saveTo);

            System.out.println("Wrote image to file.");
        } catch (IOException ioexception) {
            System.out.println("Error with I/O streams");
            ioexception.printStackTrace();
        }
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            rpiSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean getMotionConfirmation() {
        String confirmation = null;
        System.out.println("Waiting for prompts..press y");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        try {
            confirmation = bufferedReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (confirmation.equals("y")) {
            return true;
        } else {
            return false;
        }

    }

    public String recieveCarStatus() {
        String status = null;

        try {

            ESP8266_Socket = serverSocket.accept();
            System.out.println("Accepted Communications with ESP8266");

            BufferedReader socketReader = new BufferedReader(new InputStreamReader(ESP8266_Socket.getInputStream()));

            status = socketReader.readLine();

        } catch (IOException ioexception) {
            System.out.println("Error with IO streams, either accepting sockets or creating buffered reader");
            ioexception.printStackTrace();
        }


        return status;
    }



}
