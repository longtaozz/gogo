package com.zt.capacity.common.util;

public class CarUtil {


    /**
     * 判断是否是车牌号
     */
    public static boolean isCarNo(String CarNum) {
        //匹配第一位汉字
        String str = "京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼甲乙丙己庚辛壬寅辰戍午未申";
        CarNum=CarNum.toUpperCase();
        if (!(CarNum == null || CarNum.equals(""))) {
            String s1 = CarNum.substring(0, 1); //获取字符串的第一个字符
            if (str.contains(s1)) {
                String s2 = CarNum.substring(1, CarNum.length()); //不包含I O i o的判断
//                if (s2.contains("I") || s2.contains("i") || s2.contains("O") || s2.contains("o")) {
//                    return true;
//                } else {
                if (!CarNum.matches("^[\u4e00-\u9fa5]{1}[A-Z]{1}[a-z_A-Z_0-9]{5}$")) {
                    return true;
                }
//            }
            } else {
                return true;
            }
        } else {
            return false;
        }
        return false;
    }
}
