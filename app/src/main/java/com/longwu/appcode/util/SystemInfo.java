package com.longwu.appcode.util;


import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Pattern;

public class SystemInfo {
    public static String getCpuTemp() {
        String temp = "Unknow";
        BufferedReader br = null;
        FileReader fr = null;
        try {
            File dir = new File("/sys/class/thermal/");
            File[] files = dir.listFiles(new FileFilter() {
                @Override
                public boolean accept(File file) {
                    if (Pattern.matches("thermal_zone[0-9]+", file.getName())) {
                        return true;
                    }
                    return false;
                }
            });

            final int SIZE = files.length;
            Log.e("byWh","/sys/class/thermal/  file.size -- > " + SIZE);
            String path = "";
            String line = "";
            String type = "";
            for (int i = 0; i < SIZE; i++) {
                path = "/sys/class/thermal/thermal_zone" + i + "/type";
                fr = new FileReader(path);
                br = new BufferedReader(fr);
                line = br.readLine();
                if (line != null) {
                    type = line;
                }

                path = "/sys/class/thermal/thermal_zone" + i + "/temp";
                fr = new FileReader(path);
                br = new BufferedReader(fr);
                line = br.readLine();
                if (line != null) {
                    // MTK CPU
                    if (type.contains("cpu")) {
                        long temperature = Long.parseLong(line);
                        if (temperature < 0) {
                            temp = "Unknow";
                        } else {
                            temp = (float) (temperature / 1000.0) + "";
                        }
                    } else if (type.contains("tsens_tz_sensor")) {
                        // Qualcomm CPU
                        long temperature = Long.parseLong(line);
                        if (temperature < 0) {
                            temp = "Unknow";
                        } else if (temperature > 100){
                            temp = (float) (temperature / 10.0) + "";
                        } else {
                            temp = temperature + "";
                        }
                    }
                    Log.e("byWh",path);
                    Log.e("byWh",temp);
                    // text_info_01.setText("Cpu温度 --> " + temp);
                }
            }

            if (fr != null) {
                fr.close();
            }
            if (br != null) {
                br.close();
            }
        } catch (Exception e) {
            // LogUtil.e(e);
            Log.e("byWh",e.toString());
        } finally {
            if (fr != null) {
                try {
                    fr.close();
                } catch (Exception e) {
                    // LogUtil.e(e);
                    Log.e("byWh",e.toString());
                }
            }
            if (br != null) {
                try {
                    br.close();
                } catch (Exception e) {
                    // LogUtil.e(e);
                    Log.e("byWh",e.toString());
                }
            }
        }

        return temp;
    }

    public static String getBatteryTemp() {
        String temp = "Unknow";
        BufferedReader br = null;
        FileReader fr = null;
        String path1 = "";
        String path2 = "";
        String line = "";

        path1 = "/sys/class/power_supply/battery/temp";
        try {
            fr = new FileReader(path1);
            br = new BufferedReader(fr);
            line = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (line != null && !line.equals("")) {
            Log.e("byWh","/sys/class/power_supply/battery/temp -- > " + line);
            long temperature = Long.parseLong(line);
            temp = (float) (temperature / 10.0) + "";
            // text_info_02.setText("电池温度 --> " + temp);
        }

        path2 = "/sys/class/power_supply/battery/batt_temp";
        try {
            fr = new FileReader(path2);
            br = new BufferedReader(fr);
            line = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (line != null && !line.equals("")) {
            Log.e("byWh","/sys/class/power_supply/battery/temp -- > " + line);
            long temperature = Long.parseLong(line);
            temp = (float) (temperature / 10.0) + "";
            // text_info_02.setText("电池温度 --> " + temp);
        }

        if (fr != null) {
            try {
                fr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (br != null) {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return temp;
    }



}
