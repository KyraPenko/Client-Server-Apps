import java.io.*;
import java.net.*;
import java.util.Scanner;

public class EchoClient {
    public static void main(String[] args) throws IOException {

        if (args.length != 2) {
            System.err.println(
                    "Usage: java EchoClient <host name> <port number>");
            System.exit(1);
        }

        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);

        Scanner sc = new Scanner(System.in);
        Socket s = new Socket(hostName, portNumber);

        // Шаг 2: Связь - получить поток ввода и вывода
        DataInputStream input = new DataInputStream(s.getInputStream());
        DataOutputStream output = new DataOutputStream(s.getOutputStream());

        while (true)
        {
            // Введите уравнение в форме
            // "Операция операнда1 Операнд2"
            System.out.print("Enter the equation in the form: ");
            System.out.println("'operand operator operand'");

            String inp = sc.nextLine();
            if (inp.equals("exit"))
                break;



            // отправить уравнение на сервер
            output.writeUTF(inp);



            // дождемся, пока запрос будет обработан и отправлен обратно клиенту
            String ans = input.readUTF();
            System.out.println("Answer=" + ans);

        }

    }
}