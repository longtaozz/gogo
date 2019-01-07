package com.zt.capacity.jinan_zwt.plate;

public class PlateInfo {

    public String[] color = { "????", "????????", "????????", "??????????", "????????", "????????", "????????", "????????", "????????", "????????", "????" };
    private float confidence;
    private int errorCode;
    private int height;
    private String plateName;
    private int plateType;
    public String[] warningInfo = { "??????????", "????????", "????????????,??????", "????????" };
    private int width;
    private int x;
    private int y;

    public PlateInfo(int paramInt)
    {
        this.errorCode = paramInt;
    }

    public PlateInfo(String paramString, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, float paramFloat)
    {
        this.plateName = paramString;
        this.plateType = paramInt1;
        this.x = paramInt2;
        this.y = paramInt3;
        this.width = paramInt4;
        this.height = paramInt5;
        this.confidence = paramFloat;
        this.errorCode = -1;
    }

    public float getConfidence()
    {
        return this.confidence;
    }

    public int getErrorCode()
    {
        return this.errorCode;
    }

    public int getHeight()
    {
        return this.height;
    }

    public String getPlateName()
    {
        return this.plateName;
    }

    public int getPlateType()
    {
        return this.plateType;
    }

    public String getWarningInfo()
    {
        if (this.errorCode != -1) {
            return this.warningInfo[this.errorCode];
        }
        return "";
    }

    public int getWidth()
    {
        return this.width;
    }

    public int getX()
    {
        return this.x;
    }

    public int getY()
    {
        return this.y;
    }

    public void setConfidence(float paramFloat)
    {
        this.confidence = paramFloat;
    }

    public void setErrorCode(int paramInt)
    {
        this.errorCode = paramInt;
    }

    public void setHeight(int paramInt)
    {
        this.height = paramInt;
    }

    public void setPlateName(String paramString)
    {
        this.plateName = paramString;
    }

    public void setPlateType(int paramInt)
    {
        this.plateType = paramInt;
    }

    public void setWidth(int paramInt)
    {
        this.width = paramInt;
    }

    public void setX(int paramInt)
    {
        this.x = paramInt;
    }

    public void setY(int paramInt)
    {
        this.y = paramInt;
    }

}
