package com.longwu.appcode.util

class SystemInfo2 {

//    /**
//     * Scan device looking for CPU temperature in all well known locations
//     */
//    fun getCpuTemperatureFinder(): Maybe<CpuTemperatureResult> {
//        return Observable.fromIterable(CPU_TEMP_FILE_PATHS)
//            .map { path ->
//                val temp = Utils.readOneLine(File(path))
//                var validPath = ""
//                var currentTemp = 0.0
//                if (temp != null) {
//                    // Verify if we are in normal temperature range
//                    if (isTemperatureValid(temp)) {
//                        validPath = path
//                        currentTemp = temp
//                    } else if (isTemperatureValid(temp / 1000)) {
//                        validPath = path
//                        currentTemp = temp / 1000
//                    }
//                }
//                CpuTemperatureResult(validPath, currentTemp.toInt())
//            }
//            .filter { (filePath) -> !filePath.isEmpty() }
//            .firstElement()
//    }
//
//    /**
//     * Check if passed temperature is in normal range: -30 - 250 Celsius
//     *
//     * @param temp current temperature
//     */
//    private fun isTemperatureValid(temp: Double): Boolean = temp in -30.0..250.0
//
//    /**
//     * Container for temperature value and path
//     */
//    data class CpuTemperatureResult(val filePath: String = "", val temp: Int = 0)
//
//    companion object {
//        // Ugly but currently the easiest working solution is to search well known locations
//        // If you know better solution please refactor this :)
//        private val CPU_TEMP_FILE_PATHS = listOf(
//            "/sys/devices/system/cpu/cpu0/cpufreq/cpu_temp",
//            "/sys/devices/system/cpu/cpu0/cpufreq/FakeShmoo_cpu_temp",
//            "/sys/class/thermal/thermal_zone0/temp",
//            "/sys/class/i2c-adapter/i2c-4/4-004c/temperature",
//            "/sys/devices/platform/tegra-i2c.3/i2c-4/4-004c/temperature",
//            "/sys/devices/platform/omap/omap_temp_sensor.0/temperature",
//            "/sys/devices/platform/tegra_tmon/temp1_input",
//            "/sys/kernel/debug/tegra_thermal/temp_tj",
//            "/sys/devices/platform/s5p-tmu/temperature",
//            "/sys/class/thermal/thermal_zone1/temp",
//            "/sys/class/hwmon/hwmon0/device/temp1_input",
//            "/sys/devices/virtual/thermal/thermal_zone1/temp",
//            "/sys/devices/virtual/thermal/thermal_zone0/temp",
//            "/sys/class/thermal/thermal_zone3/temp",
//            "/sys/class/thermal/thermal_zone4/temp",
//            "/sys/class/hwmon/hwmonX/temp1_input",
//            "/sys/devices/platform/s5p-tmu/curr_temp")
//    }
}