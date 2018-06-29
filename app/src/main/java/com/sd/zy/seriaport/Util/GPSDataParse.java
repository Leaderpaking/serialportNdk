package com.sd.zy.seriaport.Util;

import java.util.Calendar;

/**
 * WangYanfeng 作者 E-mail:wangyanfeng_2012@163.com
 * 创建时间：2015年2月3日 下午5:08:44
 * <p>
 * 解析GPS数据
 */
public class GPSDataParse {
    //将byte[]数组转实体类
    public static GPSDataEntity parse(String parase) {
        GPSDataEntity entity = new GPSDataEntity();


        String[] gpggaArray = parase.split(",");
        String time = gpggaArray[1];

//        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.HOUR, Integer.parseInt(time.substring(0, 2)));
//        calendar.set(Calendar.MINUTE, Integer.parseInt(time.substring(2, 4)));
//        calendar.set(Calendar.SECOND, Integer.parseInt(time.substring(4, 6)));
//        calendar.set(Calendar.MILLISECOND, Integer.parseInt(time.substring(7, time.length())));

        //时间
//        entity.setTime(calendar);
        //System.out.println("时间："+entity.getTime());

        //维度
        String latitude = gpggaArray[2] + gpggaArray[3];
        entity.setLatitude(latitude);
        //System.out.println("维度:"+latitude+"Entity:"+entity.getLatitude());

        //经度
        String longitude = gpggaArray[4] + gpggaArray[5];
        entity.setLongitude(longitude);
        //System.out.println("经度:"+longitude+"Entity:"+entity.getLongitude());

        //海拔
        String altitude = gpggaArray[11];
        try {
            entity.setAltitude(Double.parseDouble(altitude));
        } catch (Exception e) {

        }
        return entity;
    }
}
