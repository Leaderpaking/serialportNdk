package com.sd.zy.seriaport;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import zyserialport.SerialPort;

public class MainActivity extends AppCompatActivity {

    private String path = "/dev/ttyMT1";
    private int baudrate = 115200;
    private String TAG = "MainActivity";
    public boolean serialPortStatus = false; //是否打开串口标志
    public boolean threadStatus; //线程状态，为了安全终止线程

    public SerialPort serialPort = null;
    public InputStream inputStream = null;
    public OutputStream outputStream = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        openSerialPort();
    }

    /**
     * 打开串口
     *
     * @return serialPort串口对象
     */
    public SerialPort openSerialPort() {
        try {
            serialPort = new SerialPort(new File(path), baudrate, 0);
            this.serialPortStatus = true;
            threadStatus = false; //线程状态

            //获取打开的串口中的输入输出流，以便于串口数据的收发
            inputStream = serialPort.getInputStream();
            outputStream = serialPort.getOutputStream();

            new ReadThread().start(); //开始线程监控是否有数据要接收
        } catch (IOException e) {
            Log.e(TAG, "openSerialPort: 打开串口异常：" + e.toString());
            return serialPort;
        }
        Log.d(TAG, "openSerialPort: 打开串口");
        return serialPort;

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
                    size = inputStream.read(buffer);
                    if (size > 0) {
                        Log.d(TAG, "run: 接收到了数据：" + FormatUtil.bytesToHexString(buffer));
                        Log.d(TAG, "run: 接收到了数据大小：" + String.valueOf(size));
                    }
                } catch (IOException e) {
                    Log.e(TAG, "run: 数据读取异常：" + e.toString());
                }
            }

        }
    }
}