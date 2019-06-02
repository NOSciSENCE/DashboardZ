package org.ys.dashboardz.webssh;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.springframework.web.socket.WebSocketSession;
import org.ys.dashboardz.bean.Server;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;

public class SshClient {
	  // ��������Ϣ
    private Server server;
    // ��ͻ������ӵ�socket�ػ�
    private WebSocketSession socket;
    // ������Ϣ
    private Connection conn = null;
    // term��session��Ϣ
    private Session session = null;
    // �����������д���ݵ��߳�
    private SshWriteThread writeThread = null;

    //д���������
    private BufferedWriter out =  null;
    
    public SshClient(Server server, WebSocketSession socket) {
        super();
        this.server = server;
        this.socket = socket;
    }

    /**
     * ���ӵ�Ŀ�������
     * 
     * @return
     */
    public boolean connect() {
        try {
            String hostname = this.server.getIp();
            String username = this.server.getName();
            String password = this.server.getRootpwd();
            // ��������
            conn = new Connection(hostname, 22);
            // ����
            conn.connect();

            // ��Ȩ
            boolean isAuthenticate = conn.authenticateWithPassword(username, password);
            if (isAuthenticate) {

                // ������
                session = conn.openSession();
                // ��bash
                //session.requestPTY("bash");
                session.requestPTY("xterm", 90, 30, 0, 0, null);

                // ����shell
                session.startShell();

                // ��ͻ���д����
                startWriter();

                // ����� 
                out = new BufferedWriter(new OutputStreamWriter(session.getStdin(), "utf-8")); 

                // ����term
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
    }

    /**
     * ���������д����
     */
    private void startWriter() {
        // �������̣߳�����ȡ�������еĽ��
        // ��һ������ ������
        // �ڶ������� ����������ֱ��������ǿ���̨
        writeThread = new SshWriteThread(session.getStdout(), socket);
        new Thread(writeThread).start();

    }

    /**
     * д���ݵ��������ˣ��û���ִ������
     * @param cmd
     * @return 
     */
    public boolean write(String cmd) {
        try {
            this.out.write(cmd);
            this.out.flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }
    /**
     * �ر�����
     */
    public void disconnect() {
        try {
            //����������˵����ӹرյ���������Ϊ��
            conn.close();
            session.close();
            session = null;
            conn = null;
            writeThread.stopThread();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
