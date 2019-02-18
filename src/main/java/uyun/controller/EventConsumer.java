package uyun.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import uyun.entity.AlertStatusEntity;
import uyun.entity.ResponseEntity;

import java.util.*;

@Slf4j
@Component
public class EventConsumer extends Thread {

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Autowired
    private Jedis jedis;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Value("${alert.key}")
    private String key;

    @Value("${alert.kafka.topic}")
    private String topic;
    public void run() {

        while (true) {
            try{

                Thread.sleep(1);
                String data=jedis.rpop(key);//key值为incidentChange
                // 消费完了休息下
                if (null == data){
                    Thread.sleep(5000);
                    continue;
                }
                sendMessage(data);

            }catch (Throwable e){
                log.warn("the acquire event thread error.",e);
                if (log.isDebugEnabled()){
                    log.debug("stack：", e);
                }
            }
        }
    }



    /**
     * 解析json数据
     *
     * @param data
     * @return
     */
    private void sendMessage(String data) throws Exception{
        JSONObject jsonObj = JSON.parseObject(data);
        // 故障id
        String id = jsonObj.getString("id");
        // 故障名称
        String name = jsonObj.getString("name");
        // 最后一次发生故障时的严重性等级
        // 恢复-0, 提醒-1, 警告-2, 紧急-3
        int severity = jsonObj.getIntValue("severity");
        // 新告警-0, 处理中-150, 已解决-190, 已关闭-255
        int status = jsonObj.getIntValue("status");
        // 故障描述
        String description = jsonObj.getString("description");

        String operatorName = "";
        Date operateTime = new Date();
        AlertStatusEntity alertStatusEntity = null;


        if(0!=status){
            Query query = new Query(Criteria.where("incidentId").is(id)).with(new Sort(Sort.Direction.DESC,"operateTime")).limit(1);
            List<Map> list = mongoTemplate.find(query, Map.class,"IncidentLog");
            for(Map value:list){
                operatorName = Optional.ofNullable(value.get("operatorName")).orElse("").toString();
                Calendar calendar = Calendar.getInstance();
                Date date = (Date)value.get("operateTime");
                calendar.setTime(date);
                operateTime = calendar.getTime();
            }
            alertStatusEntity =  AlertStatusEntity.builder()
                     .id(id)
                     .name(name)
                     .status(String.valueOf(status == 0 ? 1 : (status == 150 ? 2 : (status == 190 ? 3 : (status == 255 ? 3 : -1)))))
                     .responsePerson(operatorName)
                     .responseTime(operateTime)
                     .responseDescription(description)
                     .build();
            ResponseEntity responseAlertStatusEntity = ResponseEntity.builder()
                    .msgCode("ALARM_STATUS")
                    .msgType("MODIFY")
                    .msgData(JSONObject.toJSONString(alertStatusEntity))
                    .build();
            log.info("发送告警信息:"+ JSONObject.toJSONString(responseAlertStatusEntity));
            kafkaTemplate.send("TOPIC_UYUN_ALARM",  JSONObject.toJSONString(responseAlertStatusEntity));
        }
      }
    }