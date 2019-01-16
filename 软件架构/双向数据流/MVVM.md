


## MVVM: 数据绑定与无状态的视图
Model View View-Model模型是MV*家族中最年轻的一位，也是由Microsoft提出，并经由Martin Fowler布道传播。MVVM源于Martin Fowler的Presentation Model，Presentation Model的核心在于接管了View所有的行为响应，View的所有响应与状态都定义在了Presentation Model中。也就是说，View不会包含任意的状态。举个典型的使用场景，当用户点击某个按钮之后，状态信息是从Presentation Model传递给Model，而不是从View传递给Presentation Model。任何控制组件间的逻辑操作，即上文所述的ViewLogic，都应该放置在Presentation Model中进行处理，而不是在View层，这一点也是MVP模式与Presentation Model最大的区别。
MVVM模式进一步深化了Presentation Model的思想，利用Data Binding等技术保证了View中不会存储任何的状态或者逻辑操作。在WPF中，UI主要是利用XAML或者XML创建，而这些标记类型的语言是无法存储任何状态的，就像HTML一样(因此JSX语法其实是将View又有状态化了)，只是允许UI与某个ViewModel中的类建立映射关系。渲染引擎根据XAML中的声明以及来自于ViewModel的数据最终生成呈现的页面。因为数据绑定的特性，有时候MVVM也会被称作MVB:Model View Binder。总结一下，MVVM利用数据绑定彻底完成了从命令式编程到声明式编程的转化，使得View逐步无状态化。一个典型的MVVM的使用场景为：
- 用户交互输入
- View将数据直接传送给ViewModel，ViewModel保存这些状态数据
- 在有需要的情况下，ViewModel会将数据传送给Model
- Model在更新完成之后通知ViewModel
- ViewModel从Model中获取最新的模型，并且更新自己的数据状态
- View根据最新的ViewModel的数据进行重新渲染


![](https://coding.net/u/hoteam/p/Cache/git/raw/master/2016/7/2/BB708F10-1F39-4FFE-A66C-319293AAC71F.png)

根据上述流程，我们可知MVVM模式的特性为：
- ViewModel、Model中存在ViewLogic实现，View则不保存任何状态信息
- View不需要了解ViewModel的实现细节，但是会声明自己所需要的数据类型，并且能够知道如何重新渲染
- ViewModel不需要了解View的实现细节(非命令式编程)，但是需要根据View声明的数据类型传入对应的数据。ViewModel需要了解Model的实现细节。
-  Model不需要了解View的实现细节，需要了解ViewModel的实现细节




# iOS
![](https://coding.net/u/hoteam/p/Cache/git/raw/master/2016/7/2/1-uhPpTHYzTmHGrAZy8hiM7w.png)
```
import UIKit


struct Person { // Model
    let firstName: String
    let lastName: String
}


protocol GreetingViewModelProtocol: class {
    var greeting: String? { get }
    var greetingDidChange: ((GreetingViewModelProtocol) -> ())? { get set } // function to call when greeting did change
    init(person: Person)
    func showGreeting()
}


class GreetingViewModel : GreetingViewModelProtocol {
    let person: Person
    var greeting: String? {
        didSet {
            self.greetingDidChange?(self)
        }
    }
    var greetingDidChange: ((GreetingViewModelProtocol) -> ())?
    required init(person: Person) {
        self.person = person
    }
    func showGreeting() {
        self.greeting = "Hello" + " " + self.person.firstName + " " + self.person.lastName
    }
}


class GreetingViewController : UIViewController {
    var viewModel: GreetingViewModelProtocol! {
        didSet {
            self.viewModel.greetingDidChange = { [unowned self] viewModel in
                self.greetingLabel.text = viewModel.greeting
            }
        }
    }
    let showGreetingButton = UIButton()
    let greetingLabel = UILabel()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.showGreetingButton.addTarget(self.viewModel, action: "showGreeting", forControlEvents: .TouchUpInside)
    }
    // layout code goes here
}
// Assembling of MVVM
let model = Person(firstName: "David", lastName: "Blaine")
let viewModel = GreetingViewModel(person: model)
let view = GreetingViewController()
view.viewModel = viewModel
```
- Distribution:在Cocoa MVVM中，View相对于MVP中的View担负了更多的功能，譬如需要构建数据绑定等等
- Testability:ViewModel拥有View中的所有数据结构，因此很容易就可以进行测试
- 易用性:相对而言有很多的冗余代码


# Android

- XML中声明数据绑定
```
<data>
        <variable
            name="viewModel"
            type="uk.ivanc.archimvvm.viewmodel.MainViewModel"/>
</data>
...

            <EditText
                android:id="@+id/edit_text_username"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_toLeftOf="@id/button_search"
                android:hint="@string/hit_username"
                android:imeOptions="actionSearch"
                android:inputType="text"
                android:onEditorAction="@{viewModel.onSearchAction}"
                android:textColor="@color/white"
                android:theme="@style/LightEditText"
                app:addTextChangedListener="@{viewModel.usernameEditTextWatcher}"/>



```
- View中绑定ViewModel
```
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.main_activity);
        mainViewModel = new MainViewModel(this, this);
        binding.setViewModel(mainViewModel);
        setSupportActionBar(binding.toolbar);
        setupRecyclerView(binding.reposRecyclerView);
```
- ViewModel中进行数据操作
```
public boolean onSearchAction(TextView view, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            String username = view.getText().toString();
            if (username.length() > 0) loadGithubRepos(username);
            return true;
        }
        return false;
    }


    public void onClickSearch(View view) {
        loadGithubRepos(editTextUsernameValue);
    }


    public TextWatcher getUsernameEditTextWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {


            }


            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                editTextUsernameValue = charSequence.toString();
                searchButtonVisibility.set(charSequence.length() > 0 ? View.VISIBLE : View.GONE);
            }


            @Override
            public void afterTextChanged(Editable editable) {


            }
        };
}
```



