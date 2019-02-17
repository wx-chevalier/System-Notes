


# MVP: 将视图与模型解耦
维基百科将[MVP](http://en.wikipedia.org/wiki/Model%E2%80%93view%E2%80%93presenter)称为MVC的一个推导扩展，观其渊源而知其所以然。对于MVP概念的定义，Microsoft较为明晰，而Martin Fowler的定义最为广泛接受。MVP模式在WinForm系列以Visual-XXX命名的编程语言与Java Swing等系列应用中最早流传开来，不过后来ASP.NET以及JFaces也广泛地使用了该模式。在MVP中用户不再与Presenter进行直接交互，而是由View完全接管了用户交互，譬如窗口上的每个控件都知道如何响应用户输入并且合适地渲染来自于Model的数据。而所有的事件会被传输给Presenter，Presenter在这里就是View与Model之间的中间人，负责控制Model进行修改以及将最新的Model状态传递给View。这里描述的就是典型的所谓Passive View版本的MVP，其典型的用户场景为：
- 用户交互输入了某些内容
- View将用户输入转化为发送给Presenter
- Presenter控制Model接收需要改变的点
- Model将更新之后的值返回给Presenter
- Presenter将更新之后的模型返回给View


![](https://coding.net/u/hoteam/p/Cache/git/raw/master/2016/7/2/28983226-1BC6-4AD2-900B-E7D254266D4F.png)
根据上述流程，我们可知Passive View版本的MVP模式的特性为：
- View、Presenter、Model中皆有ViewLogic的部分实现
- Presenter负责连接View与Model，需要了解View与Model的细节。
- View需要了解Presenter的细节，将用户输入转化为事件传递给Presenter
- Model需要了解Presenter的细节，在完成更新之后将最新的模型传递给Presenter
- View与Model之间相互解耦合


### Supervising Controller MVP
简化Presenter的部分功能，使得Presenter只起到需要复杂控制或者调解的操作，而简单的Model展示转化直接由View与Model进行交互：
![](https://coding.net/u/hoteam/p/Cache/git/raw/master/2016/7/2/EB81D8B6-227A-4E94-8107-C6DCC7920574.png)






# iOS MVP
![](https://coding.net/u/hoteam/p/Cache/git/raw/master/2016/7/2/1-hKUCPEHg6TDz6gtOlnFYwQ.png)
Cocoa中MVP模式是将ViewController当做纯粹的View进行处理，而将很多的ViewLogic与模型操作移动到Presenter中进行，代码如下:

```
import UIKit


struct Person { // Model
    let firstName: String
    let lastName: String
}


protocol GreetingView: class {
    func setGreeting(greeting: String)
}


protocol GreetingViewPresenter {
    init(view: GreetingView, person: Person)
    func showGreeting()
}


class GreetingPresenter : GreetingViewPresenter {
    unowned let view: GreetingView
    let person: Person
    required init(view: GreetingView, person: Person) {
        self.view = view
        self.person = person
    }
    func showGreeting() {
        let greeting = "Hello" + " " + self.person.firstName + " " + self.person.lastName
        self.view.setGreeting(greeting)
    }
}


class GreetingViewController : UIViewController, GreetingView {
    var presenter: GreetingViewPresenter!
    let showGreetingButton = UIButton()
    let greetingLabel = UILabel()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.showGreetingButton.addTarget(self, action: "didTapButton:", forControlEvents: .TouchUpInside)
    }
    
    func didTapButton(button: UIButton) {
        self.presenter.showGreeting()
    }
    
    func setGreeting(greeting: String) {
        self.greetingLabel.text = greeting
    }
    
    // layout code goes here
}
// Assembling of MVP
let model = Person(firstName: "David", lastName: "Blaine")
let view = GreetingViewController()
let presenter = GreetingPresenter(view: view, person: model)
view.presenter = presenter
```
- Distribution:主要的业务逻辑分割在了Presenter与Model中，View相对呆板一点
- Testability:较为方便地测试
- 易用性:代码职责分割的更为明显，不过不像MVC那样直观易懂了

# Android



- 将Presenter与View绑定，并且将用户响应事件绑定到Presenter中

```
        //Set up presenter
        presenter = new MainPresenter();
        presenter.attachView(this);
        ...

        
        // Set up search button
        searchButton = (ImageButton) findViewById(R.id.button_search);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.loadRepositories(editTextUsername.getText().toString());
            }
        });
```
- Presenter中调用Model更新数据，并且调用View中进行重新渲染
```
    public void loadRepositories(String usernameEntered) {
        String username = usernameEntered.trim();
        if (username.isEmpty()) return;


        mainMvpView.showProgressIndicator();
        if (subscription != null) subscription.unsubscribe();
        ArchiApplication application = ArchiApplication.get(mainMvpView.getContext());
        GithubService githubService = application.getGithubService();
        subscription = githubService.publicRepositories(username)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(application.defaultSubscribeScheduler())
                .subscribe(new Subscriber<List<Repository>>() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "Repos loaded " + repositories);
                        if (!repositories.isEmpty()) {
                            mainMvpView.showRepositories(repositories);
                        } else {
                            mainMvpView.showMessage(R.string.text_empty_repos);
                        }
                    }


                    @Override
                    public void onError(Throwable error) {
                        Log.e(TAG, "Error loading GitHub repos ", error);
                        if (isHttp404(error)) {
                            mainMvpView.showMessage(R.string.error_username_not_found);
                        } else {
                            mainMvpView.showMessage(R.string.error_loading_repos);
                        }
                    }


                    @Override
                    public void onNext(List<Repository> repositories) {
                        MainPresenter.this.repositories = repositories;
                    }
                });
        }


```





