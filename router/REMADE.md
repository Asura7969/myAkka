#### Actor路由
```
                                  Routees

                                  |----> ActorRefRoutee ---> ActorRef
Router -----> RoutingLogic -----> |----> ActorRefRoutee ---> ActorRef
                                  |----> ActorRefRoutee ---> ActorRef

```
#### RoutingLogic
* **RoundRobin**(轮询)
* **Broadcast**(广播)
* **Balancing**:会将繁忙的**Routee**中的任务重新分配给比较空闲的**Routee**,所有**Routee**会共享一个邮箱,不支持group
* **ScatterGatherFirstCompleted**:会把消息发送给所有**Routee**,并期待一个最快的回复(此时其他回复会被丢弃),
  假设在设定的时间内不能得到一个回复,则会产生**AskTimeoutException**
...


#### pool
> 创建后自带父子级关系,下游routee（Actor）出现 down 或异常,若没配置策略, 会把该异常传递给上游父级
> 父级会监督子级


#### group
> 通过路径发送给下游routee（Actor）消息
> 通过配置文件指定子级路径集创建关系


#### 特殊消息
##### Broadcast消息
```java
router.tell(new Broadcast("broadcast msg"), ActorRef.noSender);
```
##### PoisonPill消息
```java
router.tell(new Broadcast(PoisonPill.getInstance()), ActorRef.noSender);


router.tell(new Broadcast(Kill.getInstance()), ActorRef.noSender);
```
