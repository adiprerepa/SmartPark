import com.hopding.jrpicam.*;

import javax.imageio.*;
import java.awt.image.*;
import java.io.*;
import java.net.*;

public class CameraSender {
    private static Socket socket;
    private static RPiCamera camera;
    private static ServerSocket serverSocket;

    public static void main(String[] args) {
        try {
            camera = new RPiCamera();
            System.out.println("~Camera initialized.");
            serverSocket = new ServerSocket(55553);
            socket = serverSocket.accept();
            System.out.println("~Socket connected.");
        } catch (IOException e) {
            System.err.println("!Failed to connect socket...");
            return;
        } catch (com.hopding.jrpicam.exceptions.FailedToRunRaspistillException e) {
            System.err.println("!No camera detected on this device...");
            return;
        }
        while (true) {
            try {
                System.out.println("~Waiting for packet.");
                BufferedReader socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                socketReader.readLine();
                System.out.println("~Recieved packet; Taking image");
                BufferedImage image = camera.takeBufferedStill();
                System.out.println("~Image taken; Sending image");
                ImageIO.write(image, "JPG", socket.getOutputStream());
                System.out.println("~Image sent.");
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
    }
}
