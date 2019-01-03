package com.codyy.myapplication

import android.Manifest
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.codyy.devicelibrary.DeviceUtils
import com.codyy.rx.permissions.RxPermissions
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        RxPermissions(supportFragmentManager).request(Manifest.permission.READ_PHONE_STATE,Manifest.permission.READ_EXTERNAL_STORAGE).subscribe { granted ->
            run {
                if (granted) {
                    tv_device.text = """
品牌:${DeviceUtils.brand}
型号:${DeviceUtils.model}
CPU核数:${DeviceUtils.getCpuProcessors()}
CPU位数:${DeviceUtils.getCpuArchitectureType()}
内存:共${DeviceUtils.getTotalMemory()},${DeviceUtils.getAvailMemory(this)}可用
cpu指令集:${DeviceUtils.abis}
系统版本:${DeviceUtils.sdkVersionName}
系统版本值:${DeviceUtils.sdkVersionCode}
分辨率:${DeviceUtils.getScreenWidth(this)}*${DeviceUtils.getScreenHeight(this)}
运营商:${DeviceUtils.getOperators(this)}

ID:${DeviceUtils.id}
DISPLAY:${DeviceUtils.display}
产品名:${DeviceUtils.product}
制造商:${DeviceUtils.manufacturer}
设备名:${DeviceUtils.device}
硬件:${DeviceUtils.hardware}
指纹:${DeviceUtils.fingerprint}
串口序列号:${DeviceUtils.serial}
设备版本类型:${DeviceUtils.type}
描述build的标签:${DeviceUtils.tags}
设备主机地址:${DeviceUtils.host}
设备用户名:${DeviceUtils.user}
固件开发版本代号:${DeviceUtils.codename}
源码控制版本号:${DeviceUtils.incremental}
主板:${DeviceUtils.board}
主板引导程序:${DeviceUtils.bootload}
Build时间:${DeviceUtils.time}
IMEI :${DeviceUtils.getImei(this)}
android_id :${DeviceUtils.getAndroidId(this)}

"""
//                    tv_device.text = """
//系统版本:${DeviceUtils.sdkVersionName}
//系统版本值:${DeviceUtils.sdkVersionCode}
//品牌:${DeviceUtils.brand}
//型号:${DeviceUtils.model}
//ID:${DeviceUtils.id}
//DISPLAY:${DeviceUtils.display}
//产品名:${DeviceUtils.product}
//制造商:${DeviceUtils.manufacturer}
//设备名:${DeviceUtils.device}
//硬件:${DeviceUtils.hardware}
//指纹:${DeviceUtils.fingerprint}
//串口序列号:${DeviceUtils.serial}
//设备版本类型:${DeviceUtils.type}
//描述build的标签:${DeviceUtils.tags}
//设备主机地址:${DeviceUtils.host}
//设备用户名:${DeviceUtils.user}
//固件开发版本代号:${DeviceUtils.codename}
//源码控制版本号:${DeviceUtils.incremental}
//主板:${DeviceUtils.board}
//主板引导程序:${DeviceUtils.bootload}
//Build时间:${DeviceUtils.time}
//cpu指令集:${DeviceUtils.abis}
//IMEI :${DeviceUtils.getImei(this)}
//android_id :${DeviceUtils.getAndroidId(this)}
//
//"""
                } else {
                    RxPermissions.showDialog(this, BuildConfig.APPLICATION_ID, "获取权限失败")
                }
            }
        }

    }
}
