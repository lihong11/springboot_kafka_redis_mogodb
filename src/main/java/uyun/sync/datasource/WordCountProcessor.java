package uyun.sync.datasource;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.processor.*;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.internals.InMemoryKeyValueStore;
import org.springframework.kafka.support.serializer.JsonSerde;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Created by lihonghua on 2019/2/14.
 */
public class WordCountProcessor implements Processor<String, String> {

    private ProcessorContext context;
    private KeyValueStore<String, Integer> keyValueStore;
    @Override
    public void init(ProcessorContext processorContext) {

        this.context = processorContext;
       // this.keyValueStore = (KeyValueStore<String, Integer>)processorContext.getStateStore("Counts");
        this.keyValueStore = new InMemoryKeyValueStore<>("Counts", new JsonSerde<>(),new JsonSerde<>());
        this.context.schedule(1000, PunctuationType.STREAM_TIME, new Punctuator() {
            @Override
            public void punctuate(long l) {
                KeyValueIterator<String, Integer>  keyValueIterator = WordCountProcessor.this.keyValueStore.all();
                keyValueIterator.forEachRemaining(entry ->{
                    context.forward(entry.key,entry.value);
                    WordCountProcessor.this.keyValueStore.delete(entry.key);
                });
                context.commit();
            }
        });

    }

    @Override
    public void process(String s, String s2) {

        Stream.of(s2.toLowerCase().split(" ")).forEach((String word) ->{
            Optional<Integer> counts = Optional.ofNullable(keyValueStore.get(word));
            int count = counts.map(wordcount -> wordcount+1).orElse(1);
            keyValueStore.put(word,count);
        });
    }

    @Override
    public void close() {
        keyValueStore.close();
    }
}
