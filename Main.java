public class Main {

    public static void main(String[] args) {

        DataHandler handler = new DataHandler("10.178.82.64", 55555);

        while (true) {
            handler.receiveImage();
        }

    }
}
