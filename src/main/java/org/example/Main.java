package org.example;

import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.Timer;
import java.util.concurrent.Flow;

public class Main {
    public static void main(String[] args) throws MqttException {
        MqttSubscriber sub = new MqttSubscriber();
        sub.start();
        MqttPublisher pub = new MqttPublisher();
        pub.start();
    }
}
