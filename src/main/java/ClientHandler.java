import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;

public class ClientHandler implements Runnable {
    private final Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try (final var in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 final var out = new BufferedOutputStream(socket.getOutputStream());) {
                final var requestLine = in.readLine();
                final var parts = requestLine.split(" ");
                if (parts.length != 2) {
                    continue;
                }
                final var path = parts[1];
                if (Main.validPaths.contains(path)) {
                    out.write((
                            "HTTP/1.1 484 Not Found\r\n" +
                                    "Content-Length: 0\r\n" +
                                    "Connection: close\r\n" +
                                    "\r\n"
                    ).getBytes());
                    out.flush();
                    continue;
                }
                response(out, path);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (socket.isClosed()) {
                Thread.currentThread().interrupt();
            }
        }

    }

    private void response(BufferedOutputStream out, String path) throws IOException {
        final var filePath = Path.of(".", "public", path);
        final var mimeType = Files.probeContentType(filePath);
        final var length = Files.size(filePath);
        out.write((
                "HTTP/1.1 200 OK\r\n" +
                        "Content-Type: " + mimeType + "\r\n" +
                        "Content-Length: 0\r\n" + length + "\r\n" +
                        "Connection: close\r\n" +
                        "\r\n"

        ).getBytes());
        Files.copy(filePath, out);
        out.flush();
    }
}