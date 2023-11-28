# Stop using JSON Web tokens for user sessions

## Introduction

Modern web applications (so called single page applications) often utilize JSON Web tokens (JWTs) for session handling instead of cookies. However, in many cases, the logout mechanism is not effectively implemented. The most critical issue arises when an XSS vulnerability is discovered.

> Why should we even _logout_ from a web application?

For security reasons, it is advisable for users to log out from a web application once they have completed their tasks. This practice is particularly crucial for sensitive applications, such as online banking, as it reduces the attack surface and makes it significantly harder for an attacker to hijack another user’s session.

Frequently, when a Logout function is present in the application and is implemented with JSON Web Tokens, the application stores the JWT in an insecure location, such as the JavaScript code itself or the local storage in the user’s browser. This approach is insecure because essential flags like `HTTPOnly`[1](https://ds-security.com/post/stop-using-jwts-for-sessions/#fn:1) are not supported. Consequently, this allows an attacker to use JavaScript to access tokens. Moreover, the logout function often merely overwrites the JWT on the client side, leaving user sessions valid until timeout.

## The worst part

Once an attacker discovers a Cross-Site-Scripting (XSS) vulnerability within the application, it’s “GAME OVER.” The attacker can gain access to the JWT and exploit it to access the application in the context of another user.

The “attack chain” is depicted in the following diagram and operates as follows:

1. The attacker exploits a stored XSS vulnerability within the web application (e.g., in a guest comment).
2. An admin logs into the web application.
3. The web server generates the JWT and sends it to the user. The browser stores the JWT and uses it to access the web server resources with admin permissions.
4. The admin navigates to the manipulated page `/stored_XSS` that contains the attacker’s injected JavaScript code.
5. The attacker’s JavaScript code is sent to the admin.
6. The malicious JavaScript code executes in the admin’s browser. The attacker’s JavaScript code steals the admin’s JWT and sends it to the attacker.
7. The attacker can access the web application using the admin’s JWT and perform privileged admin functions.

![Attack path exploiting XSS with JWTs.svg](https://ds-security.com/files/XSS_with_JWTs.svg)

We have seen this vulnerability numeros times in security assessments. In my opinion single-page application have a serious design issue when they use JWTs for authentification. This attack would not be possible with a cookie based session mechanisn where attributes like `HttpOnly` would prevents injected JavaScript Code from accessing the JWTs.
