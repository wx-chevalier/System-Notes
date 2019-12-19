## Reactive Abstract Resource Flow

# Introduction

Inspired By Flux and Microservice，I want to do something different in server side. I am a fresh bird for developing ， but has some practice in both frontend and backend. Recently, I develop an app containing iOS, android and web client(React). I use the Spring MVC as server-side framework. It’s good, but the mvc make me sometimes annoyed. Spring MVC mapping request to single controller according to its uri, that works well but demand or the product of my app suffers frequent change. Some one think its called agile, but result in so much deprecated code .

Sometime, even the front-end add one element in its view, I need to rewrite one controller to adapt to suitable RequestData. So , I want to present my Asynchronous Abstract Resource Flow idea. I don’t know wherther it is good and it;s only during the draft progress, no one except me know what it is.

I think the impression of Docker, which ships data via container between different sevice, may be a little alike. AARF don’t map request to one controller, it’s not Ordering food， but self-help.

In this post, I use AARF as abbreviation to indicate on Asynchronous Abstract Resource Flow.Main principle of AARF is split the complex business logic into micro handlers, which is mapping to single resource. Besides, for the convenience of MVVM(data binding), responded data is

In conclusion, the front-end draft the responsed data structure by define the resource flow path and required attribute. The server-side provide essential constraints of resources and relation.

# Resource

## Attribute

**attribute is the most granular level atom in one model, and each attribute has its unique name.**For example, in a simple model of library ， there are several attributes, user_name, user_age, book_name, book_author. user_name and user_age can be allocated to User Resource naturally. On the other hand, book_name and book_author are assigned to Book Resource. In addition, User Resource has one attribute called user_id, this is identify for one user resource.

In conclusion， there are two category of attributes in AARF model, one is called identify attribute, which can be used to index one resource and can be shared among different resource. Anonther is called value attribute, which only can be owned by one Resource.

## Resource Definition

**Any resource has at least one unique value attribute besides any resource id.**For example, in the library case, Comment may have comment_id, user_id and book_id, all of them are resource_id. If comment doesn’t have comment_content, which id unique attribute only belongs to the Comment Resource, Comment can’t be regarded as resource. It just called relation ,which will be discussed below.

# ResourceHandler

## ResourceBag

A ResourceBag is wrapper for all resourced involved in this request processing, it is the actual one which is shipped between resourceHandler and loaded with response data or any temporary data.

## ResourceHandler Implementation

A ResourceHandler is an observable(rxjava), it’s only handling logic for its resource, such as databased interacting and logic processing and data encapsulation. One demonstrated code snippet for resource handler are listed following:

```java
@Component
public class UserResourceHandler implements AbstractResourceHandler {

    @Autowired
    DispatcherController dispatcherController;

    @Override
    public Observable<ResourceBag> handle(ResourceBag resourceBag) {

        return Observable.<ResourceBag>create((subscriber -> {

            //Before
            //Initialize and fill this resource content by its resource_id
            String user_id = Optional.ofNullable(resourceBag.getUserId()).orElseGet(() -> {
                return String.valueOf(resourceBag.getUriMapping().get(resourceBag.getFlowIndex())[1]);
            });

            Resource userResource = new Resource();

            userResource.attributes.put("user_id", user_id);

//            System.out.println(resourceBag.getFlowIndex() + "AAAAAA" + resourceBag.getUriMapping().size());

            //Insert this resource into resource bag
            resourceBag.getResources().add(userResource);

            //chech whether it is terminal
            //After

            if (resourceBag.getUriMapping().size() > resourceBag.getFlowIndex() + 1) {
                dispatcherController.forward(resourceBag).subscribe(r -> {
                    resourceBag.decreaseFlowIndex();
                    subscriber.onNext(r);
                    subscriber.onCompleted();
                });
            } else {
                //if terminal, use onNext to backward
                resourceBag.decreaseFlowIndex();
                subscriber.onNext(resourceBag);
                subscriber.onCompleted();
            }


        }));

    }

}
```

# Asynchronous Flow

How the Resource Flow between Resource Handler

![](http://7xlgth.com1.z0.glb.clouddn.com/56A0BE00-E44F-4E08-AA96-3DB9CD0E561C.png)

![](http://7xlgth.com1.z0.glb.clouddn.com/E1C10411-FCDC-400D-81FA-3885065E1B70.png)

In my practice ， if front-end want to get books commented by one user. In the restful type, it can be written as :

http://api.com/user/{user_token}/comment?requestData={}

http://api.com/book?requestData={book_ids}

and in AARF, it is:

http://api.com/user/{user_token}/comment/all/books?requestData={}
