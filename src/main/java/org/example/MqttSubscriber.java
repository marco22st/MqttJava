package org.example;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.Timer;
import java.util.TimerTask;

public class MqttSubscriber extends Thread {
    private Thread t;
    private String threadName;
    static Double messageCount = 0.0;

    MqttSubscriber() {
        threadName = "MqttSubscriber";
        System.out.println("Creating MqttSubscriber");
    }

    public void run() {
        String topic = "$SYS/#";
        String broker = "tcp://itsp.htl-leoben.at:1883";
        MemoryPersistence persistence = new MemoryPersistence();

        try {
            MqttClient mqttClient = new MqttClient(broker, "subscribe_client", persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            mqttClient.connect(connOpts);
            mqttClient.subscribe(topic);
            mqttClient.setCallback(new MqttCallback() {


                @Override
                public void connectionLost(Throwable cause) {
                    System.out.println("Connection lost!");
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) {
                    messageCount++;
                    System.out.println("Topic: " + topic + "  Message: " + new String(message.getPayload()));
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    try {
                        System.out.println("Delivery complete - " + token.getMessage().toString());
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (MqttException me) {
            me.printStackTrace();
        }

    }

    public void start() {
        System.out.println("Starting " + threadName);
        if (t == null) {
            t = new Thread(this, threadName);
            t.start();
        }
    }
}

