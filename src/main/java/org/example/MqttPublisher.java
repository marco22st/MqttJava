package org.example;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.Timer;
import java.util.TimerTask;

public class MqttPublisher extends Thread {
    private Thread t;
    private String threadName;

    MqttPublisher() {
        threadName = "MqttPublisher";
        System.out.println("Creating MqttPublisher");
    }

    public void run() {
        try {
            Thread.sleep(30000);
            String topic = "$SYS/broker/stocker/messageCount";
            String broker = "tcp://itsp.htl-leoben.at:1883";
            MemoryPersistence persistence = new MemoryPersistence();
            try {
                MqttClient mqttClient = new MqttClient(broker, "publish_client", persistence);
                MqttConnectOptions connOpts = new MqttConnectOptions();
                connOpts.setCleanSession(true);
                mqttClient.connect(connOpts);
                mqttClient.publish(topic, translateMessageCount());
            } catch (MqttException e) {
                throw new RuntimeException(e);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void start() {
        System.out.println("Starting " + threadName);
        if (t == null) {
            t = new Thread(this, threadName);
            t.start();
        }
    }

    private static MqttMessage translateMessageCount() {
        byte[] payload = String.format("T:%04.2f", MqttSubscriber.messageCount)
                .getBytes();
        return new MqttMessage(payload);
    }
}
