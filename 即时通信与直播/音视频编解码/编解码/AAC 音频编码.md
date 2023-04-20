# AAC 音频编码

AAC 是高级音频编码（Advanced Audio Coding）的缩写，出现于 1997 年，最初是基于 MPEG-2 的音频编码技术，目的是取代 MP3 格式。2000 年，MPEG-4 标准出台，AAC 重新集成了其它技术（PS,SBR），为区别于传统的 MPEG-2 AAC，故含有 SBR 或 PS 特性的 AAC 又称为 MPEG-4 AAC。

AAC 是新一代的音频有损压缩技术，它通过一些附加的编码技术（比如 PS,SBR 等），衍生出了 LC-AAC,HE-AAC,HE-AACv2 三种主要的编码。其中 LC-AAC 就是比较传统的 AAC，相对而言，主要用于中高码率(>=80Kbps)，HE-AAC(相当于 AAC+SBR)主要用于中低码(<=80Kbps)，而新近推出的 HE-AACv2(相当于 AAC+SBR+PS)主要用于低码率(<=48Kbps）。事实上大部分编码器设成<=48Kbps 自动启用 PS 技术，而>48Kbps 就不加 PS，就相当于普通的 HE-AAC。

# AAC 编码规格简述

AAC 共有 9 种规格，以适应不同的场合的需要：

- MPEG-2 AAC LC 低复杂度规格（Low Complexity）注：比较简单，没有增益控制，但提高了编码效率，在中等码率的编码效率以及音质方面，都能找到平衡点
- MPEG-2 AAC Main 主规格
- MPEG-2 AAC SSR 可变采样率规格（Scaleable Sample Rate）
- MPEG-4 AAC LC 低复杂度规格（Low Complexity）---现在的手机比较常见的 MP4 文件中的音频部份就包括了该规格音频文件
- MPEG-4 AAC Main 主规格 注：包含了除增益控制之外的全部功能，其音质最好
- MPEG-4 AAC SSR 可变采样率规格（Scaleable Sample Rate）
- MPEG-4 AAC LTP 长时期预测规格（Long Term Predicition）
- MPEG-4 AAC LD 低延迟规格（Low Delay）
- MPEG-4 AAC HE 高效率规格（High Efficiency）---这种规格适合用于低码率编码，有 Nero ACC 编码器支持

目前使用最多的是 LC 和 HE(适合低码率)。流行的 Nero AAC 编码程序只支持 LC，HE，HEv2 这三种规格，编码后的 AAC 音频，规格显示都是 LC。HE 其实就是 AAC（LC）+SBR 技术，HEv2 就是 AAC（LC）+SBR+PS 技术；

- HE：“High Efficiency”（高效性）。HE-AAC v1（又称 AACPlusV1，SBR)，用容器的方法实现了 AAC（LC）+SBR 技术。SBR 其实代表的是 Spectral Band Replication(频段复制)。简要叙述一下，音乐的主要频谱集中在低频段，高频段幅度很小，但很重要，决定了音质。如果对整个频段编码，若是为了保护高频就会造成低频段编码过细以致文件巨大；若是保存了低频的主要成分而失去高频成分就会丧失音质。SBR 把频谱切割开来，低频单独编码保存主要成分，高频单独放大编码保存音质，“统筹兼顾”了，在减少文件大小的情况下还保存了音质，完美的化解这一矛盾。

- HEv2：用容器的方法包含了 HE-AAC v1 和 PS 技术。PS 指“parametric stereo”（参数立体声）。原来的立体声文件文件大小是一个声道的两倍。但是两个声道的声音存在某种相似性，根据香农信息熵编码定理，相关性应该被去掉才能减小文件大小。所以 PS 技术存储了一个声道的全部信息，然后，花很少的字节用参数描述另一个声道和它不同的地方。

# Links

- https://www.cnblogs.com/renhui/p/10412630.html
