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
