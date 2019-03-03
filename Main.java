
public class Main {
    public static void main(String[] args) {
        String RPI_Addr = "10.178.82.99";
        int RPI_port = 55553;
        String mySQL_user = "adi";
        String mySQL_password = "adityapc";
        String webSiteAddress = "10.178.82.58";
        int webSitePort = 55555;
        Server server = new Server(webSiteAddress, webSitePort, RPI_Addr, RPI_port, mySQL_user, mySQL_password);
        server.runMain();
    }
}



