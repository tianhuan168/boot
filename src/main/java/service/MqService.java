package service;


/**
 * 负责账单中心所有mq消息的发送服务
 *
 * @author tanxianwen 2017年11月27日
 */
public interface MqService {

    /**
     * 发送一条mq
     * 
     * @param exchange
     * @param routingKey 以业务分类标识，分级的时候以“.”分隔
     * @param object 要发送的数据对象，发送时会被转换成json
     * @param correlationId 关联id，如果发送失败，则失败回调中会原样返回correlationId，但消息体不一定返回
     */
    void send(String exchange, String routingKey, Object object, String correlationId);

}
