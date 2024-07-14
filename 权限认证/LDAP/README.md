# LDAP

轻量级目录访问协议（LDAP）是一种可帮助用户查找关于企业和人员等数据的协议。LDAP 有两大工作目标：一、将数据存储在 LDAP 目录中；二、对要访问目录的用户进行身份验证。它也为应用提供所需的通信语言，以便从目录服务收发信息。借助目录服务，用户可以访问网络中的企业、个人信息和其他数据的存储位置。

LDAP 最常见的用法是提供一个集中位置来访问和管理目录服务。LDAP 让企业能够存储、管理和保护与该企业、其用户和资产有关的信息（比如用户名和密码）。它提供分层的信息结构，可以让用户快速、高效地访问存储内容，当企业在扩大规模、吸纳了更多用户数据和资产时，这是一个相当重要的优势。

LDAP 也可充当身份和访问管理（IAM）解决方案来满足用户身份验证的需要，支持 Kerberos 和单点登录（SSO）、简单身份验证安全层（SASL）和安全套接字层（SSL）。

## LDAP 与 Active Directory

LDAP 是[微软](https://www.redhat.com/zh/partners/microsoft)的 Active Directory（AD）目录服务中使用的核心协议（但并非是其专用）；而 Active Directory 是一个大型目录服务数据库，包含网络中每一用户帐户的信息。更具体地说，LDAP 是目录访问协议（DAP）的轻量级版本，提供一个中央位置来访问和管理在传输控制协议/互联网协议（TCP/IP）上运行的目录服务，其最新版本是 LDAPv3。

AD 提供身份验证以及用户和组管理，它是最终对用户或计算机执行身份验证的主体。这个数据库中包含的属性数量要比 LDAP 调用的数量多。不过，LDAP 擅长于使用少量信息来查找目录，因此不需要从 AD 提取它的全部属性，而且从哪一个目录服务调取也无关紧要。

LDAP 的主要目标是与 AD 通信，从中提取对象（如域、用户和组等），并以可用格式存储到位于 LDAP 服务器上的专用目录中。

我们可以这样比喻：AD 是世界上最庞大的图书馆，而您想要找一本书名提到丧尸的书。这本书是否在美国出版、是否超过 1000 页，或是否为丧尸末日逃生指南，对于 LDAP 而言都无关紧要——尽管它们确实有助于缩小目标的范围。LDAP 是经验丰富的图书管理员，它知道哪里可以找到满足您要求的所有选项，并且会验证您是否已找到要找的目标。

## LDAP 身份验证流程

LDAP 检索是如何发起的？它是如何工作的？

LDAP 身份验证使用的是一种客户端加服务器型身份验证模式，包含以下几个重要组成部分：

- **目录系统代理（DSA）：**作为服务器，在其网络中运行 LDAP
- **目录用户代理（DUA）：**作为客户端（例如，用户的个人电脑），访问 DSA
- **DN：**即专有名称（Distinguished Name），包含了供 LDAP 检索的目录信息树（DIT）的路径（例如，cn=Susan、ou=users、o=Company）
- **相对专有名称（RDN）：**专有名称路径中的各个组成部分（例如，cn=Susan）
- [**应用编程接口**](https://www.redhat.com/zh/topics/api/what-are-application-programming-interfaces)**（API）：**通过 API，您无需了解实施原理，也能将您的产品或服务与其他其他产品或服务互通。

当用户尝试访问支持 LDAP 的客户端程序，例如其个人电脑上的商务电子邮件应用，整个 LDAP 流程就开始了。使用 LDAPv3 时，有两种用户身份验证方法：简单身份验证（例如，使用登录凭据的 SSO）和 SASL 身份验证（将 LDAP 服务器绑定到 Kerberos 等程序）。用户尝试登录时，系统会发送一个请求来鉴定分配给用户的 DN。DN 是通过启动 DSA 的客户端 API 或服务器发送的。

客户端会自动绑定到 DSA，LDAP 使用 DN 来搜索与 LDAP 数据库中记录相匹配的对象或对象集合。DN 中的 RDN 在这个阶段非常重要，因为它们会一步一步引导 LDAP 搜索 DIT，以找到所需个体。如果路径在后端缺少连接的 RDN，结果可能会呈现为无效。在本例中，LDAP 搜索的对象是个人用户帐户（cn=Susan），它只有在目录中的帐户有匹配的 uid 和 userPassword 时才会验证用户。用户组在 LDAP 目录中也被看做是对象。

一旦用户收到回复（无论有效或无效），客户端会从 LDAP 服务器解绑。然后，通过验证的用户可以访问 API 及其服务，包括所需的文件、用户信息和其他应用数据，具体取决于系统管理员授予的权限。

## 了解 LDAP 组件

LDAP 采用轻量型结构并且使用了 DIT，因而能快速运行 LDAP 搜索并提供搜索结果。想要顺利浏览 LDAP 服务器并理解 LDAP 搜索的工作原理，了解 DIT 至关重要。

DIT 能够让用户快速浏览不同层级的 LDAP 目录，以缩小搜索结果范围并提供对查询的回复。DIT 以根目录开头，后跟国家或地区，然后划分为两个子类：域名（dc）和组织名称（o）。

**域名（dc）**

dc（例如，dc=com、dc=example）使用域名系统（DNS）映射来查找互联网域名并将其转译为 IP 地址。

大多数用户不知道他们搜索的个体的域名和/或 IP 地址。这时，LDAP 使用分配给用户的专有名称（DN）作为路径，以快速浏览 DIT 并找到搜索结果。接下来就要使用 o 子类了。

**组织名称（o）**

o 子类（例如，o-Company）是 DN 中列出的最常用子类之一，通常也是 LDAP 运行搜索时的起始位置。例如，一个简单路径通常以 o 子类开头，分叉到组织单元/部门（ou），后跟用户帐户或组。

**组织单元（ou）**

如前文所述，ou 是 o 的一个子类，通常表现为 ou=users 或 ou=group，分别包含用户帐户或组的列表。其在目录中可能如下所示：

- o-Company
  - ou=groups
    - cn=developers
  - ou=users
    - cn=Susan

**通用名称（cn）**

通用名称或 cn 用于标识组或个人用户帐户的名称（如 cn=developers、cn=Susan）。用户可以从属于某个组；因此，如果 Susan 是开发人员，则也可能会存在于 cn=developers 下。

**属性和值**

LDAP DIT 中的各个子类（即 o、ou、cn）包含属性或值，或含有 LDAP 目录相关信息的模式，它们有助于缩小搜索范围。属性有点像地址簿中的条目，有着名称、电话号码和地址等标签，每个属性分配有相应的值。例如，Susan 可能是 name 属性的值。

在 cn=Susan 帐户中，user id（uid）和 userPassword 是属性，而用户的登录凭据则是值。不过，在诸如 cn=developers 之类的组中，Susan 可能有 uniqueMember 属性（例如，uniqueMember=cn-Susan,ou-Users,o-Company）。这会将路径映射到 Susan 的个人用户帐户所处的位置，以及 LDAP 所搜索的信息。用户帐户是 DIT 中的末端，也是 LDAP 最终提取搜索结果的地方。

还有许多其他属性类型和语法可以帮助缩小搜索范围，包括 organizationalPerson（structural）或 personal（structural）等 ObjectClass。不过，为了保持轻量和易用，LDAP 中的属性数量是有限的。

## 为什么要选择 LDAP？

企业网络管理员通常要同时管理成千上万的用户。这意味着，他们要根据用户的角色以及对日常任务所需文件（例如，访问公司内网）的访问权限来分配访问控制和策略。

LDAP 可以简化用户管理过程，节省网络管理员的宝贵时间，并使身份验证过程集中到同一个地方。在您采用 LDAP 之前，务必要思考以下几个问题：

- **容量：**您需要存储的用户管理数据有多少？要考虑实施 LDAP 解决方案的产品是否有足够容量来存储和管理您需要的所有数据。
- **搜索频率：**是不是有用户每天都需要访问的数据，如公司内网、电子邮件应用或服务？如果有，那么 LDAP 应该就很适合您。
- **条理性：**LDAP 中的简单 DIT 能否为您的数据提供足够的整理功能，还是说您需要更加细致周到的系统？

虽然 LDAP 通常在 AD 中使用，但也可用于为其他工具和客户端环境提供用户身份验证，例如 Unix 上的红帽目录服务器以及 OpenLDAP（Windows 上的一款开源应用）。您还可以将 LDAP 的身份验证和用户管理功能用于 [API 管理](https://www.redhat.com/zh/topics/api/what-is-api-management)、[基于角色的访问控制](https://www.redhat.com/zh/topics/containers/what-kubernetes-role-based-access-control-rbac)（RBAC），或者 [Docker ](https://www.redhat.com/zh/topics/containers/what-is-docker)和 [Kubernetes](https://www.redhat.com/zh/topics/containers/what-is-kubernetes) 等其他应用和服务。
