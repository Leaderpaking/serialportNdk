package com.sd.zy.seriaport;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.sd.zy.seriaport.Util.GPSDataEntity;
import com.sd.zy.seriaport.Util.GPSDataParse;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import zyserialport.SerialPort;

public class MainActivity extends AppCompatActivity {

    private String path = "/dev/ttyS0";
    private int baudrate = 9600;
    private String TAG = "MainActivity";
    public boolean serialPortStatus = false; //是否打开串口标志
    public boolean threadStatus; //线程状态，为了安全终止线程

    public SerialPort serialPort = null;
    public InputStream inputStream = null;
    public OutputStream outputStream = null;
    private String gpsLin = "";
    private TextView textView;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            GPSDataEntity parse = GPSDataParse.parse(gpsLin);
            textView.setText("纬度"+parse.getLatitude()+"\n经度"+parse.getLongitude());
            handler.sendEmptyMessageDelayed(0, 5000);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.sample_text);

        openSerialPort();


    }

    /**
     * 打开串口
     *
     * @return serialPort串口对象
     */
    public void openSerialPort() {
        try {

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        serialPort = new SerialPort(new File(path), baudrate, 0);
                        serialPortStatus = true;
                        threadStatus = false; //线程状态

                        //获取打开的串口中的输入输出流，以便于串口数据的收发
                        inputStream = serialPort.getInputStream();
                        outputStream = serialPort.getOutputStream();

                        new ReadThread().start(); //开始线程监控是否有数据要接收
                        Log.d(TAG, "openSerialPort: 打开串口");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            handler.sendEmptyMessageDelayed(0, 5000);
        } catch (Exception e) {
            Log.e(TAG, "openSerialPort: 打开串口异常：" + e.toString());
        }


    }

    /**
     * 单开一线程，来读数据
     */
    private class ReadThread extends Thread {
        @Override
        public void run() {
            super.run();
            //判断进程是否在运行，更安全的结束进程
            while (!threadStatus) {
                Log.d(TAG, "进入线程run");
                //64   1024
                byte[] buffer = new byte[1024];
                int size; //读取数据的大小
                try {

//                    size = inputStream.read(buffer);
//                    if (size > 0) {
////                        Log.d(TAG, "run: 接收到了数据：" + buffer);
////                        Log.d(TAG, "run: 接收到了数据大小：" + String.valueOf(size));
//                        GPSDataEntity parse = GPSDataParse.parse(buffer);
//                        Log.d(TAG, "run: 经度：" + parse.getLongitude());
//                        Log.d(TAG, "run: 纬度：" + parse.getLatitude());
//                    }

                    BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
                    String line = " ";
                    while ((line = in.readLine()) != null) {
                        if (line.startsWith("$GNGGA")) {
                            Log.e(TAG, line);
                            gpsLin = line;
                        }
                    }


                } catch (IOException e) {
                    Log.e(TAG, "run: 数据读取异常：" + e.toString());
                }
            }

        }
    }
}