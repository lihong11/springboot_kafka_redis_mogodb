package uyun.sync.datasource;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.state.KeyValueStore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by lihonghua on 2019/2/13.
 */
public class KafkaStreamUtil {

    public KafkaStreamUtil(){
        Map<String, Object> props = new HashMap<>();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "my-stream-processing-application");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.0.188:9092,192.168.0.188:9093,192.168.0.188:9094");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

//        StreamsBuilder builder = new StreamsBuilder();
//        KStream<String, String> textLines = builder.stream("TextLinesTopic");
//        KTable<String, Long> wordCounts = textLines
//                .flatMapValues(textLine -> Arrays.asList(textLine.toLowerCase().split("\\W+")))
//                .groupBy((key, word) -> word)
//                .count(Materialized.<String, Long, KeyValueStore<Bytes, byte[]>>as("counts-store"));
//        wordCounts.toStream().to("WordsWithCountsTopic", Produced.with(Serdes.String(), Serdes.Long()));


        // 创建一个StreamsConfig对象
        StreamsConfig config = new StreamsConfig(props);
        // KStreamBuilder builder = new KStreamBuilder();
        // 创建一个TopologyBuilder对象
        Topology topology = new Topology();
        // 添加一个SOURCE，接收两个参数，param1 定义一个名称，param2 从哪一个topic读取消息
        topology.addSource("SOURCE", "my-replicated-topic")
                // 添加第一个PROCESSOR，param1 定义一个processor名称，param2 processor实现类，param3 指定一个父名称
          //      .addProcessor("PROCESS1", MyProcessorA::new, "SOURCE")
                .addProcessor("PROCESS2", WordCountProcessor::new, "SOURCE")
                // 添加第二个PROCESSOR，param1 定义一个processor名称， param2 processor实现类，param3 指定一个父名称
//                .addProcessor("PROCESS2", MyProcessorB::new, "PROCESS1")
                // 添加第三个PROCESSOR，param1 定义一个processor名称， param2 processor实现类，param3 指定一个父名称
//                .addProcessor("PROCESS3", MyProcessorC::new, "PROCESS2")

                // 最后添加SINK位置，param1 定义一个sink名称，param2 指定一个输出TOPIC，param3 指定接收哪一个PROCESSOR的数据
         ///       .addSink("SINK1", "topicA", "PROCESS1")
                .addSink("SINK2", "topicA", "PROCESS2");
//                .addSink("SINK2", "topicB", "PROCESS2")
//                .addSink("SINK3", "topicC", "PROCESS3");

        // 创建一个KafkaStreams对象，传入TopologyBuilder和StreamsConfig
        KafkaStreams kafkaStreams = new KafkaStreams(topology, config);
        // 启动kafkaStreams
        kafkaStreams.start();


//        Properties properties = new Properties();
//        properties.putAll(props);
//        Topology topology = new Topology();
//        topology.addGlobalStore()
//        KafkaStreams streams = new KafkaStreams(, properties);
//        streams.start();
//        StreamsBuilder builder = new StreamsBuilder();
//        builder.<String, String>stream("my-input-topic").mapValues(value -> value.length().toString()).to("my-output-topic");
//
//        KafkaStreams streams = new KafkaStreams(builder.build(), props);
//        streams.start();


    }

    public static  void main(String args[]){
        KafkaStreamUtil kafkaStreamUtil = new KafkaStreamUtil();
    }
}
