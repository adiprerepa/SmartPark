import com.hopding.jrpicam.*;

import javax.imageio.*;
import java.io.*;
import java.net.*;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

class ImageRotator {
    static BufferedImage Rotate(
            BufferedImage image)
    {
        BufferedImage newImage = new BufferedImage(
                image.getWidth(), image.getHeight(),
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = newImage.createGraphics();
        g.transform(AffineTransform.getRotateInstance(Math.PI, image.getWidth() / 2.0, image.getHeight() / 2.0));
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return newImage;
    }
}

public class CameraSender {
    private static RPiCamera camera;
    private static ServerSocket serverSocket;
    private static Socket recieveSocket;
    private static Socket sendSocket;

    public static void main(String[] args) {

        try {
            serverSocket = new ServerSocket(55553);
            camera = new RPiCamera();
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        while (true) {
            try {
                System.out.println("~Waiting for packet.");
                recieveSocket = serverSocket.accept();
                BufferedReader socketReader = new BufferedReader(new InputStreamReader(recieveSocket.getInputStream()));
                socketReader.readLine();
                System.out.println("~Recieved packet; Taking image");
                recieveSocket.close();
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
            try {
                sendSocket = new Socket("10.178.82.64", 55553);
                BufferedImage image = ImageRotator.Rotate(camera.takeBufferedStill());
                System.out.println("~Image taken; Sending image");
                ImageIO.write(image, "JPG", sendSocket.getOutputStream());
                System.out.println("~Image sent.");
                sendSocket.close();
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
    }
}
