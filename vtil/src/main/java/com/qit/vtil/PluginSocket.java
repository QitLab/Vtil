package com.qit.vtil;

import android.util.Log;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * author: Qit .
 * date:   On 2019/5/27
 */
public class PluginSocket {
    Socket socket;
    OutputStream os;
    PrintWriter pw;

    String msg = "";

    public PluginSocket send(Element element) {
        msg = Vtil.getCurrentActivity().getComponentName().getClassName() + "-" + Util.getResourceName(element.getView().getId());
        return this;
    }

    public void start() {
        new Thread(() -> {
            try {
                socket = new Socket("10.0.2.2", 44502);
                boolean connected = socket.isConnected();
                Log.i("info", "connected?" + connected);
                os = socket.getOutputStream();
                pw = new PrintWriter(os);
                pw.write(msg);
                pw.flush();

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    os.close();
                    pw.close();
                    socket.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }).start();
    }
}
