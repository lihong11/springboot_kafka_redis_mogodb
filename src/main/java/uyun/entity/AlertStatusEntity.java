package uyun.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AlertStatusEntity implements Serializable {
    private String id; //告警id
    private String name; //告警名称
    private String status; //告警状态  (“1”:未处理，“2”:处理中，“3“:处理完成)
    private String responsePerson; //状态变更的处理人
    private Date responseTime; //状态变更的处理时间
    private String responseDescription; //状态变更的处理信息
}
