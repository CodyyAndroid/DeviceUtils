# DeviceUtils[![](https://jitpack.io/v/CodyyAndroid/DeviceUtils.svg)](https://jitpack.io/#CodyyAndroid/DeviceUtils)
---

```
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
---
```
dependencies {
	        implementation 'com.github.CodyyAndroid:DeviceUtils:0.0.2'
	}
```
---


| 名称 | API | 备注 | 示例 |
| ------ | ------ | ------ | ------ |
| 品牌 | brand | 获取设备品牌 |  HUAWEI |
| 型号 | model | 获取手机型号 | HUAWEINXT-TL00 |
| CPU核数 | getCpuProcessors | 获取CPU核数 | 8 |
| CPU位数 | getCpuArchitectureType | 获取CPU位数 | 64-Bit |
| CPU指令集 | abis | 获取CPU指令集 | arm64-v8a |
| 总内存 | getTotalMemory | 获取总内存 | 2.71GB |
| 可用内存 | getAvailMemory | 获取可用内存 | 1.12GB |
| 系统版本 | sdkVersionName | 获取系统版本 | 8.0.0 |
| 系统版本值 | sdkVersionCode | 获取系统版本值 | 26 |
| 手机分辨率-宽 | getScreenWidth | 获取手机分辨率-宽 | 1080 |
| 手机分辨率-高 | getScreenHeight | 获取手机分辨率-高,包含NaviBar | 1920 |
| 运营商 | getOperators | 获取运营商名称,建议后台获取 | 中国联通 |

---
### example
![](https://github.com/CodyyAndroid/DeviceUtils/blob/master/main.png)