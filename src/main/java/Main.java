import java.io.*;
import java.util.List;

public class Main {
    public static List<String> validPaths = List.of("/index.html", "/spring.svg", "/spring.png");
    public static void main(String[] args) throws IOException {
      new Server(9999).listen();

    }
}
