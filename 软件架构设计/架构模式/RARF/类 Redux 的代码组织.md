在笔者之前关于[RARF](https://segmentfault.com/a/1190000004600730)的描述中，曾提及基于 MVC 风格的业务模块代码架构中存在的一些问题。彼时笔者推崇的是基于 URFP 的链式逻辑组织，换言之，一个完整的业务逻辑有数个 ResourceHandler 链接完成。但是在实践中这种方式并不是适用于全部的情况，很多时候，从一个正常的思维角度来说，我们还是习惯于去写**面条式**的代码，即在一个 Controller 中通过调用多个 Service 进行一条业务逻辑线的处理，本文笔者即是在编写这种面条式的逻辑代码的前提下，自己思索的一些实践。

> 笔者并没有评估过本文这种实现所引起的性能损耗，大概是因为 JVM 调试分析的能力太渣了吧。另一方面，本文很有可能矫枉过正，所以权当一乐。

# Overview

为了能有一个大概的印象，我们以一个简单的登录注册逻辑作为示范，整个流程中只有两条主要的逻辑线，大概如下图所示：
![](http://7xkt0f.com1.z0.glb.clouddn.com/B5EE471B-D893-4B88-8B5A-53C46588A801.png)
然后我们直接看最终的 Controller 的写法：

```@RequestMapping("/login/{verifyCode}")
String login(HttpServletRequest request, @PathVariable("verifyCode") String verifyCode) {

    //初始化上下文
    SyncContext syncContext = new SyncContext();

    syncContext

            //判断用户提交的信息是否有效
            .requestHandler((uniResourceBag, action) -> {


                JSONObject requestData = UniResourceBag.parseRequestData(request, "username", "password");

                return new Action("RequestDataReady")
                        .addActionData("username", requestData.getString("username"))
                        .addActionData("password", requestData.getString("password"));

            })

            .step("请求处理")

            .reducer("判断用户是否存在", (uniResourceBag, action) -> {

                //判断接收到的类型是否为当前需要处理的类型,如果不是则忽略
                if (!action.isType("RequestDataReady")) {
                    return new Action();
                }

                String username = (String) action.getActionDataOrDefault("username");

                String password = (String) action.getActionDataOrDefault("password");

                //如果用户名为chevalier,则认为是存在的
                if (username.equals("chevalier")) {

                    return new Action("doLogin").addActionData("username", username).addActionData("password", password);

                } else if (username.equals("error")) {

                    throw new NullPointerException("error");

                } else {
                    //否则创建新用户
                    return new Action("doRegister").addActionData("username", username).addActionData("password", password);
                }

            })

            .step("执行登录操作或者创建新用户")

            .reducer("对于已存在用户进行登录操作,返回user_token", (uniResourceBag, action) -> {

                if (!action.isType("doLogin")) {
                    return new Action();
                }

                String username = (String) action.getActionDataOrDefault("username");

                String password = (String) action.getActionDataOrDefault("password");

                //将username+password连接作为user_token登录
                uniResourceBag.setRequestDataWhenSuccess("user_token", UUID.randomUUID());

                return new Action("Complete");
            })

            .reducer("对于新用户判断用户名是否存在,返回创建好的信息与user_token", (uniResourceBag, action) -> {

                if (!action.isType("doRegister")) {
                    return new Action();
                }

                //返回用户名,用户密码,创建时间和用户Token
                String username = (String) action.getActionDataOrDefault("username");

                String password = (String) action.getActionDataOrDefault("password");

                //将username+password连接作为user_token登录
                uniResourceBag.setRequestDataWhenSuccess(
                        "username", username,
                        "password", password,
                        "create_time", Instant.now().toEpochMilli(),
                        "user_token", UUID.randomUUID());

                return new Action("Complete");
            })

            .responseHandler((uniResourceBag, action) -> {

                //进行最后的处理

                return new Action();
            });


    return syncContext
            .getUniResourceBag()
            .getResponseString();
}
```

上面就是完成该流程的整个的代码，可以看出整个流程由 step 划分为了数个层次，这也是借鉴了软件设计中的分层模型，一般来说，每次请求只会针对一条逻辑线，即是每一个 Step 中只有一个 Reducer 会被调用。同一个 Step 中的不同的 Reducer 往往之间是不同的条件选择，而不同 Step 之间只会用 Action 进行沟通。但是整个流程还是以面条式的代码实现，方便理解。我们再看下整个的请求与响应效果：
(1)正常登录请求：login/1?requestData={"username":"chevalier","password":123}

```

      {    "code": 0,    "subCode": 0,    "runtimeLog":    {        "Context Uptime": 2,        "steps":        [            {                "actualReducerDesc": "RequestHandler",                "stepDesc": "RequestHandler",                "stepId": 0,                "actualReducerAction":                {                    "actionType": "RequestDataReady",                    "actionData":                    {                        "password": "123",                        "username": "chevalier"                    }                },                "stepRunTime": 0,                "actualReducerUUID": "2608550a-6550-40da-84aa-a6a5b68bb93d"            },            {                "actualReducerDesc": "判断用户是否存在",                "stepDesc": "请求处理",                "stepId": 1,                "actualReducerAction":                {                    "actionType": "doLogin",                    "actionData":                    {                        "password": "123",                        "username": "chevalier"                    }                },                "stepRunTime": 0,                "actualReducerUUID": "92277976-bc24-4f69-b286-a4c95be993ac"            },            {                "actualReducerDesc": "对于已存在用户进行登录操作,返回user_token",                "stepDesc": "执行登录操作或者创建新用户",                "stepId": 2,                "actualReducerAction":                {                    "actionType": "Complete",                    "actionData":                    {                    }                },                "stepRunTime": 0,                "actualReducerUUID": "f21b1a80-9b86-49d7-9751-5bdc6d90c355"            }        ]    },    "user_token": "499206a2-0130-484b-82e0-2b822c3b8dfa",    "desc": "Success"
}
```

(2)正常注册请求：login/1?requestData={"username":"123","password":123}

```

      {    "password": "123",    "code": 0,    "create_time": 1461944838909,    "subCode": 0,    "runtimeLog":    {        "Context Uptime": 0,        "steps":        [            {                "actualReducerDesc": "RequestHandler",                "stepDesc": "RequestHandler",                "stepId": 0,                "actualReducerAction":                {                    "actionType": "RequestDataReady",                    "actionData":                    {                        "password": "123",                        "username": "123"                    }                },                "stepRunTime": 0,                "actualReducerUUID": "c3e3040d-6ff4-41a7-b911-dbc31de6c6dd"            },            {                "actualReducerDesc": "判断用户是否存在",                "stepDesc": "请求处理",                "stepId": 1,                "actualReducerAction":                {                    "actionType": "doRegister",                    "actionData":                    {                        "password": "123",                        "username": "123"                    }                },                "stepRunTime": 0,                "actualReducerUUID": "a5ea22dd-41c4-46bc-9d14-f60c8f6a737d"            },            {                "actualReducerDesc": "对于新用户判断用户名是否存在,返回创建好的信息与user_token",                "stepDesc": "执行登录操作或者创建新用户",                "stepId": 2,                "actualReducerAction":                {                    "actionType": "Complete",                    "actionData":                    {                    }                },                "stepRunTime": 0,                "actualReducerUUID": "eb06837e-03b3-4812-9c2d-ff86a4d795a0"            }        ]    },    "user_token": "6866cf13-ee06-4333-831a-dde0f2114764",    "username": "123",    "desc": "Success"}


```

(3)我在随机一个地方抛出了一个异常：

```

      {    "code": -1008,    "runtimeLog":    {        "Context Uptime": 5,        "steps":        [            {                "actualReducerDesc": "RequestHandler",                "stepDesc": "RequestHandler",                "stepId": 0,                "actualReducerAction":                {                    "actionType": "RequestDataReady",                    "actionData":                    {                        "password": "123",                        "username": "error"                    }                },                "stepRunTime": 0,                "actualReducerUUID": "50cb7b43-cc09-4491-8650-3294903fd6e6"            },            {                "stepDesc": "请求处理",                "stepId": 1,                "stepRunTime": -1461944917734            },            {                "stepDesc": "执行登录操作或者创建新用户",                "stepId": 2,                "stepRunTime": 0            }        ]    },    "stackTrace": "error
wx.controller.user.LoginController.lambda$login$13(LoginController.java:67)
wx.controller.user.LoginController$$Lambda$215/1765146717.doWork(Unknown Source)
wx.rarf.context.SyncContext.lambda$null$21(SyncContext.java:304)
wx.rarf.context.SyncContext$$Lambda$220/1322867488.call(Unknown Source)
rx.Observable.unsafeSubscribe(Observable.java:7507)
rx.internal.operators.OperatorMerge$MergeSubscriber.handleNewSource(OperatorMerge.java:215)
rx.internal.operators.OperatorMerge$MergeSubscriber.onNext(OperatorMerge.java:185)
rx.internal.operators.OperatorMerge$MergeSubscriber.onNext(OperatorMerge.java:120)
rx.internal.operators.OperatorMap$1.onNext(OperatorMap.java:55)
rx.internal.operators.OperatorSingle$ParentSubscriber.onCompleted(OperatorSingle.java:124)
rx.internal.operators.OperatorTake$1.onNext(OperatorTake.java:69)
rx.internal.operators.OperatorMerge$InnerSubscriber.emit(OperatorMerge.java:676)
rx.internal.operators.OperatorMerge$InnerSubscriber.onNext(OperatorMerge.java:586)
wx.rarf.context.SyncContext.lambda$requestHandler$18(SyncContext.java:93)
wx.rarf.context.SyncContext$$Lambda$214/1827551503.call(Unknown Source)
rx.Observable.unsafeSubscribe(Observable.java:7507)
rx.internal.operators.OperatorMerge$MergeSubscriber.handleNewSource(OperatorMerge.java:215)
rx.internal.operators.OperatorMerge$MergeSubscriber.onNext(OperatorMerge.java:185)
rx.internal.operators.OperatorMerge$MergeSubscriber.onNext(OperatorMerge.java:120)
rx.internal.operators.OnSubscribeFromIterable$IterableProducer.request(OnSubscribeFromIterable.java:98)
rx.Subscriber.setProducer(Subscriber.java:177)
rx.internal.operators.OnSubscribeFromIterable.call(OnSubscribeFromIterable.java:50)
rx.internal.operators.OnSubscribeFromIterable.call(OnSubscribeFromIterable.java:33)
rx.Observable$1.call(Observable.java:144)
rx.Observable$1.call(Observable.java:136)
rx.Observable$1.call(Observable.java:144)
rx.Observable$1.call(Observable.java:136)
rx.Observable$1.call(Observable.java:144)
rx.Observable$1.call(Observable.java:136)
rx.Observable$1.call(Observable.java:144)
rx.Observable$1.call(Observable.java:136)
rx.Observable$1.call(Observable.java:144)
rx.Observable$1.call(Observable.java:136)
rx.Observable.unsafeSubscribe(Observable.java:7507)
rx.internal.operators.OperatorMerge$MergeSubscriber.handleNewSource(OperatorMerge.java:215)
rx.internal.operators.OperatorMerge$MergeSubscriber.onNext(OperatorMerge.java:185)
rx.internal.operators.OperatorMerge$MergeSubscriber.onNext(OperatorMerge.java:120)
rx.internal.operators.OnSubscribeFromIterable$IterableProducer.request(OnSubscribeFromIterable.java:98)
rx.Subscriber.setProducer(Subscriber.java:177)
rx.internal.operators.OnSubscribeFromIterable.call(OnSubscribeFromIterable.java:50)
rx.internal.operators.OnSubscribeFromIterable.call(OnSubscribeFromIterable.java:33)
rx.Observable$1.call(Observable.java:144)
rx.Observable$1.call(Observable.java:136)
rx.Observable$1.call(Observable.java:144)
rx.Observable$1.call(Observable.java:136)
rx.Observable$1.call(Observable.java:144)
rx.Observable$1.call(Observable.java:136)
rx.Observable$1.call(Observable.java:144)
rx.Observable$1.call(Observable.java:136)
rx.Observable$1.call(Observable.java:144)
rx.Observable$1.call(Observable.java:136)
rx.Observable.unsafeSubscribe(Observable.java:7507)
rx.internal.operators.OperatorMerge$MergeSubscriber.handleNewSource(OperatorMerge.java:215)
rx.internal.operators.OperatorMerge$MergeSubscriber.onNext(OperatorMerge.java:185)
rx.internal.operators.OperatorMerge$MergeSubscriber.onNext(OperatorMerge.java:120)
rx.internal.operators.OnSubscribeFromIterable$IterableProducer.request(OnSubscribeFromIterable.java:98)
rx.Subscriber.setProducer(Subscriber.java:177)
rx.internal.operators.OnSubscribeFromIterable.call(OnSubscribeFromIterable.java:50)
rx.internal.operators.OnSubscribeFromIterable.call(OnSubscribeFromIterable.java:33)
rx.Observable$1.call(Observable.java:144)
rx.Observable$1.call(Observable.java:136)
rx.Observable$1.call(Observable.java:144)
rx.Observable$1.call(Observable.java:136)
rx.Observable$1.call(Observable.java:144)
rx.Observable$1.call(Observable.java:136)
rx.Observable$1.call(Observable.java:144)
rx.Observable$1.call(Observable.java:136)
rx.Observable$1.call(Observable.java:144)
rx.Observable$1.call(Observable.java:136)
rx.Observable$1.call(Observable.java:144)
rx.Observable$1.call(Observable.java:136)
rx.Observable.subscribe(Observable.java:7597)
rx.observables.BlockingObservable.blockForSingle(BlockingObservable.java:442)
rx.observables.BlockingObservable.first(BlockingObservable.java:169)
wx.rarf.context.SyncContext.responseHandler(SyncContext.java:172)
wx.controller.user.LoginController.login(LoginController.java:115)
sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
java.lang.reflect.Method.invoke(Method.java:497)
org.springframework.web.method.support.InvocableHandlerMethod.doInvoke(InvocableHandlerMethod.java:221)
org.springframework.web.method.support.InvocableHandlerMethod.invokeForRequest(InvocableHandlerMethod.java:136)
org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod.invokeAndHandle(ServletInvocableHandlerMethod.java:110)
org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.invokeHandlerMethod(RequestMappingHandlerAdapter.java:817)
org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.handleInternal(RequestMappingHandlerAdapter.java:731)
org.springframework.web.servlet.mvc.method.AbstractHandlerMethodAdapter.handle(AbstractHandlerMethodAdapter.java:85)
org.springframework.web.servlet.DispatcherServlet.doDispatch(DispatcherServlet.java:959)
org.springframework.web.servlet.DispatcherServlet.doService(DispatcherServlet.java:893)
org.springframework.web.servlet.FrameworkServlet.processRequest(FrameworkServlet.java:968)
org.springframework.web.servlet.FrameworkServlet.doGet(FrameworkServlet.java:859)
javax.servlet.http.HttpServlet.service(HttpServlet.java:687)
org.springframework.web.servlet.FrameworkServlet.service(FrameworkServlet.java:844)
javax.servlet.http.HttpServlet.service(HttpServlet.java:790)
org.eclipse.jetty.servlet.ServletHolder.handle(ServletHolder.java:812)
org.eclipse.jetty.servlet.ServletHandler$CachedChain.doFilter(ServletHandler.java:1669)
org.eclipse.jetty.websocket.server.WebSocketUpgradeFilter.doFilter(WebSocketUpgradeFilter.java:224)
org.eclipse.jetty.servlet.ServletHandler$CachedChain.doFilter(ServletHandler.java:1652)
org.springframework.boot.actuate.autoconfigure.EndpointWebMvcAutoConfiguration$ApplicationContextHeaderFilter.doFilterInternal(EndpointWebMvcAutoConfiguration.java:237)
org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:107)
org.eclipse.jetty.servlet.ServletHandler$CachedChain.doFilter(ServletHandler.java:1652)
wx.application.filter.SystemFilter.doFilter(SystemFilter.java:95)
org.eclipse.jetty.servlet.ServletHandler$CachedChain.doFilter(ServletHandler.java:1652)
org.springframework.boot.actuate.trace.WebRequestTraceFilter.doFilterInternal(WebRequestTraceFilter.java:112)
org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:107)
org.eclipse.jetty.servlet.ServletHandler$CachedChain.doFilter(ServletHandler.java:1652)
org.springframework.web.filter.RequestContextFilter.doFilterInternal(RequestContextFilter.java:99)
org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:107)
org.eclipse.jetty.servlet.ServletHandler$CachedChain.doFilter(ServletHandler.java:1652)
org.springframework.web.filter.HttpPutFormContentFilter.doFilterInternal(HttpPutFormContentFilter.java:87)
org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:107)
org.eclipse.jetty.servlet.ServletHandler$CachedChain.doFilter(ServletHandler.java:1652)
org.springframework.web.filter.HiddenHttpMethodFilter.doFilterInternal(HiddenHttpMethodFilter.java:77)
org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:107)
org.eclipse.jetty.servlet.ServletHandler$CachedChain.doFilter(ServletHandler.java:1652)
org.springframework.web.filter.CharacterEncodingFilter.doFilterInternal(CharacterEncodingFilter.java:121)
org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:107)
org.eclipse.jetty.servlet.ServletHandler$CachedChain.doFilter(ServletHandler.java:1652)
org.springframework.boot.actuate.autoconfigure.MetricsFilter.doFilterInternal(MetricsFilter.java:103)
org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:107)
org.eclipse.jetty.servlet.ServletHandler$CachedChain.doFilter(ServletHandler.java:1652)
org.eclipse.jetty.servlet.ServletHandler.doHandle(ServletHandler.java:585)
org.eclipse.jetty.server.handler.ScopedHandler.handle(ScopedHandler.java:143)
org.eclipse.jetty.security.SecurityHandler.handle(SecurityHandler.java:577)
org.eclipse.jetty.server.session.SessionHandler.doHandle(SessionHandler.java:223)
org.eclipse.jetty.server.handler.ContextHandler.doHandle(ContextHandler.java:1127)
org.eclipse.jetty.servlet.ServletHandler.doScope(ServletHandler.java:515)
org.eclipse.jetty.server.session.SessionHandler.doScope(SessionHandler.java:185)
org.eclipse.jetty.server.handler.ContextHandler.doScope(ContextHandler.java:1061)
org.eclipse.jetty.server.handler.ScopedHandler.handle(ScopedHandler.java:141)
org.eclipse.jetty.server.handler.HandlerWrapper.handle(HandlerWrapper.java:97)
org.eclipse.jetty.server.Server.handle(Server.java:499)
org.eclipse.jetty.server.HttpChannel.handle(HttpChannel.java:311)
org.eclipse.jetty.server.HttpConnection.onFillable(HttpConnection.java:257)
org.eclipse.jetty.io.AbstractConnection$2.run(AbstractConnection.java:544)
org.eclipse.jetty.util.thread.QueuedThreadPool.runJob(QueuedThreadPool.java:635)
org.eclipse.jetty.util.thread.QueuedThreadPool$3.run(QueuedThreadPool.java:555)
java.lang.Thread.run(Thread.java:745)
",    "desc": "内部错误"}
```

# Features & Motivation

首先，我们需要考虑现在的问题是什么，以及改进的目标是什么。笔者最早萌生出要做出一些改进的动因，是看到了下面这一个 Controller，因为代码过多，如果有碍阅读可以直接拉到下面看。一个 Controller 写了 600 多行，这绝不是一件令人骄傲的事情，这种代码的可读性、可维护性基本上为零。而这种代码的产生，正如笔者在[RARF](https://segmentfault.com/a/1190000004600730)中分析的，并不一定是因为程序猿的能力和整体代码规范的问题。下面的这个代码还算是规范的吧(笔者已经删去了很多的注释)，但是因为在一个快速演进迭代地业务系统中，我们不可避免的要以打补丁的方式来修正逻辑。但是打补丁的可能后果之一就是因为在一条业务逻辑线中的分支过多而导致 N 层的内嵌选择。最佳的方式，肯定是在系统规划之初就把这个逻辑线再细分一下，但是 PM 说好的不会改需求呢？

```
    @RequestMapping("doShareCreateWithAt")
    public void doShareCreateWithAt(HttpServletRequest request, PrintWriter out)
            throws Exception {
        HJSONObject rtn = new HJSONObject();

        int code = ErrorConfig.CODE_SUCCESS;
        String desc = "返回成功";
        int subCode = -1;
        HJSONObject json = new HJSONObject();
        String user_id = null;
        String share_id = "10";//默认的分享创建成功之后
        try {
            //鉴权：判断用户Token是否有效
            json = requestHandler(request, new String[]{"user_token", "share_img_path"});
            String user_token = json.getString("user_token");
            user_id = userTokenServiceImpl.queryUserIdByToken(user_token);
            if (user_id == null) {
                code = ErrorConfig.CODE_1006;
                subCode = 1;
                desc = "[" + code + "]+" + "[" + subCode + "],用户鉴权失败";
                errorInfo.setErrorInfo(code, subCode, desc, request, out);
                return;
            } else {
                //提取分享的基本信息
                String share_category = json.getString("share_category", "0");
                String share_description = json.getString("share_description", "");
                String share_img_path = json.getString("share_img_path");
                String share_city = json.getString("share_city", "0");
                String share_county = json.getString("share_county", "0");
                String share_lon = json.getString("share_lon", "0.0");
                String share_lat = json.getString("share_lat", "0.0");
                //活动IDs
                String activity_ids = json.getString("activity_ids", "");
                //群组IDs
                String group_ids = json.getString("group_ids", "");
                //用户IDs
                String user_ids = json.getString("user_ids", "");
                String share_img_path_with_lables = json.getString("share_img_path_with_lables", "");
                String share_img_path_with_pasters = json.getString("share_img_path_with_pasters", "");
                String share_location = json.getString("share_location", "");
                String sport_ids = json.getString("sport_ids", "");
                //这个是抽出来的贴纸信息
                String share_paster_ids = "";


                HashMap<String, String> activityMap = null;
                HashMap<String, String> groupMap = null;


                //检查活动的参数-Json数组-只允许@一个活动
                if (StringUtil.isBlank(activity_ids) || StringUtil.isEmpty(activity_ids)) {
                    activity_ids = null;
                } else {
                    if (!isJSONArray(activity_ids)) {
                        code = ErrorConfig.CODE_1006;
                        subCode = 2;
                        desc = "[" + code + "]+" + "[" + subCode + "],activity_ids格式不符合";
                        errorInfo.setErrorInfo(code, subCode, desc, request, out);
                        return;
                        //throw new RequestException(code, subCode,desc);
                    } else {
                        JSONArray activityIds = JSONArray.parseArray(activity_ids);
                        //获取传来的id的个数
                        int idsSize = activityIds.size();
                        if (idsSize != 1) {
                            code = ErrorConfig.CODE_1005;
                            subCode = 3;
                            desc = "[" + code + "]+" + "[" + subCode + "],一条分享只允许@一个活动";
                            errorInfo.setErrorInfo(code, subCode, desc, request, out);
                            return;
                            //throw new RequestException(code, subCode,desc);
                        } else {
                            //目前只让@一个活动
                            //鉴别活动id
                            String activity_id = activityIds.getString(0);
                            String group_id = null;
                            if (StringUtil.isBlank(activity_id) || StringUtil.isEmpty(activity_id)) {
                                code = ErrorConfig.CODE_1005;
                                subCode = 4;
                                desc = "[" + code + "]+" + "[" + subCode + "],activity_id有误,活动不存在";
                                errorInfo.setErrorInfo(code, subCode, desc, request, out);
                                return;
                                //throw new RequestException(code, subCode,desc);
                            } else {
                                Map<String, Object> groupInfo = socialActivityServiceImpl.queryActivityByIdV(activity_id);
                                if (groupInfo == null) {
                                    code = ErrorConfig.CODE_1005;
                                    subCode = 4;
                                    desc = "[" + code + "]+" + "[" + subCode + "],activity_id有误,活动不存在";
                                    errorInfo.setErrorInfo(code, subCode, desc, request, out);
                                    return;
                                    //throw new RequestException(code, subCode,desc);
                                } else {
                                    //活动存在，看看是不是官方活动？官方活动没有群：group_id为空
                                    if (groupInfo.get("group_id") != null)
                                        group_id = groupInfo.get("group_id").toString();
                                    activityMap = new HashMap<String, String>();
                                    activityMap.put("group_id", null);//默认放个null进去
                                    //查看用户是否已经参加了该活动：1-已参加、-1-未参加、0-已退出
                                    String attendState = socialActivityServiceImpl.queryUserAttendStateV(user_id, activity_id);
                                    if (!attendState.equals("1")) {
                                        activityMap.put("hasAttend", "0");
                                    } else {
                                        activityMap.put("hasAttend", "1");
                                    }
                                    if (StringUtil.isBlank(group_id) || StringUtil.isEmpty(group_id)) {
                                        //官方活动，参加不参加都能@，没参加的先参加
                                        //activity_ids = activity_id;
                                        activityMap.put("isOfficial", "1");
                                    } else {
                                        //非官方活动，看参加没，没参加不能@
                                        //TODO-直接参加？
                                        activityMap.put("isOfficial", "0");
                                        if (!attendState.equals("1")) {
                                            code = ErrorConfig.CODE_1006;
                                            subCode = 5;
                                            desc = "[" + code + "]+" + "[" + subCode + "],非官方活动，没有参加不能@";
                                            errorInfo.setErrorInfo(code, subCode, desc, request, out);
                                            return;
                                            //throw new RequestException(code, subCode,desc);
                                        } else {
                                            //参加了让@
                                            //activity_ids = activity_id;
                                            activityMap.put("group_id", group_id);
                                        }
                                    }
                                    //TODO-两个？
                                    activityMap.put("activity_id", activity_id);
                                    activityMap.put("activity_ids", activity_ids);
                                }
                            }
                        }
                    }
                }




                //检查群组的参数-Json-只允许@一个群组
                if (StringUtil.isBlank(group_ids) || StringUtil.isEmpty(group_ids)) {
                    group_ids = null;
                } else {
                    if (!isJSONArray(group_ids)) {
                        code = ErrorConfig.CODE_1006;
                        subCode = 6;
                        desc = "[" + code + "]+" + "[" + subCode + "],group_ids格式不符合";
                        errorInfo.setErrorInfo(code, subCode, desc, request, out);
                        return;
                        //throw new RequestException(code, subCode,desc);
                    } else {
                        JSONArray groupIds = JSONArray.parseArray(group_ids);
                        //获取传来的id的个数
                        int idsSize = groupIds.size();
                        if (idsSize != 1) {
                            code = ErrorConfig.CODE_1005;
                            subCode = 7;
                            desc = "[" + code + "]+" + "[" + subCode + "],一条分享只允许@一个群组";
                            errorInfo.setErrorInfo(code, subCode, desc, request, out);
                            return;
                            //throw new RequestException(code, subCode,desc);
                        } else {
                            //目前只让@一个群组
                            //看看群组存在么
                            String group_id = groupIds.getString(0);
                            groupMap = new HashMap<String, String>();
                            if (StringUtil.isBlank(group_id) || StringUtil.isEmpty(group_id)) {
                                code = ErrorConfig.CODE_1005;
                                subCode = 8;
                                desc = "[" + code + "]+" + "[" + subCode + "],group_id有误,群组不存在";
                                errorInfo.setErrorInfo(code, subCode, desc, request, out);
                                return;
                                //throw new RequestException(code, subCode,desc);
                            } else {
                                //根据群组ID获取群组详情
                                Map<String, String> groupInfo = socialGroupServiceImpl.queryGroupInfo(group_id);
                                if (groupInfo == null) {
                                    code = ErrorConfig.CODE_1005;
                                    subCode = 8;
                                    desc = "[" + code + "]+" + "[" + subCode + "],group_id有误，群组不存在";
                                    errorInfo.setErrorInfo(code, subCode, desc, request, out);
                                    return;
                                    //throw new RequestException(code, subCode,desc);
                                } else {
                                    //群组存在,看看参加没有
                                    boolean isJoin = socialGroupServiceImpl.queryIdByGroupAndUserIdV(group_id, user_id);
                                    if (isJoin == false) {
                                        code = ErrorConfig.CODE_1005;
                                        subCode = 9;
                                        desc = "[" + code + "]+" + "[" + subCode + "],@了未加入的群组";
                                        errorInfo.setErrorInfo(code, subCode, desc, request, out);
                                        return;
                                        //throw new RequestException(code, subCode,desc);
                                    } else {
                                        //group_ids = group_id;
                                        groupMap.put("group_ids", group_ids);//这是存的，为了不该群相册接口，还是存一个array,以后如果可以关联多个群组什么的也可以
                                        groupMap.put("group_id", group_id);//这是拿到service层做比较的，不用再从array转化一次
                                    }
                                }
                            }
                        }
                    }
                }


                //@用户
                if (StringUtil.isBlank(user_ids) || StringUtil.isEmpty(user_ids)) {
                    user_ids = null;
                } else {
                    if (!isJSONArray(user_ids)) {
                        code = ErrorConfig.CODE_1005;
                        subCode = 10;
                        desc = "[" + code + "]+" + "[" + subCode + "],user_ids不符合";
                        errorInfo.setErrorInfo(code, subCode, desc, request, out);
                        return;
                        //throw new RequestException(code, subCode,desc);
                    } else {
                        JSONArray userIds = JSONArray.parseArray(user_ids);
                        //获取传来的id的个数
                        int idsSize = userIds.size();
                        if (idsSize < 1 || idsSize > 3) {
                            code = ErrorConfig.CODE_1005;
                            subCode = 11;
                            desc = "[" + code + "]+" + "[" + subCode + "],一条分享只允许1-3个好友";
                            errorInfo.setErrorInfo(code, subCode, desc, request, out);
                            return;
                            //throw new RequestException(code, subCode,desc);
                        } else {
                            //看看重不重复
                            Set<String> userIdSet = new HashSet<String>();
                            for (int i = 0; i < idsSize; i++) {
                                userIdSet.add(userIds.getString(i));
                            }
                            if (idsSize != userIdSet.size()) {
                                code = ErrorConfig.CODE_1005;
                                subCode = 12;
                                desc = "[" + code + "]+" + "[" + subCode + "],一个好友只能@一次";
                                errorInfo.setErrorInfo(code, subCode, desc, request, out);
                                return;
                                //throw new RequestException(code, subCode,desc);
                            }


                            //TODO-批量查询数据库
                            //看看好友存在么
                            for (int i = 0; i < idsSize; i++) {
                                String userId = userIds.getString(i);
                                //存在性
                                boolean isUser = userServiceImpl.queryUserIdById(userId);
                                if (isUser == false) {
                                    code = ErrorConfig.CODE_1005;
                                    subCode = 13;
                                    desc = "[" + code + "]+" + "[" + subCode + "],@了一个不存在的用户";
                                    errorInfo.setErrorInfo(code, subCode, desc, request, out);
                                    return;
                                    //throw new RequestException(code, subCode,desc);
                                }


                                //是否好友
                                boolean isAttention = socialFollowingServiceImpl.queryFollowingIdV(user_id, userId);
                                boolean isAttention1 = socialFollowingServiceImpl.queryFollowingIdV(userId, user_id);
                                if (isAttention == false || isAttention1 == false) {
                                    code = ErrorConfig.CODE_1005;
                                    subCode = 14;
                                    desc = "[" + code + "]+" + "[" + subCode + "],@了一个不是好友的用户";
                                    errorInfo.setErrorInfo(code, subCode, desc, request, out);
                                    return;
                                    //throw new RequestException(code, subCode,desc);
                                }
                            }
                        }
                    }
                }


                //图片标签-"share_img_path_with_lables":[{"pic1_url":[]},{"pic2_url":[]},{"pic3_url":[]}]
                //键key:存图片的Url，值Value:存图片对应的标签
                if (!"".equals(share_img_path_with_lables)) {
                    //判断是否是json格式
                    if (!isJSONArray(share_img_path_with_lables)) {
                        code = ErrorConfig.CODE_1006;
                        subCode = 15;
                        desc = "[" + code + "]+" + "[" + subCode + "],请求参数格式不符,检查share_img_path_with_lables";
                        errorInfo.setErrorInfo(code, subCode, desc, request, out);
                        return;
                        //throw new RequestException(code, subCode,desc);
                    }
                    //解析share_img_path_with_lables的值，真实的图片，总共有9张图片
                    JSONArray shareImgLableArr = JSONArray.parseArray(share_img_path_with_lables);
                    int urlSize = shareImgLableArr.size();
                    if (urlSize > 9) {
                        code = ErrorConfig.CODE_1005;
                        subCode = 16;
                        desc = "[" + code + "]+" + "[" + subCode + "],分享不能超过9张图,检查share_img_path_with_lables";
                        errorInfo.setErrorInfo(code, subCode, desc, request, out);
                        return;
                        //throw new RequestException(code, subCode,desc);
                    } else {
                        //遍历，看看必填的参数是不是都填了。
                        //遍历每个图片附带的标签属性
                        for (int i = 0; i < urlSize; i++) {
                            String shareImgLables = shareImgLableArr.getString(i);//"pic1_url":["":"","":""]
                            HJSONObject shareImgLablesJson = new HJSONObject(JSONObject.parseObject(shareImgLables));
                            //获取第i张图片的键key，存放的是pic_url的Url路径，所以key的size为1
                            Set<String> keyset = shareImgLablesJson.keySet();
                            if (keyset.size() != 1) {
                                code = ErrorConfig.CODE_1006;
                                subCode = 15;
                                desc = "[" + code + "]+" + "[" + subCode + "],请求参数格式不符,检查share_img_path_with_lables";
                                errorInfo.setErrorInfo(code, subCode, desc, request, out);
                                return;
                                //throw new RequestException(code, subCode,desc);
                            } else {
                                String url = keyset.iterator().next();
                                if (!isUrl(url)) {
                                    code = ErrorConfig.CODE_1006;
                                    subCode = 15;
                                    desc = "[" + code + "]+" + "[" + subCode + "],请求参数格式不符,检查share_img_path_with_lables";
                                    errorInfo.setErrorInfo(code, subCode, desc, request, out);
                                    return;
                                    //throw new RequestException(code, subCode,desc);
                                }
                                //根据键key获取对应的值value，其中存放的是图片对应的标签信息
                                String shareImgLablesStr = shareImgLablesJson.getString(url);
                                JSONArray shareImgLablesArr = JSONArray.parseArray(shareImgLablesStr);
                                int lableSize = shareImgLablesArr.size();
                                if (lableSize > 6) {
                                    code = ErrorConfig.CODE_1005;
                                    subCode = 17;
                                    desc = "[" + code + "]+" + "[" + subCode + "],一张图片不能超过6个标签";
                                    errorInfo.setErrorInfo(code, subCode, desc, request, out);
                                    return;
                                    //throw new RequestException(code, subCode,desc);
                                } else {
                                    for (int j = 0; j < lableSize; j++) {
                                        String shareImgLable = shareImgLablesArr.getString(j);
                                        //标签在图片上的位置，还有牌子信息必填
                                        HJSONObject shareImgLableJson = HJSONHandler(shareImgLable, new String[]{"offset_x", "offset_y", "brand", "direction"});
                                        String offset_x = shareImgLableJson.getString("offset_x");
                                        try {
                                            Double.parseDouble(offset_x);
                                        } catch (Exception e) {
                                            code = ErrorConfig.CODE_1005;
                                            subCode = 18;
                                            desc = "[" + code + "]+" + "[" + subCode + "],非法的x偏移参数";
                                            errorInfo.setErrorInfo(code, subCode, desc, request, out);
                                            return;
                                            //throw new RequestException(code, subCode,desc);
                                        }


                                        String offset_y = shareImgLableJson.getString("offset_y");
                                        try {
                                            Double.parseDouble(offset_y);
                                        } catch (Exception e) {
                                            code = ErrorConfig.CODE_1005;
                                            subCode = 19;
                                            desc = "[" + code + "]+" + "[" + subCode + "],非法的y偏移参数";
                                            errorInfo.setErrorInfo(code, subCode, desc, request, out);
                                            return;
                                            //throw new RequestException(code, subCode,desc);
                                        }


                                        String direction = shareImgLableJson.getString("direction");
                                        String brand = shareImgLableJson.getString("brand");
                                        //这个需要一个brand数据表
                                        String altitude = shareImgLableJson.getString("altitude", "");
                                        String number = shareImgLableJson.getString("number", "");
                                        String img_label_type = shareImgLableJson.getString("img_label_type", "");


                                        shareImgLableJson.clear();


                                        shareImgLableJson.put("offset_x", offset_x);
                                        shareImgLableJson.put("offset_y", offset_y);
                                        shareImgLableJson.put("brand", brand);
                                        shareImgLableJson.put("altitude", altitude);
                                        shareImgLableJson.put("number", number);
                                        shareImgLableJson.put("img_label_type", img_label_type);
                                        shareImgLableJson.put("direction", direction);


                                        shareImgLablesArr.set(j, shareImgLableJson);
                                    }
                                }
                                shareImgLablesJson.put(url, shareImgLablesArr);
                            }
                            shareImgLableArr.set(i, shareImgLablesJson);


                            share_img_path_with_lables = shareImgLableArr.toString();
                        }
                    }
                }


                //贴纸，类似标签的数据结构， "share_paster_ids":[{"url1":["1","2"]},{"url2":["1","2"]}]
                if (!"".equals(share_img_path_with_pasters)) {
                    //判断是否是json格式
                    if (!isJSONArray(share_img_path_with_pasters)) {
                        code = ErrorConfig.CODE_1006;
                        subCode = 20;
                        desc = "[" + code + "]+" + "[" + subCode + "],请求参数格式不符,检查share_img_path_with_pasters";
                        errorInfo.setErrorInfo(code, subCode, desc, request, out);
                        return;
                        //throw new RequestException(code, subCode,desc);
                    }
                    JSONArray shareImgPasterUrlsArr = JSONArray.parseArray(share_img_path_with_pasters);
                    Set<String> shareImgPasterIdsSet = new HashSet<String>();
                    int urlSize = shareImgPasterUrlsArr.size();
                    if (urlSize > 9) {
                        code = ErrorConfig.CODE_1005;
                        subCode = 21;
                        desc = "[" + code + "]+" + "[" + subCode + "],分享不能超过9张图,检查share_img_path_with_pasters";
                        errorInfo.setErrorInfo(code, subCode, desc, request, out);
                        return;
                        //throw new RequestException(code, subCode,desc);
                    } else {
                        for (int i = 0; i < urlSize; i++) {
                            String shareImgPasterIdsArrStr = shareImgPasterUrlsArr.getString(i);
                            HJSONObject shareImgPasterIdsArrJson = new HJSONObject(JSONObject.parseObject(shareImgPasterIdsArrStr));
                            Set<String> keyset = shareImgPasterIdsArrJson.keySet();
                            if (keyset.size() != 1) {
                                code = ErrorConfig.CODE_1006;
                                subCode = 20;
                                desc = "[" + code + "]+" + "[" + subCode + "],请求参数格式不符,检查share_img_path_with_pasters";
                                errorInfo.setErrorInfo(code, subCode, desc, request, out);
                                return;
                                //throw new RequestException(code, subCode,desc);
                            } else {
                                String url = keyset.iterator().next();
                                if (!isUrl(url)) {
                                    code = ErrorConfig.CODE_1006;
                                    subCode = 20;
                                    desc = "[" + code + "]+" + "[" + subCode + "],请求参数格式不符,检查share_img_path_with_pasters";
                                    errorInfo.setErrorInfo(code, subCode, desc, request, out);
                                    return;
                                    //throw new RequestException(code, subCode,desc);
                                }
                                String shareImgPasterIdsStr = shareImgPasterIdsArrJson.getString(url);
                                JSONArray shareImgPasterIdsArr = JSONArray.parseArray(shareImgPasterIdsStr);
                                int idsSize = shareImgPasterIdsArr.size();
                                if (idsSize > 5) {
                                    code = ErrorConfig.CODE_1005;
                                    subCode = 22;
                                    desc = "[" + code + "]+" + "[" + subCode + "],一张图不能超过5个贴纸";
                                    errorInfo.setErrorInfo(code, subCode, desc, request, out);
                                    return;
                                    //throw new RequestException(code, subCode,desc);
                                } else {
                                    for (int j = 0; j < idsSize; j++) {
                                        String paster_id = shareImgPasterIdsArr.getString(j);


                                        try {
                                            int paster_id_int = Integer.parseInt(paster_id);
                                            shareImgPasterIdsSet.add(String.valueOf(paster_id_int));
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            subCode = 20;
                                            desc = "[" + code + "]+" + "[" + subCode + "],请求参数格式不符,检查share_img_path_with_pasters";
                                            errorInfo.setErrorInfo(code, subCode, desc, request, out);
                                            return;
                                            //throw new RequestException(code, subCode,desc);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }


                //运动类型 "sport_ids":["1"]-json-一个分享智能关联一个运动类型
                if (StringUtil.isBlank(sport_ids) || StringUtil.isEmpty(sport_ids)) {
                    sport_ids = null;
                } else {
                    if (!isJSONArray(sport_ids)) {
                        code = ErrorConfig.CODE_1005;
                        subCode = 26;
                        desc = "[" + code + "]+" + "[" + subCode + "],sport_ids不符合";
                        errorInfo.setErrorInfo(code, subCode, desc, request, out);
                        return;
                        //throw new RequestException(code, subCode,desc);
                    } else {
                        JSONArray sportIds = JSONArray.parseArray(sport_ids);
                        //获取传来的id的个数
                        int idsSize = sportIds.size();
                        if (idsSize != 1) {
                            code = ErrorConfig.CODE_1005;
                            subCode = 27;
                            desc = "[" + code + "]+" + "[" + subCode + "],一条分享只关联1个运动类型";
                            errorInfo.setErrorInfo(code, subCode, desc, request, out);
                            return;
                            //throw new RequestException(code, subCode,desc);
                        } else {
                            //看看运动类型存在么
                            for (int i = 0; i < idsSize; i++) {
                                String sportId = sportIds.getString(i);
                                //存在性
                                String sport_id = sportIds.getString(i);
                                try {
                                    int sport_id_i = Integer.parseInt(sport_id);
                                    Boolean is_user_sport_id_exist = userPersonServiceImpl.queryUserSportBySportId(String.valueOf(sport_id_i));
                                    if (!is_user_sport_id_exist) {
                                        throw new Exception();
                                    }
                                    sportIds.set(i, String.valueOf(sport_id_i));
                                } catch (Exception e) {
                                    code = ErrorConfig.CODE_1005;
                                    subCode = 28;
                                    desc = "[" + code + "]+" + "[" + subCode + "],用户运动类型有误";
                                    errorInfo.setErrorInfo(code, subCode, desc, request, out);
                                    return;
                                    //throw new RequestException(code, subCode,desc);
                                }
                            }


                            sport_ids = sportIds.toString();
                        }
                    }
                }


                if (!"".equals(share_location)) {
                    if (StringUtil.isBlank(share_location) || StringUtil.isEmpty(share_location)) {
                        code = ErrorConfig.CODE_1006;
                        subCode = 25;
                        desc = "[" + code + "]+" + "[" + subCode + "],请检查share_location是否为空";
                        errorInfo.setErrorInfo(code, subCode, desc, request, out);
                        return;
                        //throw new RequestException(code, subCode,desc);
                    }
                }


                if (StringUtil.isBlank(share_img_path) || StringUtil.isEmpty(share_img_path) || Integer.parseInt(share_category) < 0 || Integer.parseInt(share_category) > 1 || !isJSONArray(share_img_path)) {
                    code = ErrorConfig.CODE_1006;
                    subCode = 23;
                    desc = "[" + code + "]+" + "[" + subCode + "],请求参数格式不符,检查share_img_path和share_category";
                    errorInfo.setErrorInfo(code, subCode, desc, request, out);
                    return;
                    //throw new RequestException(code, subCode,desc);
                } else {
                    if (isJsonEmpty(share_img_path)) {
                        code = ErrorConfig.CODE_1006;
                        subCode = 23;
                        desc = "[" + code + "]+" + "[" + subCode + "],请求参数格式不符,检查share_img_path和share_category";
                        throw new RequestException(code, subCode, desc);
                    }
                    JSONArray share_img_path_json = JSONArray.parseArray(share_img_path);
                    //获取传来的id的个数
                    int idsSize = share_img_path_json.size();
                    if (idsSize > 9) {
                        code = ErrorConfig.CODE_1005;
                        subCode = 29;
                        desc = "[" + code + "]+" + "[" + subCode + "],一条分享最多关联9张图片";
                        errorInfo.setErrorInfo(code, subCode, desc, request, out);
                        return;
                        //throw new RequestException(code, subCode,desc);
                    } else {
                        for (int i = 0; i < idsSize; i++) {
                            String url = share_img_path_json.getString(i);
                            if (!isUrl(url)) {
                                code = ErrorConfig.CODE_1005;
                                subCode = 30;
                                desc = "[" + code + "]+" + "[" + subCode + "],分享图片中有不合法的url";
                                errorInfo.setErrorInfo(code, subCode, desc, request, out);
                                return;
                                //throw new RequestException(code, subCode,desc);
                            }
                        }


                        share_img_path = share_img_path_json.toString();
                    }


                    if (share_category.equals("0")) {
                        try {
                            //调用创建分享的服务,成功则返回分享的编号
                            share_id = socialShareServiceImpl.insertShareWithAt(user_id, share_category, share_description,
                                    share_img_path, share_city, share_county, share_lon, share_lat, activityMap, groupMap, user_ids,
                                    share_img_path_with_lables, share_paster_ids, share_img_path_with_pasters, share_location, sport_ids);
                        } catch (Exception e) {
                            e.printStackTrace();
                            code = ErrorConfig.CODE_1006;
                            subCode = 24;
                            desc = "[" + code + "]+" + "[" + subCode + "],数据库更新失败";
                            errorInfo.setErrorInfo(code, subCode, desc, request, out);
                            return;
                        }
                    } else if (share_category.equals("1")) {
                        //承诺的实现还没写
                        try {
                            socialShareServiceImpl.insertPromiseWithAt(user_id, share_category, share_description,
                                    share_img_path, share_city, share_county, share_lon, share_lat, activityMap, groupMap, user_ids, share_img_path_with_lables);
                        } catch (Exception e) {
                            code = ErrorConfig.CODE_1006;
                            subCode = 24;
                            desc = "[" + code + "]+" + "[" + subCode + "],数据库更新失败";
                            errorInfo.setErrorInfo(code, subCode, desc, request, out);
                            return;
                        }
                    }
                }
            }


        } catch (ParamException e) {
            code = ErrorConfig.CODE_1005;
            subCode = 0;
            desc = "[" + code + "]+" + "[" + subCode + "],请求参数缺失";
            rtn.put("subCode", subCode);
            rtn.put("desc", desc);
            errorInfo.setErrorInfo(code, subCode, desc, request, out);
            return;
        }
        rtn.put("code", code);


        //Todo 添加真实的返回share_id
        rtn.put("share_id", share_id);


        request.setAttribute("user_id", user_id);


        out.print(responseHandler(rtn, request));
        out.close();
    }
```

## 清晰的业务代码逻辑

一般来说，一个 Controller 是进行一个业务流程的处理，而该流程可能根据输入参数的不同、当前状态的不同导致走不同的逻辑流线。正如上文中列举的代码，正是因为存在着多个可选参数导致存在着大量的 if-else 分支。而用面条式的、瀑布流式的代码是最好的逻辑表现，但是一旦分支多了，if-else 多了之后，就变成了一团乱麻。笔者不是否认在充分的注释情况下可以完全理解原有代码，但是这样的可读性依然很差。笔者之前一直在测试中比较喜欢 BDD 方式，譬如：

```
  Scenario: 两数相加
    Given 我有一个计算器
    And 我向计算器输入50
    And 我向计算器输入70
    When 我点击累加
    Then 我应该看到结果120
```

在 OverView 部分，笔者呈现出的代码也希望能符合这个特性:

```
requestHandler().
step("我是本步骤的描述").
reducer("我是本步骤的第一个可能情况").
reducer("我是本步骤的第二个可能情况")
```

## 全局状态与局部状态的分割

在 Redux 中，会把所有的状态放在 Store 中进行统一管理，这样就把状态变量和临时变量区分了开来，即把全局状态与局部状态分割了开来。笔者没有真实的大公司的工作经验，不过在一个可能几百行的逻辑处理中，很有可能出现大量的 a,b,c,d 这样临时的变量，然后在最后构造返回数据时，随手把之前的某个临时变量封装进去。此外，整个逻辑线的不同步骤中的局部变量可能相互干扰，可能在步骤一中定义了某个临时变量，步骤二中没用到，然后步骤三中突然拿来进行某个条件的判断，然后步骤四中再重新赋予其他用法。然后在维护的时候，大家都懵逼了。

## 可回溯性

Redux 有一个非常诱人的特性叫时空旅行，即可以回溯整个应用生命周期中的所有状态。这个特性主要是基于其状态树，可以回溯出每个操作之后的状态。笔者认为可回溯性在业务逻辑开发中的表现即是全局状态的记录与每次业务处理中的局部状态的记录。之前进行 Log 的时候，有很大的随意性，而有了一个全局状态之后，可以对于输入、输出进行统一的记录。而对于中间状态，因为划分了 Step 和不同 Step 之间统一的 Action，也可以方便的记录下来。在这种情况下，只要了解每个 Step 具体执行了哪个 Reducer，并且每个 Reducer 的输出值，可以很快定位到问题代码所在的地方。

## 可容错性

上文中已经提及，因为 RARF 是记录了每个 Step 的情况，那么可以很快的定位到错误代码。此外，由于全部的代码是包裹在了 Observable 之中，那么任何的未知错误也都可以进行妥善处理，而不用担心不可回溯。

## 并发编程与异步实现

随着计算机硬件，笔者比较推崇响应式流这种异步模型，在本部分的实现中，笔者也是优先考虑了并发的易实现性。RARF 将整个业务处理逻辑分成了数个 Step，如果你发现哪个 Step 是 IO 密集型或者计算密集型，简言之，就需要耗费较长的时间，那么完全可以放到新线程中执行，而把 Main 线程流出来响应新的请求。
RxJava 这种链式调用形式的异步写法很是清晰明了，不过需要注意的是 RxJava 本身并不一定是并发地，默认情况下所有的 Observable 与 Subscriber 都是在一个线程里运行，但是可以简单的使用`subscribeOn`方法将某个 Observable 扔到子线程中运行。

# Terminology

![](http://7xkt0f.com1.z0.glb.clouddn.com/0F1F45B5-53DB-4768-8122-BE7F43151230.png)

## Context

Context 即对应一个业务处理流程，包含多个逻辑线。一个 Context 会被划分为输入处理、一到多个中间过程以及最后的输出处理。

## Step

一个 Step 即是某个具体的业务处理块，一个 Step 包含一到多个 Reducer，每个 Reducer 表示该步骤可能的一个逻辑线。一般来说，一起请求中一个 Step 中只有一个 Reducer 会有效执行，即返回有效的 Action。如果某个 Step 中所有 Action 都没有执行，那么会返回一个错误代码。

## UniResourceBag

资源包即是上文描述的全局状态存放的地方，包括输入、输出。

## Reducer & Action

每个 Reducer 的执行块都会返回一个 Action 对象，如果 Action 的 isValid 值为 True，表示要把该 Action 发射到下一个 Observable，否则不进行发射。每个 Reducer 首先会对上一步 Step 传入的 Action 类型进行判断，只有在是自己需要的类型的情况下才会进行执行。每个 Reducer 在有效执行后会发射有效地 Action 到下一步中。

### RequestHandler:对于请求数据进行预处理

请求方式基于 RARF 中的 URFP，即是 PathVariable 与 RequestData 混合的方式，一个典型的登录请求为：

```
/login/{verifyCode}?requestData={"username":chevalier,"password":123456}
```

### ResponseHandler:对于输出数据进行处理

ResponseHandler 会对 ResourceBag 中的数据进行最终的处理校验，同时负责处理日志啊、消息推送啊等等非必须逻辑的异步实现。
