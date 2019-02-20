package uyun.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseEntity implements Serializable {
    private String msgCode; //消息码，用于识别不同的消息     固定值：ALARM
    private String msgType; //消息类型，用于识别消息的动作   ADD：添加，MODIFY：修改
    private List msgData; //消息数据，json字符串           告警信息 列表 的json字符串。
}
