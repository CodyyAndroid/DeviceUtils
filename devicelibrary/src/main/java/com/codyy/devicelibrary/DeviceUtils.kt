package com.codyy.devicelibrary

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Context.TELEPHONY_SERVICE
import android.content.pm.PackageManager
import android.graphics.Point
import android.os.Build
import android.provider.Settings
import android.support.v4.content.ContextCompat
import android.telephony.TelephonyManager
import android.text.TextUtils
import android.util.DisplayMetrics
import android.util.Log
import android.view.KeyCharacterMap
import android.view.KeyEvent
import android.view.ViewConfiguration
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.FileReader
import java.text.SimpleDateFormat
import java.util.*


/**
 * Android获取硬件设备信息
 */
class DeviceUtils {

    init {
        throw UnsupportedOperationException("u can't instantiate me...")
    }

    companion object {
        private const val LOGENABLE = false
        /**
         * The system libc.so file path
         */
        private const val SYSTEM_LIB_C_PATH = "/system/lib/libc.so"
        private const val SYSTEM_LIB_C_PATH_64 = "/system/lib64/libc.so"
        /**
         * ELF文件头 e_indent[]数组文件类标识索引
         */
        private const val EI_CLASS = 4
        /**
         * ELF文件头 e_indent[EI_CLASS]的取值：ELFCLASS32表示32位目标
         */
        private const val ELFCLASS32 = 1
        /**
         * ELF文件头 e_indent[EI_CLASS]的取值：ELFCLASS64表示64位目标
         */
        private const val ELFCLASS64 = 2
        /**
         * Return whether device is rooted.
         *
         * @return `true`: yes<br></br>`false`: no
         */
        @JvmStatic
        val isDeviceRooted: Boolean
            get() {
                val su = "su"
                val locations = arrayOf(
                    "/system/bin/",
                    "/system/xbin/",
                    "/sbin/",
                    "/system/sd/xbin/",
                    "/system/bin/failsafe/",
                    "/data/local/xbin/",
                    "/data/local/bin/",
                    "/data/local/"
                )
                for (location in locations) {
                    if (File(location + su).exists()) {
                        return true
                    }
                }
                return false
            }

        /**
         * 系统版本
         *
         * @return the version name of device's system
         */
        @JvmStatic
        val sdkVersionName: String
            get() = android.os.Build.VERSION.RELEASE

        /**
         * 系统的API级别
         *
         * @return version code of device's system
         */
        @JvmStatic
        val sdkVersionCode: Int
            get() = android.os.Build.VERSION.SDK_INT

        /**
         * 制造商
         *
         * e.g. Xiaomi
         *
         * @return the manufacturer of the product/hardware
         */
        @JvmStatic
        val manufacturer: String
            get() = Build.MANUFACTURER
        /**
         * 品牌
         */
        @JvmStatic
        val brand: String
            get() = Build.BRAND
        /**
         * ID
         */
        @JvmStatic
        val id: String
            get() = Build.ID
        /**
         * DISPLAY
         */
        @JvmStatic
        val display: String
            get() = Build.DISPLAY
        /**
         * 产品名
         */
        @JvmStatic
        val product: String
            get() = Build.PRODUCT
        /**
         * 设备名
         */
        @JvmStatic
        val device: String
            get() = Build.DEVICE
        /**
         * 硬件
         */
        @JvmStatic
        val hardware: String
            get() = Build.HARDWARE
        /**
         * 指纹
         */
        @JvmStatic
        val fingerprint: String
            get() = Build.FINGERPRINT
        /**
         * 串口序列号
         */
        @JvmStatic
        val serial: String
            get() = Build.SERIAL
        /**
         * 设备版本类型
         */
        @JvmStatic
        val type: String
            get() = Build.TYPE
        /**
         * 描述build的标签
         */
        @JvmStatic
        val tags: String
            get() = Build.TAGS
        /**
         * 设备主机地址
         */
        @JvmStatic
        val host: String
            get() = Build.HOST
        /**
         * 设备用户名
         */
        @JvmStatic
        val user: String
            get() = Build.USER
        /**
         * 固件开发版本代号
         */
        @JvmStatic
        val codename: String
            get() = Build.VERSION.CODENAME
        /**
         * 源码控制版本号
         */
        @JvmStatic
        val incremental: String
            get() = Build.VERSION.INCREMENTAL
        /**
         * 主板
         */
        @JvmStatic
        val board: String
            get() = Build.BOARD
        /**
         * 主板引导程序
         */
        @JvmStatic
        val bootload: String
            get() = Build.BOOTLOADER
        /**
         * Build时间
         */
        @JvmStatic
        val time: String
            get() = SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒", Locale.getDefault()).format(Build.TIME)

        /**
         * 型号
         *
         * e.g. MI2SC
         *
         * @return the model of device
         */
        @JvmStatic
        val model: String
            get() {
                var model: String? = Build.MODEL
                model = model?.trim { it <= ' ' }?.replace("\\s*".toRegex(), "") ?: ""
                return model
            }

        /**
         * Return an ordered list of ABIs supported by this device. The most preferred ABI is the first
         * element in the list.
         *
         * @return an ordered list of ABIs supported by this device
         */
        @JvmStatic
        val abis: String
            get() {
                var str: String? = ""
                val s = getAbis()
                for (v in s) {
                    str += v
                    str += " "
                }
                return str ?: ""
            }

        private fun getAbis(): Array<String> {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Build.SUPPORTED_ABIS
            } else {
                if (!TextUtils.isEmpty(Build.CPU_ABI2)) {
                    arrayOf(Build.CPU_ABI, Build.CPU_ABI2)
                } else arrayOf(Build.CPU_ABI)
            }
        }

        /**
         * Require Permission {@code <uses-permission android:name="android.permission.READ_PHONE_STATE" />}</p>
         */
        @JvmStatic
        fun getImei(activity: Activity): String {
            return if (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.READ_PHONE_STATE
                )
            ) (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                (activity.getSystemService(TELEPHONY_SERVICE) as TelephonyManager).imei
            } else (activity.getSystemService(TELEPHONY_SERVICE) as TelephonyManager).deviceId) else ""
        }
        @JvmStatic
        fun getLine1Number(activity: Activity): String {
            return if (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.READ_PHONE_STATE
                )
            ) (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                (activity.getSystemService(TELEPHONY_SERVICE) as TelephonyManager).line1Number
            } else (activity.getSystemService(TELEPHONY_SERVICE) as TelephonyManager).deviceId) else ""
        }
        @JvmStatic
        fun getAndroidId(activity: Activity): String {
            return if (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.READ_PHONE_STATE
                )
            )
                Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID)
            else ""
        }

        /**
         * 获取CPU核数
         */
        @JvmStatic
        fun getCpuProcessors(): Int {
            return Runtime.getRuntime().availableProcessors()
        }

        /**
         * 获取CPU位数
         */
        @JvmStatic
        fun getCpuArchitectureType(): String {
            return if (isLibc64()) "64-Bit" else "32-Bit"
        }

        /**
         * Check if system libc.so is 32 bit or 64 bit
         */
        private fun isLibc64(): Boolean {
            val libcFile = File(SYSTEM_LIB_C_PATH)
            if (libcFile != null && libcFile.exists()) {
                val header = readELFHeadrIndentArray(libcFile)
                if (header != null && header[EI_CLASS].toInt() == ELFCLASS64) {
                    if (LOGENABLE) {
                        Log.d("isLibc64()", "$SYSTEM_LIB_C_PATH is 64bit")
                    }
                    return true
                }
            }

            val libcFile64 = File(SYSTEM_LIB_C_PATH_64)
            if (libcFile64 != null && libcFile64.exists()) {
                val header = readELFHeadrIndentArray(libcFile64)
                if (header != null && header[EI_CLASS].toInt() == ELFCLASS64) {
                    if (LOGENABLE) {
                        Log.d("isLibc64()", "$SYSTEM_LIB_C_PATH_64 is 64bit")
                    }
                    return true
                }
            }

            return false
        }

        /**
         * ELF文件头格式是固定的:文件开始是一个16字节的byte数组e_indent[16]
         * e_indent[4]的值可以判断ELF是32位还是64位
         */
        private fun readELFHeadrIndentArray(libFile: File?): ByteArray? {
            if (libFile != null && libFile.exists()) {
                var inputStream: FileInputStream? = null
                try {
                    inputStream = FileInputStream(libFile)
                    if (inputStream != null) {
                        val tempBuffer = ByteArray(16)
                        val count = inputStream.read(tempBuffer, 0, 16)
                        if (count == 16) {
                            return tempBuffer
                        } else {
                            if (LOGENABLE) {
                                Log.e(
                                    "readELFHeadrIndentArray",
                                    "Error: e_indent lenght should be 16, but actual is $count"
                                )
                            }
                        }
                    }
                } catch (t: Throwable) {
                    if (LOGENABLE) {
                        Log.e("readELFHeadrIndentArray", "Error:" + t.toString())
                    }
                } finally {
                    if (inputStream != null) {
                        try {
                            inputStream.close()
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }

                    }
                }
            }

            return null
        }

        /**
         * 获取CPU名称
         */
        @Deprecated("Some device get cpu info failed")
        fun getCpuName(): String? {

            val str1 = "/proc/cpuinfo"
            var str2: String
            var localBufferedReader: BufferedReader? = null
            try {
                val fr = FileReader(str1)
                localBufferedReader = BufferedReader(fr)
                do {
                    str2 = localBufferedReader.readLine()
                    if (str2 != null && str2.contains("Hardware")) {
                        return str2.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
                    }
                } while (true)

            } catch (e: Exception) {
            } finally {
                localBufferedReader?.close()
            }

            return null
        }

        /**
         * 获取最大内存
         */
        @JvmStatic
        fun getTotalMemory(): String {
            val str1 = "/proc/meminfo"
            var str2: String
            try {
                val fr = FileReader(str1)
                val localBufferedReader = BufferedReader(fr)
                do {
                    str2 = localBufferedReader.readLine()
                    if (str2.contains("MemTotal")) {
                        return formatSize((str2.split(":")[1].replace("kB", "").trim().toLong()) * 1024L)
                    }
                } while (true)
            } catch (e: Exception) {
            }
            return ""
        }

        /**
         * 获取可用内存
         */
        @JvmStatic
        fun getAvailMemory(context: Context): String {
            val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val mi = ActivityManager.MemoryInfo()
            am.getMemoryInfo(mi)
            return formatSize(mi.availMem)
        }

        /**
         * 格式化size大小,单位B
         */
        private fun formatSize(size: Long): String {
            var suffix: String? = null
            var fSize: Float

            if (size >= 1024) {
                suffix = "KB"
                fSize = (size / 1024).toFloat()
                if (fSize >= 1024) {
                    suffix = "MB"
                    fSize /= 1024f
                }
                if (fSize >= 1024) {
                    suffix = "GB"
                    fSize /= 1024f
                }
            } else {
                fSize = size.toFloat()
            }
            val df = java.text.DecimalFormat("#0.00")
            val resultBuffer = StringBuilder(df.format(fSize.toDouble()))
            if (suffix != null)
                resultBuffer.append(suffix)
            return resultBuffer.toString()
        }

        /**
         * 获取屏幕宽度
         */
        @JvmStatic
        fun getScreenWidth(activity: Activity): String {
            val metric = DisplayMetrics()
            activity.windowManager.defaultDisplay.getMetrics(metric)
            return metric.widthPixels.toString()     // 屏幕宽度（像素）
        }

        /**
         * 获取屏幕高度(height+navigation bar Height)
         */
        @JvmStatic
        fun getScreenHeight(activity: Activity): String {
            val metric = DisplayMetrics()
            activity.windowManager.defaultDisplay.getMetrics(metric)
            return (metric.heightPixels + getNavigationBarHeight(activity)).toString()     // 屏幕高度（像素）
        }

        /**
         * 底部导航栏是否显示
         */
        private fun isNavigationBarShow(activity: Activity): Boolean {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                val display = activity.windowManager.defaultDisplay
                val size = Point()
                val realSize = Point()
                display.getSize(size)
                display.getRealSize(realSize)
                realSize.y != size.y
            } else {
                val menu = ViewConfiguration.get(activity).hasPermanentMenuKey()
                val back = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK)
                !(menu || back)
            }
        }

        /**
         * 获取底部导航栏高度
         */
        private fun getNavigationBarHeight(activity: Activity): Int {
            if (!isNavigationBarShow(activity)) {
                return 0
            }
            val resources = activity.resources
            val resourceId = resources.getIdentifier(
                "navigation_bar_height",
                "dimen", "android"
            )
            //获取NavigationBar的高度
            return resources.getDimensionPixelSize(resourceId)
        }

        /**
         * 返回运营商 需要加入权限 <uses-permission android:name="android.permission.READ_PHONE_STATE"></uses-permission> <BR></BR>
         *
         * @return 1,代表中国移动，2，代表中国联通，3，代表中国电信，0，代表未知
         * @author youzc@yiche.com
         */
        @SuppressLint("HardwareIds")
        @JvmStatic
        fun getOperators(activity: Activity): String {
            // 移动设备网络代码（英语：Mobile Network Code，MNC）是与移动设备国家代码（Mobile Country Code，MCC）（也称为“MCC /
            // MNC”）相结合, 例如46000，前三位是MCC，后两位是MNC 获取手机服务商信息
            var operatorsName = "未知"

            if (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.READ_PHONE_STATE
                )
            ) {
                val imsi = (activity.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager).subscriberId
                // IMSI号前面3位460是国家，紧接着后面2位00 运营商代码
                if (imsi == null) {
                    return operatorsName
                } else if (imsi.startsWith("46000") || imsi.startsWith("46002") || imsi.startsWith("46007")) {
                    operatorsName = "中国移动"
                } else if (imsi.startsWith("46001") || imsi.startsWith("46006")) {
                    operatorsName = "中国联通"
                } else if (imsi.startsWith("46003") || imsi.startsWith("46005")) {
                    operatorsName = "中国电信"
                }
            }


            return operatorsName
        }

    }


}
