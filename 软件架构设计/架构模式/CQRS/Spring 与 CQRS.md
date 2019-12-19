# Spring 中实现 CQRS

目的是将服务层和控制器层明确分开，以分别处理进入系统的读取–查询和写入–命令。

# Service 层

首先，我们将原本的 UserService 拆分为 UserQueryService 与 UserCommandService：

```java
public interface IUserQueryService {

    List<User> getUsersList(int page, int size, String sortDir, String sort);

    String checkPasswordResetToken(long userId, String token);

    String checkConfirmRegistrationToken(String token);

    long countAllUsers();

}

public interface IUserCommandService {

    void registerNewUser(String username, String email, String password, String appUrl);

    void updateUserPassword(User user, String password, String oldPassword);

    void changeUserPassword(User user, String password);

    void resetPassword(String email, String appUrl);

    void createVerificationTokenForUser(User user, String token);

    void updateUser(User user);

}
```

# Controller 层

## Query Controller

```java
@Controller
@RequestMapping(value = "/api/users")
public class UserQueryRestController {

    @Autowired
    private IUserQueryService userService;

    @Autowired
    private IScheduledPostQueryService scheduledPostService;

    @Autowired
    private ModelMapper modelMapper;

    @PreAuthorize("hasRole('USER_READ_PRIVILEGE')")
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public List<UserQueryDto> getUsersList(...) {
        PagingInfo pagingInfo = new PagingInfo(page, size, userService.countAllUsers());
        response.addHeader("PAGING_INFO", pagingInfo.toString());

        List<User> users = userService.getUsersList(page, size, sortDir, sort);
        return users.stream().map(
          user -> convertUserEntityToDto(user)).collect(Collectors.toList());
    }

    private UserQueryDto convertUserEntityToDto(User user) {
        UserQueryDto dto = modelMapper.map(user, UserQueryDto.class);
        dto.setScheduledPostsCount(scheduledPostService.countScheduledPostsByUser(user));
        return dto;
    }
}
```

这里有趣的是查询控制器仅注入查询服务，更加有趣的是，通过将这些控制器放在单独的模块中，可以切断该控制器对命令服务的访问。

## Command Controller

```java
@Controller
@RequestMapping(value = "/api/users")
public class UserCommandRestController {

    @Autowired
    private IUserCommandService userService;

    @Autowired
    private ModelMapper modelMapper;

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void register(
      HttpServletRequest request, @RequestBody UserRegisterCommandDto userDto) {
        String appUrl = request.getRequestURL().toString().replace(request.getRequestURI(), "");

        userService.registerNewUser(
          userDto.getUsername(), userDto.getEmail(), userDto.getPassword(), appUrl);
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/password", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void updateUserPassword(@RequestBody UserUpdatePasswordCommandDto userDto) {
        userService.updateUserPassword(
          getCurrentUser(), userDto.getPassword(), userDto.getOldPassword());
    }

    @RequestMapping(value = "/passwordReset", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void createAResetPassword(
      HttpServletRequest request,
      @RequestBody UserTriggerResetPasswordCommandDto userDto)
    {
        String appUrl = request.getRequestURL().toString().replace(request.getRequestURI(), "");
        userService.resetPassword(userDto.getEmail(), appUrl);
    }

    @RequestMapping(value = "/password", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void changeUserPassword(@RequestBody UserchangePasswordCommandDto userDto) {
        userService.changeUserPassword(getCurrentUser(), userDto.getPassword());
    }

    @PreAuthorize("hasRole('USER_WRITE_PRIVILEGE')")
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void updateUser(@RequestBody UserUpdateCommandDto userDto) {
        userService.updateUser(convertToEntity(userDto));
    }

    private User convertToEntity(UserUpdateCommandDto userDto) {
        return modelMapper.map(userDto, User.class);
    }
}
```

# 对象

将命令和查询分离之后，现在让我们快速浏览一下 User 资源的不同表示形式：

```java
public class UserQueryDto {
    private Long id;

    private String username;

    private boolean enabled;

    private Set<Role> roles;

    private long scheduledPostsCount;
}

public class UserRegisterCommandDto {
    private String username;
    private String email;
    private String password;
}

public class UserUpdatePasswordCommandDto {
    private String oldPassword;
    private String password;
}

public class UserTriggerResetPasswordCommandDto {
    private String email;
}

public class UserChangePasswordCommandDto {
    private String password;
}

public class UserUpdateCommandDto {
    private Long id;

    private boolean enabled;

    private Set<Role> roles;
}
```
