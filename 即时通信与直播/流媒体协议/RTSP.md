# RTSP

RTSP（Real Time Streaming Protocol）是由 Real Network 和 Netscape 共同提出的如何有效地在 IP 网络上传输流媒体数据的应用层协议。RTSP 对流媒体提供了诸如暂停，快进等控制，而它本身并不传输数据，RTSP 的作用相当于流媒体服务器的远程控制。服务器端可以自行选择使用 TCP 或 UDP 来传送串流内容，它的语法和运作跟 HTTP 1.1 类似，但并不特别强调时间同步，所以比较能容忍网络延迟。而且允许同时多个串流需求控制（Multicast），除了可以降低服务器端的网络用量，还可以支持多方视频会议（Video onference）因为与 HTTP1.1 的运作方式相似，所以代理服务器的快取功能 Cache 也同样适用于 RTSP，并因 RTSP 具有重新导向功能，可视实际负载情况来转换提供服务的服务器，以避免过大的负载集中于同一服务器而造成延迟。

## RTSP 和 HTTP 的区别和联系

联系在于，两者都用纯文本来发送消息，且 rtsp 协议的语法也和 HTTP 类似。Rtsp 一开始这样设计，也是为了能够兼容使用以前写的 HTTP 协议分析代码。

区别在于，rtsp 是有状态的，不同的是 RTSP 的命令需要知道现在正处于一个什么状态，也就是说 rtsp 的命令总是按照顺序来发送，某个命令总在另外一个命令之前要发送。Rtsp 不管处于什么状态都不会去断掉连接。，而 http 则不保存状态，协议在发送一个命令以后，连接就会断开，且命令之间是没有依赖性的。rtsp 协议使用 554 端口，http 使用 80 端口。

## RTSP 和 SIP 的区别和联系

SIP（Session Initiation Protocol），是基于 IP 的一个应用层控制协议。由于 SIP 是基于纯文本的信令协议，可以管理不同接入网络上的会话等。会话可以是终端设备之间任何类型的通信，如视频会话、既时信息处理或协作会话。该协议不会定义或限制可使用的业务，传输、服务质量、计费、安全性等问题都由基本核心网络和其它协议处理。

- 联系：sip 和 rtsp 都是应用层的控制协议，负责一次通信过程的建立和控制和结束，不负责中间的传输部分。他们都是基于纯文本的信令协议，穿墙性能良好。支持 tcp、udp，支持多方通信。他们都需要服务器支持，都支持会话中重定向。sip 和 rtsp 都使用 sdp 协议来传送媒体参数，使用 rtp（rtcp）协议来传输媒体流。

- 区别：rtsp 是专门为流媒体制定的协议，在多个媒体流的时间同步方面比 sip 强大。rtsp 还提供网络负载均衡的功能，减轻服务器压力和网络带宽要求。sip 一般用来创建一次音频、视频通话（双向），而 rtsp 一般用来做视频点播、视频监控等（单向）。当然，从原理上讲，rtsp 也可以做双向的视频通话。

## RTSP 和 RTP（RTCP）的关系

![RTSP 网络层次图](https://s2.ax1x.com/2020/02/10/151agH.png)

rtsp 负责建立和控制会话，rtp 负责多媒体的传输，rtcp 配合 rtp 做控制和流量统计，他们是合作的关系。

# 消息

RTSP 的消息有两大类，一是请求消息(request)，一是回应消息(response)，两种消息的格式不同。

请求消息格式：

```s
方法 URI RTSP 版本 CR LF
消息头 CR LF CR LF
消息体 CR LF
```

其中方法包括 OPTIONS、SETUP、PLAY、TEARDOWN 等待，URI 是接收方（服务端）的地址，例如：rtsp://192.168.22.136:5000/v0，每行后面的 CR LF 表示回车换行，需要接收端有相应的解析，最后一个消息头需要有两个 CR LF。

回应消息格式：

```s
RTSP 版本 状态码 解释 CR LF
消息头 CR LF CR LF
消息体 CR LF
```

其中 RTSP 版本一般都是 RTSP/1.0，状态码是一个数值，200 表示成功，解释是与状态码对应的文本解释。状态码由三位数组成，表示方法执行的结果，定义如下：

- 1XX：保留，将来使用；
- 2XX：成功，操作被接收、理解、接受（received,understand,accepted）；
- 3XX：重定向，要完成操作必须进行进一步操作；
- 4XX：客户端出错，请求有语法错误或无法实现；
- 5XX：服务器出错，服务器无法实现合法的请求。

# 方法

rtsp 中定义的方法有：OPTIONS, DESCRIBE, SETUP, TEARDOWN, PLAY, PAUSE, SCALE, GET_PARAMETER，SET_PARAMETER。

## OPTION

目的是得到服务器提供的可用方法:

```sh
OPTIONS rtsp://192.168.20.136:5000/xxx666 RTSP/1.0
CSeq: 1 //每个消息都有序号来标记，第一个包通常是 option 请求消息
User-Agent: VLC media player (LIVE555 Streaming Media v2005.11.10)

# 服务器的回应信息包括提供的一些方法,例如:

RTSP/1.0 200 OK
Server: UServer 0.9.7_rc1
Cseq: 1 //每个回应消息的 cseq 数值和请求消息的 cseq 相对应
Public: OPTIONS, DESCRIBE, SETUP, TEARDOWN, PLAY, PAUSE, SCALE, GET_PARAMETER //服务器提供的可用的方法
```

## DESCRIBE

C 向 S 发起 DESCRIBE 请求,为了得到会话描述信息(SDP):

```sh
DESCRIBE rtsp://192.168.20.136:5000/xxx666 RTSP/1.0
CSeq: 2
token:
Accept: application/sdp
User-Agent: VLC media player (LIVE555 Streaming Media v2005.11.10)

# 服务器回应一些对此会话的描述信息(sdp):
RTSP/1.0 200 OK
Server: UServer 0.9.7_rc1
Cseq: 2
x-prev-url: rtsp://192.168.20.136:5000
x-next-url: rtsp://192.168.20.136:5000
x-Accept-Retransmit: our-retransmit
x-Accept-Dynamic-Rate: 1
Cache-Control: must-revalidate
Last-Modified: Fri, 10 Nov 2006 12:34:38 GMT
Date: Fri, 10 Nov 2006 12:34:38 GMT
Expires: Fri, 10 Nov 2006 12:34:38 GMT
Content-Base: rtsp://192.168.20.136:5000/xxx666/
Content-Length: 344
Content-Type: application/sdp
v=0 //以下都是 sdp 信息
o=OnewaveUServerNG 1451516402 1025358037 IN IP4 192.168.20.136
s=/xxx666
u=http:///
e=admin@
c=IN IP4 0.0.0.0
t=0 0
a=isma-compliance:1,1.0,1
a=range:npt=0-
m=video 0 RTP/AVP 96 //m 表示媒体描述，下面是对会话中视频通道的媒体描述
a=rtpmap:96 MP4V-ES/90000
a=fmtp:96 profile-level-id=245;config=000001B0F5000001B509000001000000012000C888B0E0E0FA62D089028307
a=control:trackID=0//trackID ＝ 0 表示视频流用的是通道 0
```

## SETUP

客户端提醒服务器建立会话,并确定传输模式:

```sh
SETUP rtsp://192.168.20.136:5000/xxx666/trackID=0 RTSP/1.0
CSeq: 3
Transport: RTP/AVP/TCP;unicast;interleaved=0-1
User-Agent: VLC media player (LIVE555 Streaming Media v2005.11.10)
//uri 中带有 trackID ＝ 0，表示对该通道进行设置。Transport 参数设置了传输模式，包的结构。接下来的数据包头部第二个字节位置就是 interleaved，它的值是每个通道都不同的，trackID ＝ 0 的 interleaved 值有两个 0或1，0 表示 rtp 包，1 表示 rtcp 包，接受端根据 interleaved 的值来区别是哪种数据包。

# 服务器回应信息:
RTSP/1.0 200 OK
Server: UServer 0.9.7_rc1
Cseq: 3
Session: 6310936469860791894 //服务器回应的会话标识符
Cache-Control: no-cache
Transport: RTP/AVP/TCP;unicast;interleaved=0-1;ssrc=6B8B4567
```

## PLAY

客户端发送播放请求:

```s
PLAY rtsp://192.168.20.136:5000/xxx666 RTSP/1.0
CSeq: 4
Session: 6310936469860791894
Range: npt=0.000- //设置播放时间的范围
User-Agent: VLC media player (LIVE555 Streaming Media v2005.11.10)

# 服务器回应信息:
RTSP/1.0 200 OK
Server: UServer 0.9.7_rc1
Cseq: 4
Session: 6310936469860791894
Range: npt=0.000000-
RTP-Info: url=trackID=0;seq=17040;rtptime=1467265309
//seq 和 rtptime 都是 rtp 包中的信息
```

## TEARDOWN

客户端发起关闭请求:

```sh
TEARDOWN rtsp://192.168.20.136:5000/xxx666 RTSP/1.0
CSeq: 5
Session: 6310936469860791894
User-Agent: VLC media player (LIVE555 Streaming Media v2005.11.10)

# 服务器回应:
RTSP/1.0 200 OK
Server: UServer 0.9.7_rc1
Cseq: 5
Session: 6310936469860791894
Connection: Close
```

---

以上方法都是交互过程中最为常用的,其它还有一些重要的方法如 get/set_parameter,pause,redirect 等等
