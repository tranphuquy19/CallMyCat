package com.example.tranp.callmycat;

import android.os.AsyncTask;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class Sender extends AsyncTask<String, Void, Void> {
    Socket socket;
    //DataOutputStream dataOutputStream;
    PrintWriter printWriter;
    protected Void doInBackground(String... strings) {
        String message = strings[0];
        String host = strings[1];
        try {
            socket = new Socket(host, 16057);
            printWriter = new PrintWriter(socket.getOutputStream());
            printWriter.write(message);
            printWriter.flush();
            printWriter.close();
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
