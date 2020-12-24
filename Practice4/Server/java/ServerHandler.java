import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class ServerHandler extends SimpleChannelInboundHandler<String> {
    private static final List<Channel> channels = new ArrayList<>();
    private static int newClientIndex = 1;
    private String clientNick;

    @Override
    // Событие подключения(добовления) участника
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("Участник подключился: " + ctx);
        channels.add(ctx.channel());
        clientNick = "Участник " + newClientIndex;
        newClientIndex++;
        broadcastMessage("SERVER", "Подключился новый участник: " + clientNick);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) {
        System.out.println("Получено сообщение: " + s);
        int result;
            // разбить уравнение на операнды и операцию
            StringTokenizer st = new StringTokenizer(s);
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
            broadcastMessage("SERVER",  clientNick + " Ваш ответ: " + result + ".");

    }

    public void broadcastMessage(String clientName, String message) {
        String out = String.format("[%s]: %s\n", clientName, message);
        for (Channel c : channels) {
            c.writeAndFlush(out);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception { //Событие, запускаемое при выходе
        System.out.println("Участник " + clientNick + " вышел из сети");      // участника
        channels.remove(ctx.channel());
        broadcastMessage("SERVER", "Участник " + clientNick + " вышел из сети");
        ctx.close();
    }

}