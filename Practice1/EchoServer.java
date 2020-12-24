import java.net.*;
import java.io.*;
import java.util.StringTokenizer;

public class EchoServer {
    public static void main(String[] args) throws IOException {

        if (args.length != 1) {
            System.err.println("Usage: java EchoServer <port number>");
            System.exit(1);
        }


        int portNumber = Integer.parseInt(args[0]);

                ServerSocket serverSocket = new ServerSocket(portNumber);
                Socket s = serverSocket.accept();

                DataInputStream dis = new DataInputStream(s.getInputStream());
                DataOutputStream dos = new DataOutputStream(s.getOutputStream());

            while (true) {

                String input = dis.readUTF(); // ждать ввода

                if (input.equals("exit"))
                    break;

                System.out.println("Equation received: " + input);
                int result;

                // разбить уравнение на операнды и операцию
                StringTokenizer st = new StringTokenizer(input);
                int opd1 = Integer.parseInt(st.nextToken());
                String operation = st.nextToken();
                int opd2 = Integer.parseInt(st.nextToken());

                // выполнить требуемую операцию.
                if (operation.equals("+")) {
                    result = opd1 + opd2;
                } else if (operation.equals("-")) {
                    result = opd1 - opd2;
                } else if (operation.equals("*")) {
                    result = opd1 * opd2;
                } else {
                    result = opd1 / opd2;
                }

                System.out.println("Sending the result...");

                dos.writeUTF(Integer.toString(result));    // отправить результат обратно клиенту

            }
        }
    }
