# 班级通讯录管理系统开发文档

## 一、项目简介
班级通讯录管理系统是一个基于 **Java** 编写的命令行应用，面向班级层级的学生信息管理。核心目标是在控制台提供班级与学生的查询、增删改、排序、持久化存储以及全局搜索能力。系统主要技术栈为标准 Java 类库，不依赖第三方框架，运行方式为直接执行 `com.addressbook.app.StudentConsoleApp` 的 `main` 方法。

## 二、功能总览
- **班级管理**：新增班级、按编号或名称查询班级、修改班级名称/班主任/专业、查看全部班级列表。【F:src/com/addressbook/service/ClassManager.java†L17-L71】【F:src/com/addressbook/app/StudentConsoleApp.java†L33-L121】
- **学生管理**：在指定班级内添加、删除、更新学生信息；按学号或姓名搜索学生；输出学生详情。【F:src/com/addressbook/service/Classroom.java†L41-L104】【F:src/com/addressbook/app/StudentConsoleApp.java†L83-L193】
- **排序功能**：基于策略模式按学号或姓名对班级学生列表排序。【F:src/com/addressbook/service/Classroom.java†L86-L101】【F:src/com/addressbook/strategy/StudentIdSortStrategy.java†L1-L12】【F:src/com/addressbook/strategy/StudentNameSortStrategy.java†L1-L12】
- **持久化功能**：使用文本文件存储班级列表和各班学生数据，支持启动初始化、备份、容错读取。【F:src/com/addressbook/storage/TxtFileStorage.java†L1-L170】【F:src/com/addressbook/data/DataStore.java†L7-L30】
- **全局搜索功能**：跨班级匹配班级与学生的多字段模糊搜索，返回命中描述。【F:src/com/addressbook/service/BasicGlobalSearchService.java†L1-L71】【F:src/com/addressbook/app/StudentConsoleApp.java†L146-L173】
- **菜单式交互**：循环展示功能菜单，读取用户输入并调用对应操作。【F:src/com/addressbook/app/StudentConsoleApp.java†L25-L66】

## 三、代码结构说明
```
src/
├─com/addressbook/app/            # 主程序入口与控制台交互
├─com/addressbook/data/           # 数据初始化与仓储单例
├─com/addressbook/entity/         # 实体与抽象父类
├─com/addressbook/interfaces/     # 领域接口与策略接口
├─com/addressbook/service/        # 业务服务、领域模型与搜索
├─com/addressbook/strategy/       # 具体排序策略
└─com/addressbook/storage/        # 持久化接口与TXT实现
```
- `app/StudentConsoleApp`：负责菜单展示、输入读取、调用业务服务。【F:src/com/addressbook/app/StudentConsoleApp.java†L1-L175】
- `data/DataStore`：系统的单例入口，组合 `ClassManager` 与 `TxtFileStorage`，完成启动加载或默认数据初始化。【F:src/com/addressbook/data/DataStore.java†L7-L31】
- `data/DataInitializer`：提供默认班级与学生样例数据，便于初次运行。【F:src/com/addressbook/data/DataInitializer.java†L11-L38】
- `entity/Person` 与 `entity/Student`：抽象人员与学生实体，定义基础字段与信息输出。【F:src/com/addressbook/entity/Person.java†L1-L35】【F:src/com/addressbook/entity/Student.java†L1-L48】
- `interfaces`：`ClassManagement`、`StudentOperations` 定义班级与学生操作契约；`SortStrategy` 约束排序策略。【F:src/com/addressbook/interfaces/ClassManagement.java†L1-L12】【F:src/com/addressbook/interfaces/StudentOperations.java†L1-L18】【F:src/com/addressbook/interfaces/SortStrategy.java†L1-L7】
- `service/ClassManager` 与 `service/Classroom`：班级聚合根与学生集合操作，封装持久化调用。【F:src/com/addressbook/service/ClassManager.java†L1-L68】【F:src/com/addressbook/service/Classroom.java†L1-L116】
- `service/BasicGlobalSearchService`、`GlobalSearchResult`、`SearchMatch`、`GlobalSearchService`：全局搜索实现与返回结构。【F:src/com/addressbook/service/BasicGlobalSearchService.java†L1-L71】【F:src/com/addressbook/service/GlobalSearchResult.java†L1-L19】【F:src/com/addressbook/service/SearchMatch.java†L1-L21】【F:src/com/addressbook/service/GlobalSearchService.java†L1-L5】
- `strategy`：按学号/姓名排序的具体策略类。【F:src/com/addressbook/strategy/StudentIdSortStrategy.java†L1-L12】【F:src/com/addressbook/strategy/StudentNameSortStrategy.java†L1-L12】
- `storage`：持久化接口与文本文件实现，包含序列化/反序列化与备份逻辑。【F:src/com/addressbook/storage/DataStorage.java†L1-L13】【F:src/com/addressbook/storage/TxtFileStorage.java†L1-L170】

## 四、类设计说明
- **Person (abstract)**：持有 `id`、`name`、`phone`、`email`，声明抽象方法 `showInfo()` 供子类实现。【F:src/com/addressbook/entity/Person.java†L1-L35】
- **Student extends Person**：新增 `qq`、`dormAddress`、`gender`、`age`，实现 `showInfo` 打印完整信息，提供标准 getter/setter 与 `toString`。【F:src/com/addressbook/entity/Student.java†L1-L48】
- **Classroom implements StudentOperations**：作为班级领域模型，维护班级基础信息与 `students` 列表，实现新增、删除、更新、按学号/姓名查询、排序、替换学生集合等操作；通过策略应用排序。【F:src/com/addressbook/service/Classroom.java†L11-L116】
- **ClassManager implements ClassManagement**：维护全局班级列表并组合 `DataStorage`，提供班级查询、新增、更新，同时代理学生的增删改并触发持久化。【F:src/com/addressbook/service/ClassManager.java†L9-L65】
- **BasicGlobalSearchService implements GlobalSearchService**：遍历所有班级和学生，基于字段 Map 进行大小写不敏感包含匹配，生成 `GlobalSearchResult`。【F:src/com/addressbook/service/BasicGlobalSearchService.java†L10-L67】
- **GlobalSearchResult**：封装搜索结果，区分班级与学生命中列表，并提供 `isEmpty` 判断。【F:src/com/addressbook/service/GlobalSearchResult.java†L6-L19】
- **SearchMatch**：表示单条命中记录，包含类型、班级名、描述文本。【F:src/com/addressbook/service/SearchMatch.java†L3-L20】
- **StudentIdSortStrategy / StudentNameSortStrategy implements SortStrategy**：使用 `Comparator` 对学生列表按学号或姓名排序。【F:src/com/addressbook/strategy/StudentIdSortStrategy.java†L1-L12】【F:src/com/addressbook/strategy/StudentNameSortStrategy.java†L1-L12】
- **TxtFileStorage implements DataStorage, DataSerializer, DataDeserializer**：负责文本文件的序列化存取，包含备份恢复机制及容错输出。【F:src/com/addressbook/storage/TxtFileStorage.java†L1-L170】
- **DataStore (singleton helper)**：持有全局 `ClassManager` 和 `DataStorage`，应用启动时加载已有数据或使用 `DataInitializer` 默认数据填充，随后写入文件。【F:src/com/addressbook/data/DataStore.java†L7-L31】
- **StudentConsoleApp**：主入口，持有 `ClassManager` 与 `GlobalSearchService`，循环读取用户指令执行具体操作。【F:src/com/addressbook/app/StudentConsoleApp.java†L1-L175】

### 关系概览（文本 UML）
```
Person (abstract)
  ↑
Student

Classroom --implements--> StudentOperations
Classroom --uses--> SortStrategy (StudentIdSortStrategy | StudentNameSortStrategy)
ClassManager --implements--> ClassManagement
ClassManager --has--> List<Classroom> ; --uses--> DataStorage
DataStore --has--> ClassManager, DataStorage ; --uses--> DataInitializer
BasicGlobalSearchService --implements--> GlobalSearchService ; --uses--> ClassManager -> Classroom -> Student
StudentConsoleApp --uses--> ClassManager, GlobalSearchService
TxtFileStorage --implements--> DataStorage, DataSerializer, DataDeserializer
```

## 五、接口与抽象设计
- **ClassManagement**：抽象班级管理契约（新增、按名查找、获取全部），由 `ClassManager` 实现。【F:src/com/addressbook/interfaces/ClassManagement.java†L1-L12】【F:src/com/addressbook/service/ClassManager.java†L9-L65】
- **StudentOperations**：定义班级内学生的增删改查、排序与获取列表接口，由 `Classroom` 实现，方便替换或扩展其他学生集合实现。【F:src/com/addressbook/interfaces/StudentOperations.java†L1-L18】【F:src/com/addressbook/service/Classroom.java†L41-L106】
- **SortStrategy**：排序策略接口，具体实现以策略模式对学生集合排序，便于扩展新的排序方式（如年龄、宿舍等）。【F:src/com/addressbook/interfaces/SortStrategy.java†L1-L7】【F:src/com/addressbook/service/Classroom.java†L86-L101】
- **GlobalSearchService**：抽象全局搜索接口，当前由 `BasicGlobalSearchService` 实现，可按需替换为更复杂的搜索算法。【F:src/com/addressbook/service/GlobalSearchService.java†L1-L5】【F:src/com/addressbook/service/BasicGlobalSearchService.java†L10-L67】
- **DataStorage / DataSerializer / DataDeserializer**：分离持久化与序列化契约，`TxtFileStorage` 统一实现，未来可新增数据库或 JSON 存储实现替换。【F:src/com/addressbook/storage/DataStorage.java†L1-L13】【F:src/com/addressbook/storage/TxtFileStorage.java†L1-L170】
- **Person (abstract)**：抽象公共人员属性，要求子类提供信息展示实现。【F:src/com/addressbook/entity/Person.java†L1-L35】

## 六、持久化模块设计
- **文件结构**：
  - `class_list.txt`：保存班级信息，每行 `classId|className|major|headTeacher`。【F:src/com/addressbook/storage/TxtFileStorage.java†L21-L66】
  - `student_data_<classId>.txt`：按班级保存学生列表，每行 `id|name|phone|qq|email|dorm|gender|age`。【F:src/com/addressbook/storage/TxtFileStorage.java†L26-L66】
- **序列化逻辑**：`serializeClass` 和 `serializeStudent` 以管道符分隔字段，调用 `writeWithBackup` 写入。【F:src/com/addressbook/storage/TxtFileStorage.java†L63-L90】
- **反序列化逻辑**：`deserializeClass` 与 `deserializeStudent` 根据 `|` 切分，校验必填字段，异常或缺失时打印错误并跳过记录。【F:src/com/addressbook/storage/TxtFileStorage.java†L92-L139】
- **数据加载流程**：`DataStore` 启动时读取 `class_list.txt`；若为空则用 `DataInitializer` 样例数据填充并立即保存；否则按班级文件加载学生集合。【F:src/com/addressbook/data/DataStore.java†L12-L30】
- **数据保存流程**：班级或学生变更后由 `ClassManager` 调用 `DataStorage.saveAllClasses` / `saveStudents` 写入对应文件。【F:src/com/addressbook/service/ClassManager.java†L17-L65】
- **容错与一致性**：
  - 写入前如存在旧文件会创建时间戳备份，写入失败时尝试恢复备份以避免数据丢失。【F:src/com/addressbook/storage/TxtFileStorage.java†L141-L165】
  - 反序列化时对字段数量和必填项进行校验，遇到错误行会记录错误并跳过，避免污染内存数据。【F:src/com/addressbook/storage/TxtFileStorage.java†L104-L139】
  - `ClassManager` 在修改成功后立即触发对应文件保存，确保内存与磁盘一致。【F:src/com/addressbook/service/ClassManager.java†L17-L65】

## 七、全局搜索模块设计
- **搜索范围**：遍历所有班级与班级下的全部学生。【F:src/com/addressbook/service/BasicGlobalSearchService.java†L23-L49】
- **匹配字段**：
  - 班级：编号、名称、专业、班主任。【F:src/com/addressbook/service/BasicGlobalSearchService.java†L30-L36】
  - 学生：学号、姓名、手机号、QQ、邮箱、宿舍、性别、年龄。【F:src/com/addressbook/service/BasicGlobalSearchService.java†L38-L45】
- **匹配算法**：将关键字转为小写，对各字段值执行包含判断（忽略大小写），命中字段以 `字段名(值)` 形式收集。【F:src/com/addressbook/service/BasicGlobalSearchService.java†L17-L70】
- **输出结构**：`GlobalSearchResult` 区分班级与学生命中列表；每条命中为 `SearchMatch`，包含类型（CLASS/STUDENT）、班级名、描述信息。控制台逐条打印描述。【F:src/com/addressbook/service/GlobalSearchResult.java†L6-L19】【F:src/com/addressbook/service/SearchMatch.java†L3-L20】【F:src/com/addressbook/app/StudentConsoleApp.java†L146-L173】

## 八、程序主流程
1. **启动**：`StudentConsoleApp.main` 通过 `DataStore.getClassManager()` 获取已加载的班级管理器（含持久化初始化）。【F:src/com/addressbook/app/StudentConsoleApp.java†L21-L28】【F:src/com/addressbook/data/DataStore.java†L12-L30】
2. **菜单循环**：`run` 方法打印 12 项功能菜单，读取用户数字选择并分派到对应私有方法；输入异常会提示重试。【F:src/com/addressbook/app/StudentConsoleApp.java†L25-L80】【F:src/com/addressbook/app/StudentConsoleApp.java†L156-L175】
3. **班级/学生操作**：各菜单方法调用 `ClassManager` 或 `Classroom` 完成功能并在变更后触发持久化，信息输出通过 `Student.showInfo` 或 `toString`。【F:src/com/addressbook/app/StudentConsoleApp.java†L69-L145】【F:src/com/addressbook/service/ClassManager.java†L17-L65】【F:src/com/addressbook/entity/Student.java†L31-L48】
4. **全局搜索**：读取关键字后调用 `BasicGlobalSearchService.search`，按结果描述逐条输出。【F:src/com/addressbook/app/StudentConsoleApp.java†L146-L173】【F:src/com/addressbook/service/BasicGlobalSearchService.java†L10-L67】
5. **退出**：选择 12 结束循环并输出结束语。【F:src/com/addressbook/app/StudentConsoleApp.java†L29-L50】

## 九、可扩展性分析
- **排序策略扩展**：通过新增实现 `SortStrategy` 的类并在 `Classroom` 中调用，可轻松添加按年龄、宿舍等排序。【F:src/com/addressbook/interfaces/SortStrategy.java†L1-L7】【F:src/com/addressbook/service/Classroom.java†L86-L101】
- **持久化方式扩展**：`DataStorage`/`DataSerializer`/`DataDeserializer` 接口允许实现数据库、JSON、CSV 等存储；在 `DataStore` 中替换实现即可应用。【F:src/com/addressbook/storage/DataStorage.java†L1-L13】【F:src/com/addressbook/storage/TxtFileStorage.java†L1-L170】【F:src/com/addressbook/data/DataStore.java†L8-L15】
- **搜索策略升级**：`GlobalSearchService` 接口便于接入更复杂的分词、权重或索引方案，替换 `BasicGlobalSearchService` 即可。【F:src/com/addressbook/service/GlobalSearchService.java†L1-L5】【F:src/com/addressbook/service/BasicGlobalSearchService.java†L10-L67】
- **学生字段扩展**：`Student` 继承 `Person`，新增字段只需修改实体与序列化逻辑（`TxtFileStorage`），并在输入交互中添加相应提示。【F:src/com/addressbook/entity/Student.java†L1-L48】【F:src/com/addressbook/storage/TxtFileStorage.java†L55-L136】
- **班级属性扩展**：`Classroom` 支持更新名称、班主任、专业，可扩展新属性并同步调整序列化/显示即可。【F:src/com/addressbook/service/Classroom.java†L17-L52】【F:src/com/addressbook/storage/TxtFileStorage.java†L63-L117】
- **菜单扩展**：`StudentConsoleApp.printMenu` 与 `run` 的分发结构使新增功能入口直观，只需添加菜单项与对应方法。【F:src/com/addressbook/app/StudentConsoleApp.java†L33-L80】

## 十、总结
本项目以简洁的分层结构实现班级与学生通讯录管理，核心特点包括：
- 清晰的接口抽象（管理、操作、排序、搜索、存储）与面向接口编程，便于替换实现。
- 采用策略模式实现排序，持久化与序列化接口分离，支持未来扩展。
- 文本文件存储并带备份与容错逻辑，保证数据基本可靠性。
- 命令行菜单交互简单直观，默认数据初始化方便快速体验。

改进方向：
- 增加输入校验与重复性检查（例如学生学号唯一性）以提升数据质量。
- 将 I/O 异常与提示统一为日志或用户友好输出，增强可维护性。
- 引入单元测试覆盖核心服务逻辑，保障扩展与重构的安全。
- 支持更丰富的搜索与排序维度，以及分页、导出等实用功能。
