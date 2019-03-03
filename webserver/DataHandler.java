/**
 * @author Aditya Prerepa
 * DataHandler Class, recieves data from server
 */

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;

public class DataHandler {

    private Socket socket;
    private String serverAddress;
    private int serverPort;

    public DataHandler(String serverAddress, int serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;

    }

    public void receiveImage() {
        Socket socket = null;
        try {
            socket = new Socket(serverAddress, serverPort);
        } catch (IOException e) {
            e.printStackTrace();
        }
        File writeToFile = new File("/Users/ishan/webserver/image.jpg");
        BufferedImage img;

        try {
            img = ImageIO.read(socket.getInputStream());
            System.out.printf("Recieved image from %s: %d", serverAddress, serverPort);
            ImageIO.write(img, "JPG", writeToFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Wrote image to file.");
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void receiveEntries()  {
        File entries = new File("/Users/ishan/webserver/time.txt");
        FileWriter fw = null;

        String receiveAndWrite = null;
        BufferedReader socketReader = null;
        try {
            socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fw = new FileWriter(entries);
        } catch (IOException e) {
            e.printStackTrace();
        }
        while(true) {
            try {
                receiveAndWrite = socketReader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (receiveAndWrite.equals("done")) {
                return;
            }
            try {
                fw.write(receiveAndWrite);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
