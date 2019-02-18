package uyun.sync.datasource;

import org.apache.kafka.clients.producer.*;

import java.util.Properties;

/**
 * Created by lihonghua on 2019/1/31.
 * kafka集群代码
 */
public class KafkaProducerUtil {

    public KafkaProducerUtil(){
//        Properties props = new Properties();
//        props.put("bootstrap.servers", "192.168.253.148:9092");
//        props.put("acks", "all");
////        props.put("delivery.timeout.ms", 30000);
//        props.put("batch.size", 16384);
////        props.put("linger.ms", 1);
//        props.put("buffer.memory", 33554432);
//        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
//        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
//
//        Producer<String, String> producer = new KafkaProducer<>(props);
//        for (int i = 0; i < 100; i++){
//            producer.send(new ProducerRecord<String, String>("TOPIC_UYUN_ALARM", Integer.toString(i), Integer.toString(i)));
//        }
//
//
//
//        producer.close();

        Properties props = new Properties();
        props.put("bootstrap.servers", "192.168.0.61:9093");
//        props.put("bootstrap.servers", "192.168.0.188:9092,192.168.0.188:9093,192.168.0.188:9094");
        props.put("acks", "all");
        props.put("retries",0);
        props.put("delivery.timeout.ms", 3000000);
        props.put("request.timeout.ms", 2000);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        try {
            Producer<String, String> producer = new KafkaProducer<>(props);
            for (int i = 0; i < 100; i++){
                producer.send(new ProducerRecord<String, String>("TOPIC_UYUN_ALARM", Integer.toString(i), "hello world 测试你好你好kafkaStream！"+Integer.toString(i)),new Callback(){

                    @Override
                    public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                         System.out.println(recordMetadata.topic());
                         System.out.println(e.fillInStackTrace());
                    }
                });
//                Thread.sleep(1000);
            }
            producer.close();
        }catch (Exception e){
                e.printStackTrace();
        }

        System.out.print("send success");



    }

    public static void main(String  args[]){
        KafkaProducerUtil kafkaProducerUtil = new KafkaProducerUtil();
    }

}
