# LVS

LVS 有如下的组成部分：

- Direct Server（以下简称 DS）：前端暴露给客户端进行负载均衡的服务器。

- Virtual Ip 地址（以下简称 VIP）：DS 暴露出去的 IP 地址，做为客户端请求的地址。

- Direct Ip 地址（以下简称 DIP）：DS 用于与 Real Server 交互的 IP 地址。

- Real Server（以下简称 RS）：后端真正进行工作的服务器，可以横向扩展。

- Real IP 地址（以下简称 RIP）：RS 的地址。

- Client IP 地址（以下简称 CIP）：Client 的地址。

![](https://tva3.sinaimg.cn/large/007DFXDhgy1g5us7zazn4j30ly0f03zb.jpg)

客户端进行请求时，流程如下：

- 使用 VIP 地址访问 DS，此时的地址二元组为<src:CIP,dst:VIP>。

- DS 根据自己的负载均衡算法，选择一个 RS 将请求转发过去，在转发过去的时候，修改请求的源 IP 地址为 DIP 地址，让 RS 看上去认为是 DS 在访问它，此时的地址二元组为<src:DIP,dst:RIP A>。

- RS 处理并且应答该请求，这个回报的源地址为 RS 的 RIP 地址，目的地址为 DIP 地址，此时的地址二元组为<src:RIP A,dst:DIP>。

- DS 在收到该应答包之后，将报文应答客户端，此时修改应答报文的源地址为 VIP 地址，目的地址为 CIP 地址，此时的地址二元组为<src:VIP,dst:CIP>。
