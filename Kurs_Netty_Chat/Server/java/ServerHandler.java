import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.ArrayList;
import java.util.List;

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
    // Событие отправки сообщения и смены ник-нейма (команда: "/*/nickchange <Новый ник>")
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) {
        System.out.println("Получено сообщение: " + s);
        if (s.startsWith("/")) {
            if (s.startsWith("/*/nickchange ")) {
                String newNick = s.split("\\s", 2)[1];

                if (s.startsWith("/*/nickchange Буй")      //ЦЕНЗУРА!
                        || s.startsWith("/*/nickchange Нехороший человек")
                        || s.startsWith("/*/nickchange Ещё кто")){

                    broadcastMessage("SERVER", "Ник " + newNick +" не соответствует политике чата!");
                }else {
                    broadcastMessage("SERVER", "Участник " + clientNick + " сменил ник на " + newNick);
                    clientNick = newNick;
                }
            }
            return;
        }
        broadcastMessage(clientNick, s);
    }
    
    //Событие вывода сообщения
    public void broadcastMessage(String clientName, String message) {     
        String out = String.format("[%s]: %s\n", clientName, message);
        for (Channel c : channels) {
            c.writeAndFlush(out);
        }
    }

    //Событие выхода участника из чата
    @Override
    public void channelInactive(ChannelHandlerContext ctx) { 
        System.out.println("Участник " + clientNick + " вышел из сети");      
        channels.remove(ctx.channel());
        broadcastMessage("SERVER", "Участник " + clientNick + " вышел из сети");
        ctx.close();
    }

}
