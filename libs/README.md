# libs/ — 编译期依赖（第三方，未随本仓库分发）

本目录**不包含**任何第三方 jar。要编译本模组，请自行把下列 jar 放进本目录（文件名需与下面一致）：

| 文件 | 说明 | 来源 |
| --- | --- | --- |
| `create.jar` | 机械动力 Create | https://www.curseforge.com/minecraft/mc-mods/create |
| `ponder.jar` | Create 的 Ponder 库 | 随 Create 分发的内嵌库 |
| `flywheel.jar` | Flywheel 渲染库 | https://www.curseforge.com/minecraft/mc-mods/flywheel |
| `registrate.jar` | Registrate 注册库 | 随 Create 分发的内嵌库 |
| `aeronautics.jar` | 航空学（Create 附属，含 Sable 物理） | 航空学模组分发包 |

> 这些 jar 仅用于**编译**（`compileOnly`），不会被打进本模组的 jar。
> 它们各自归其作者所有，请遵守各自的授权；本仓库不再分发它们。

`build.gradle` 里通过 `flatDir { dirs 'libs' }` 引用这些文件。
