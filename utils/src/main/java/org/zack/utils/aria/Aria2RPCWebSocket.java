package org.zack.utils.aria;

import org.java_websocket.client.WebSocketClient;

import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

import java.net.URISyntaxException;

public class Aria2RPCWebSocket extends WebSocketClient {

    public Aria2RPCWebSocket(String url) throws URISyntaxException {

        super(new URI(url));

    }

    @Override

    public void onOpen(ServerHandshake shake) {

        System.out.println("已经连接...");

    }

    @Override

    public void onMessage(String s) {

    }

    @Override

    public void onClose(int i, String s, boolean b) {

    }

    @Override

    public void onError(Exception e) {

    }

    private static Aria2RPCWebSocket client;

    public static void main(String[] args) {

        try {

// 创建 WebSocket 客户端

            client = new Aria2RPCWebSocket("ws://127.0.0.1:6800/jsonrpc");

// 连接 WebSocket 服务器

            client.connect();

            System.out.println(client.getReadyState());

// 当服务器连接上时，发送 Json 数据

            while (!client.isOpen()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {

                }
            }

// 开始下载

            String url = "需要下载的文件地址";

            String filePath = "Aira2的下载位置";

            client.send("{\n" +

                    "    \"jsonrpc\":\"2.0\",\n" +

                    "    \"id\":\"qwer\",\n" +

                    "    \"method\":\"aria2.addUri\",\n" +

                    "    \"params\":[\n" +

                    "        [\n" +

                    "            \"" + "" + url + "\"\n" +

                    "        ],\n" +

                    "        {\n" +

                    "            \"dir\":\"" + "" + filePath + "\"\n" +

                    "        }\n" +

                    "    ]\n" +

                    "}");

        } catch (URISyntaxException e) {

            e.printStackTrace();

        } finally {

            client.close();

        }

    }

}
