package uyun.sync.datasource;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Arrays;
import java.util.Properties;

/**
 * Created by lihonghua on 2019/1/31.
 */
public class KafkaConsumerUtil {

     public KafkaConsumerUtil() {
         Properties props = new Properties();
        // props.put("bootstrap.servers", "192.168.0.188:9092,192.168.0.188:9093,192.168.0.188:9094");
         props.put("bootstrap.servers", "192.168.0.61:9093");
         props.put("group.id", "ompGroup");
         props.put("enable.auto.commit", "true");
         props.put("auto.commit.interval.ms", "1000");
         props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
         props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
         KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
         consumer.subscribe(Arrays.asList("TOPIC_UYUN_ALARM"));
         while (true) {
             ConsumerRecords<String, String> records = consumer.poll(100);
             for (ConsumerRecord<String, String> record : records)
                 System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
         }
     }

     public static void main(String args[]){
         KafkaConsumerUtil kafkaConsumerUtil = new KafkaConsumerUtil();
     }
}
