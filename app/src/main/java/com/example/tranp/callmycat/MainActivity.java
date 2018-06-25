package com.example.tranp.callmycat;

import android.annotation.SuppressLint;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.NetworkOnMainThreadException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.Toast;


import com.example.tranp.model.Command;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Formatter;

public class MainActivity extends AppCompatActivity {
    static Socket mySocket;

    Spinner spn1, spn2;
    ArrayList<String> dsCongViec, dsThoiGian;
    ArrayAdapter<String> adapterCongViec, adapterThoiGian;
    Button btnOK, btnSet;
    ImageButton btnSend;
    EditText txtConsole, txtCommand, txtTimecd;
    LayoutInflater layoutInflater;
    PopupWindow popupWindow;
    View container;
    Command cmd;
    ListView lvCmd;
    ArrayList<String> dsLenh = new ArrayList<String>();
    ArrayAdapter adapterLv;

    JSONObject jsonObject9;
    JSONArray jsonArray9;

    int choose1 = 0, choose2 = 0;
    String serverHost = "";
    String serverPort = "";
    String timeCountDown = "";
    String time = "3600";
    String myIP = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createAdapter();
        readHost();
        addControls();
        addEvents();

        @SuppressLint("WifiManagerLeak") WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ip1 = wifiInfo.getIpAddress();

        myIP = String.format("%d.%d.%d.%d", (ip1 & 0xff), (ip1 >> 8 & 0xff), (ip1 >> 16 & 0xff), (ip1 >> 24 & 0xff));
        Log.v("Thong bao", "ip:" + myIP);

        Thread thread = new Thread(new MyClient());
        thread.start();


        if ("".equals(serverHost) || "".equals(serverPort)) {
            addInfoServer();
        }else{
            addTextToConsole("\n Gõ help để xem hướng dẫn...");
        }
    }

    private void createAdapter() {
        String txtJson1, txtJson = "";
        try {
            InputStream inputStream = getAssets().open("data_listview.txt");
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuilder = new StringBuilder();
            if(inputStream != null)
            {
                txtJson1 = bufferedReader.readLine();
                stringBuilder.append(txtJson1);
                txtJson = stringBuilder.toString();
                Log.v("Thong bao", "JSON is:"+txtJson);
                inputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            jsonObject9 = new JSONObject(txtJson);
            Log.v("Thog Bao", jsonObject9.toString());
            jsonArray9 = (JSONArray) jsonObject9.getJSONArray("List Cmd");
            Log.v("So luong", Integer.toString(jsonArray9.length()));
            for(int i = 0; i<jsonArray9.length(); i++)
            {
                JSONObject jsonObject1 = jsonArray9.getJSONObject(i);
                Log.v("Thog baonull", jsonObject1.getString("Mo Ta"));
                dsLenh.add(jsonObject1.get("Mo Ta").toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        adapterLv = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, dsLenh);
    }

    /*
    Lắng nghe từ server!
     */
    class MyClient implements Runnable {
        Socket socketClient;
        ServerSocket serverSocketClient;
        InputStreamReader inputStreamReaderClient;
        BufferedReader bufferedReaderClient;
        String messageClient = "";


        @Override
        public void run() {
            try {
                serverSocketClient = new ServerSocket(16058);
                while (true) {
                    socketClient = serverSocketClient.accept();
                    inputStreamReaderClient = new InputStreamReader(socketClient.getInputStream());
                    bufferedReaderClient = new BufferedReader(inputStreamReaderClient);
                    messageClient = bufferedReaderClient.readLine();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            txtConsole.setText(txtConsole.getText().toString() + "\n" + messageClient);
                        }
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /*
    Khởi tạo thông tin server
     */
    private void addInfoServer() {
        this.serverHost = "";
        this.serverPort = "";
        if ("".equals(serverHost) || "".equals(serverPort)) {
            if ("".equals(serverHost)) {
                txtConsole.setText("Enter your Server Host: ");
                txtCommand.setHint("Enter your server host here");
            } else {
                String temp = txtConsole.getText().toString();
                txtConsole.setText(temp + "\nEnter your Server Port: ");
                txtCommand.setHint("Enter your server port here");
            }
        }
    }

    /*
    Khởi tạo Command
     */
    private void khoiTaoCommand() {
        cmd = new Command();
        String cmdString = "";
        String moTa = "";
        switch (choose1) {
            case 0:
                cmdString = "shutdown -s ";
                moTa = "Máy tính sẽ Shutdown trong";
                break;
            case 1:
                cmdString += "shutdown /h";
                moTa = "Máy tính sẽ Ngủ đông trong";
                break;
            case 2:
                cmdString = "shutdown -r ";
                moTa = "Máy tính sẽ Khởi động lại trong";
                break;
            case 3:
                cmdString = "logoff";
                moTa = "Máy tính sẽ Đăng xuất trong";
                break;
            case 4:
                cmdString = "shutdown -a";
                moTa = "Lệnh đã được thu hồi";
                break;
            default:
                break;
        }
        if (choose1 != 1 && choose1 != 3 && choose1 != 4) {
            switch (choose2) {
                case 0:
                    cmdString += "-t 00";
                    moTa += " 0 giây";
                    break;
                case 1:
                    cmdString += "-t 900";
                    moTa += " 15 phút";
                    break;
                case 2:
                    cmdString += "-t 1800";
                    moTa += " 30 phút";
                    break;
                case 3:
                    cmdString += "-t 3600";
                    moTa += " 1 giờ";
                    break;
                case 4:
                    cmdString += "-t 10800";
                    moTa += " 3 giờ";
                    break;
                case 5:
                    cmdString += "-t 21600";
                    moTa += " 6 giờ";
                    break;
                case 6:
                    showPopupTimme();
                    cmdString += "-t " + time;
                    break;
                default:
                    break;
            }
        }
        cmd.setCmd(cmdString);
        cmd.setMoTa(moTa);
    }

    /*
    Hiện Popup thời gian tùy chọn
     */
    private void showPopupTimme() {
        layoutInflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        container = (View) layoutInflater.inflate(R.layout.popup_time, null);
        popupWindow = new PopupWindow(container, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
        txtTimecd = (EditText) container.findViewById(R.id.txtTime);
        popupWindow.showAtLocation(container, Gravity.CENTER, 0, 0);
        ((Button) container.findViewById(R.id.btnSet)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    time = txtTimecd.getText().toString();
                } catch (Exception ex) {
                    addTextToConsole("\nLỗi");
                    Toast.makeText(MainActivity.this, "Không được để trống chứ :)", Toast.LENGTH_LONG).show();
                }
                if ("".equals(time)) {
                    Toast.makeText(MainActivity.this, "Không được để trống chứ :)", Toast.LENGTH_LONG).show();
                    addTextToConsole("\nSet Time Countdown Error!");
                } else {
                    popupWindow.dismiss();
                    addTextToConsole("\nSet time countdown Success!");
                }
            }
        });
    }

    private void addEvents() {
        txtConsole.setSelection(txtConsole.getText().length());
        spn1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                choose1 = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spn2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                choose2 = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        /*
        Event Button OK
         */
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                khoiTaoCommand();
                executeCommand(cmd);
            }
        });
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("".equals(serverHost) || "".equals(serverPort)) {
                    if ("".equals(serverHost)) {
                        setServerHost();
                        addTextToConsole("\nSet Server Host Success!");
                        addTextToConsole("\nEnter your Server Port: ");
                        txtCommand.setHint("Enter your server port here");
                        txtCommand.setText("16057");
                    } else {
                        setServerPort();
                        addTextToConsole("\nSet Server Port Success!");
                        addTextToConsole("\nServer Info:\nHost: " + serverHost + "\nPort: " + serverPort);
                        txtCommand.setText("");
                        btnOK.setEnabled(true);
                        txtCommand.setHint("Enter your command here");
                    }
                } else {
                    String str = txtCommand.getText().toString();
                    if("help".equals(str) || "HELP".equals(str) || "config".equals(str) || "clear".equals(str))
                    {
                        if("clear".equals(str)){
                            txtConsole.setText("");
                            txtCommand.setText("");
                        }
                        else if("config".equals(str))
                        {
                            addInfoServer();
                            txtCommand.setText("");
                        }else{
                            addTextToConsole("\nKhởi chạy ứng dụng server trên máy tính\nĐiện thoại và máy tính phải kết nối\n cùng 1 mạng wifi\nCấu hình cấu hình server như thông tin\n trên máy tính hiển thị\nGõ 'config' nếu muốn cấu hình lại!\nGõ 'clear' nếu muốn xóa console\n--Được tạo bởi @tranphuquy19--");
                            txtCommand.setText("");
                        }
                    }else{
                        executeCommand(str);
                        txtCommand.setText("");
                    }
                }
                savedHost();
            }
        });
        lvCmd.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Command cmd = new Command();
                try {
                    cmd.setCmd(jsonArray9.getJSONObject(position).getString("CMD"));
                    cmd.setMoTa(jsonArray9.getJSONObject(position).getString("Mo Ta"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                executeCommand(cmd);
            }
        });
    }

    /*
    Xử lí Object Command
     */
    private void executeCommand(String cmd) {
        Command cmd2 = new Command();
        cmd2.setCmd(cmd);
        cmd2.setMoTa("Custom Command");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("Command", cmd2.getCmd());
            jsonObject.put("Mo Ta", cmd2.getMoTa());
            jsonObject.put("IP", myIP);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Sender sender = new Sender();
        sender.execute(jsonObject.toString(), serverHost);
        Log.v("Thong Bao", jsonObject.toString());
        addTextToConsole("\nSend Command: " + cmd);
    }

    private void executeCommand(Command cmd) {
        Sender sender = new Sender();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("Command", cmd.getCmd());
            jsonObject.put("Mo Ta", cmd.getMoTa());
            jsonObject.put("IP", myIP);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        sender.execute(jsonObject.toString(), serverHost);
        Log.v("Thong Bao", jsonObject.toString());
        addTextToConsole("\n" + cmd.getCmd());
        addTextToConsole("\nCommand were sent...");
    }

    private void addControls() {
        TabHost tabHost = findViewById(R.id.tabHost);
        tabHost.setup();

        TabHost.TabSpec tab1 = tabHost.newTabSpec("basic");
        tab1.setIndicator("Basic");
        tab1.setContent(R.id.tab1);
        tabHost.addTab(tab1);

        TabHost.TabSpec tab2 = tabHost.newTabSpec("advanced");
        tab2.setIndicator("Advanced");
        tab2.setContent(R.id.tab2);
        tabHost.addTab(tab2);

        spn1 = (Spinner) findViewById(R.id.spn1);
        spn2 = (Spinner) findViewById(R.id.spn2);

        btnOK = (Button) findViewById(R.id.btnOK);
        if ("".equals(serverHost) && "".equals(serverPort)) {
            btnOK.setEnabled(false);
        }
        btnSend = (ImageButton) findViewById(R.id.btnSend);

        dsCongViec = new ArrayList<>();
        dsCongViec.add("Shutdown");
        dsCongViec.add("Hibernate");
        dsCongViec.add("Restart");
        dsCongViec.add("Logoff");
        dsCongViec.add("Hủy lệnh trước đó");

        dsThoiGian = new ArrayList<>();
        dsThoiGian.add("Ngay bây giờ");
        dsThoiGian.add("15 phút");
        dsThoiGian.add("30 phút");
        dsThoiGian.add("1 giờ");
        dsThoiGian.add("3 giờ");
        dsThoiGian.add("6 giờ");
        dsThoiGian.add("Tùy chọn...");

        adapterCongViec = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, dsCongViec);
        adapterThoiGian = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, dsThoiGian);


        adapterCongViec.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterThoiGian.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spn1.setAdapter(adapterCongViec);
        spn2.setAdapter(adapterThoiGian);

        txtConsole = (EditText) findViewById(R.id.txtConsole);
        txtCommand = (EditText) findViewById(R.id.txtcmd);
        lvCmd = (ListView) findViewById(R.id.lvCmd);
        lvCmd.setAdapter(adapterLv);
    }

    private void addTextToConsole(String txt) {
        txtConsole.setText(txtConsole.getText().toString() + txt);
    }

    private void setServerHost() {
        serverHost = txtCommand.getText().toString();
        addTextToConsole(serverHost);
    }
    private void savedHost()
    {
        try{
            FileOutputStream fileOutputStream = this.openFileOutput("host.txt", MODE_PRIVATE);
            fileOutputStream.write(serverHost.getBytes());
            Log.v("Thong bao", serverHost);
            fileOutputStream.close();
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
    private void readHost(){
        try{
            FileInputStream fileInputStream =this.openFileInput("host.txt");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String s = "";
            s = bufferedReader.readLine();
            stringBuilder.append(s);
            this.serverHost = stringBuilder.toString();
            Log.v("Thong Bao file", serverHost);
            this.serverPort = "16057";
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
    private void setServerPort() {
        serverPort = txtCommand.getText().toString();
        addTextToConsole(serverPort);
    }


}

