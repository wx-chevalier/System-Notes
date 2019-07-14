# WebRTC 实战

There is one significant difference: Websockets works via TCP, WebRTC works via UDP. In fact, WebRTC is SRTP protocol with some addutional features like STUN, ICE, DTLS etc and internal VoIP features such as Adaptive Jitter Buffer, AEC, AGC etc.

So, Websocket is designed for reliable communication. It is good choise if you want to send any data that must be sent reliably.

When you use WebRTC, the transmitted stream is unreliable. Some packets can be lost in the network. It is bad if you send critical data, for example for financial processing, the same issuee is ideally suitable when you send audio or video stream where some frames can be lost without any noticeable quality issues.

If you want to send data channel via WebRTC, you should have some forward error correction algorithm to restore data if a data frame was lost in the network.
