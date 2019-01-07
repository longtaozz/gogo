package pr.platerecognization;

import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.zt.capacity.jinan_zwt.plate.PlateInfo;


import java.util.ArrayList;
import java.util.List;


public class PlateRecognition {
    static {
        System.loadLibrary("hyperlpr");
    }

    public static long handle = 0;

    public static native long InitPlateRecognizer(String paramString1, String paramString2, String paramString3, String paramString4);

    static native void ReleasePlateRecognizer(long object);

    static native String SimpleRecognization(long inputMat, long object);

    private static native String RunRecognition(long paramLong1, long paramLong2, float paramFloat, int paramInt1, int paramInt2, int paramInt3);

    private static native String RunRecognitionAsRaw(byte[] paramArrayOfByte, int paramInt1, int paramInt2, long paramLong, float paramFloat, int paramInt3, int paramInt4, int paramInt5);

    public static String plateRecognitionWithMat(long paramMat, float paramFloat, int paramInt1, int paramInt2, int paramInt3) {
        return parserPlate(RunRecognition(paramMat, handle, paramFloat, paramInt1, paramInt2, paramInt3));
    }

    private static String parserPlate(String paramString) {
        Log.e("shibie.........", paramString);
        ArrayList<PlateInfo> localArrayList = new ArrayList();
        if (paramString.indexOf("sessionError") != -1) {
            localArrayList.add(new PlateInfo(Integer.parseInt(paramString.split("/")[1])));
        }
        String[] paramStringx = paramString.split("/");
        for (int i = 0; i < paramStringx.length; i++) {
            String[] arrayOfString;
            if (i < paramStringx.length) {
                arrayOfString = paramStringx[i].split(",");
                if (arrayOfString.length > 0) {
                    return arrayOfString[0];
                } else {
                    return "";
                }
            } else {
                return "";
            }
        }
        return "";
    }


    public static List<String> plateRecognitionWithRaw(byte[] paramArrayOfByte, int paramInt1, int paramInt2, float paramFloat, int paramInt3, int paramInt4, int paramInt5) {
        String paramArrayOfst = RunRecognitionAsRaw(paramArrayOfByte, paramInt1, paramInt2, handle, paramFloat, paramInt3, paramInt4, paramInt5);

        List<String> rets=new ArrayList<>();

        if (paramArrayOfst != null && !TextUtils.isEmpty(paramArrayOfst)) {
            //数据返回
            String[] nuberStrs=paramArrayOfst.split("/");
            if(nuberStrs.length>0){
                for (int i = 0; i <nuberStrs.length ; i++) {
                    String[] nuberStrd=nuberStrs[i].split(",");
                    if(nuberStrd.length>0){
                        Log.e("chepai......",paramArrayOfst);
                        rets.add(nuberStrd[0]);
//                        if(nuberStrd[0].length()==7){
//                            //车牌号数目正确
//                            if(Double.parseDouble(nuberStrd[6])>0.91){
//                                //准确度高于0.91的
//                            }
//                        }
                    }
                }
            }

        }
        return rets;


    }


}
