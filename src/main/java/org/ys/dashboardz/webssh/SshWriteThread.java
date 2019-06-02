package org.ys.dashboardz.webssh;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

public class SshWriteThread implements Runnable {
    //����һ��flag,��ֹͣ�߳���
    private boolean isStop = false;

    //��������������
    private InputStream in; 

    //�����������
    private WebSocketSession session;

    private static final String ENCODING = "UTF-8";

    //ֹͣ�߳�
    public void stopThread() {
        this.isStop = true;
    }


    public SshWriteThread(InputStream in, WebSocketSession session) {
        super();
        this.in = in;
        this.session = session;
    }

    public void run() {
        try {
            //��ȡ����
            while (!isStop  &&                       //�߳��Ƿ�ֹͣ
                    session != null &&                //session ���ǿյ�
                    session.isOpen()) {               //session�Ǵ򿪵�״̬
                //��ȡ�����ǵ�session
//              session.sendMessage(new TextMessage(new String(result.toString().getBytes("ISO-8859-1"),"UTF-8")));
                //д���ݵ���������
//              session.sendMessage(new TextMessage(result));

                //д���ݵ��ͻ���
                writeToWeb(in);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * д���ݵ�web���ƽ���
     * @param in
     */
    private void writeToWeb(InputStream in) {

        try {
            //����һ������
            //һ��UDP ���û����ݱ��������ֶγ���Ϊ8192�ֽ�
            byte [] buff = new byte[8192];  

            int len =0;
            StringBuffer sb = new StringBuffer();
            while((len = in.read(buff)) > 0) {
                //�趨��0 ��ʼ
                sb.setLength(0);

                //��ȡ������������ݣ����в���
                for (int i = 0; i < len; i++){
                    //���в������
                    char c = (char) (buff[i] & 0xff);
                    sb.append(c);
                }
                //д���ݵ���������
                session.sendMessage(new TextMessage(new String(sb.toString().getBytes("ISO-8859-1"),"UTF-8")));
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
