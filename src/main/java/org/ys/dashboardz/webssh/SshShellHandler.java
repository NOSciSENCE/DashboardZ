package org.ys.dashboardz.webssh;

import java.util.Arrays;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.ys.dashboardz.bean.Server;

@RequestMapping("/webssh/sshShellHandler")
public class SshShellHandler extends TextWebSocketHandler{
	//�ͻ���
    SshClient client = null;

    //�ر����Ӻ�Ĵ���
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // TODO Auto-generated method stub
        super.afterConnectionClosed(session, status);

        //�ر�����
        if (client != null) {
            client.disconnect();
        }
    }

    //����socket����
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // TODO Auto-generated method stub
        super.afterConnectionEstablished(session);
        //������û������ʱ�򣬿���������ط��� ,�ж��û�id��
        
        //���÷�������Ϣ
        Server server = new Server("ip","root","pwd");
        client = new SshClient(server, session);
        client.connect();
    }
    //����socker���͹�������Ϣ
    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        super.handleMessage(session, message);

        //��������
        try {
            TextMessage msg = (TextMessage) message;
            //���ͻ��˲�Ϊ�յ����
            if (client != null) {
                //receive a close cmd
                if (Arrays.equals("exit".getBytes(), msg.asBytes())) {
                    if (client != null) {
                        client.disconnect();
                    }
                    session.close();
                    return ;
                }
                //д��ǰ̨���ݹ�����������͵�Ŀ���������
                if (msg.asBytes().length!=0) {
                	client.write(new String(msg.asBytes(), "UTF-8"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            session.sendMessage(new TextMessage("An error occured, websocket is closed."));
            session.close();
        }
    }
}
