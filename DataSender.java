import com.mysql.jdbc.Connection;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DataSender {

    private Connection con;

    private int x;
    private String webSiteAddress;
    private int webSitePort;
    private String mySQL_URL = "jdbc:mysql://localhost/WebCamImages";
    private String mySQL_Password;
    private String mySQL_user;
    private String RPI_Addr;
    private int RPI_port;


    public void sendImageToWebsite(File file) {
        BufferedImage image = null;
        ServerSocket serverSocket = null;

        try {
            image = ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            serverSocket = new ServerSocket(55555);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.printf("Image trying to send to %s : %d", webSiteAddress, webSitePort);
        try {
            assert serverSocket != null;
            Socket socket = serverSocket.accept();
            ImageIO.write(image, "JPG", socket.getOutputStream());

        } catch (UnknownHostException e) {
            System.out.println("Host not in existed ");
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("\nSent image to website!");
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public DataSender(String webSiteAddress, int webSitePort, String mySQL_user, String mySQL_Password, String RPI_Addr, int RPI_port) {
        this.webSiteAddress = webSiteAddress;
        this.webSitePort = webSitePort;
        this.mySQL_Password = mySQL_Password;
        this.mySQL_user = mySQL_user;
        this.RPI_Addr = RPI_Addr;
        this.RPI_port = RPI_port;
    }

    public void commitToDatabase(String carStatus) {

        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = (Connection) DriverManager.getConnection(mySQL_URL,mySQL_user,mySQL_Password);

            con.setAutoCommit(false);
        } catch (ClassNotFoundException e) {
            System.out.println("Class Not found or DriverManager not init.");
            e.printStackTrace();

        } catch (SQLException sqlException) {
            System.out.println("Error with MySQL");
            sqlException.printStackTrace();
        }

        String INSERT_STATEMENT = "INSERT INTO RESULTS(null,?,?)";


        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String time = sdf.format(cal.getTime());
        try {
            PreparedStatement ps = con.prepareStatement(INSERT_STATEMENT);
            ps.setString(2, carStatus);
            ps.setString(3, time);
        } catch (SQLException e) {
            System.out.println("Committed To mySQL successfully!");

        }
    }

    public void sendPromptToRPI() {

        Socket rpiSocket = null;
        try {
            rpiSocket = new Socket(RPI_Addr, RPI_port);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Connected To RPI as client");

        try {
            rpiSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendInformationToWebsite() {

    }


}
