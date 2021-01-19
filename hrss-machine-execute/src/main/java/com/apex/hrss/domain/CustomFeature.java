package com.apex.hrss.domain;

public class CustomFeature {
    private String cardNo;
    private String name;
    private String sex;
    private String nation;
    private String birthday;
    private String address;
    /**
     * 采集信息
     */
    private byte[] feature;
    private int featureLev;
    private int isRepick;
    private String idCardDept;
    private String idCardDate;
    private int isManual;
    /**
     * 保留字段，暂时没用
     */
    private byte[] backup;
    /**
     * 头像
     */
    private byte[] headImage;
    /**
     * 身份证照片
     */
    private byte[] idcardPhoto;
    /**
     * 采集照片
     */
    private byte[] collectPhoto;
    private byte[] xor;

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public byte[] getFeature() {
        return feature;
    }

    public void setFeature(byte[] feature) {
        this.feature = feature;
    }

    public int getFeatureLev() {
        return featureLev;
    }

    public void setFeatureLev(int featureLev) {
        this.featureLev = featureLev;
    }

    public int getIsRepick() {
        return isRepick;
    }

    public void setIsRepick(int isRepick) {
        this.isRepick = isRepick;
    }

    public String getIdCardDept() {
        return idCardDept;
    }

    public void setIdCardDept(String idCardDept) {
        this.idCardDept = idCardDept;
    }

    public String getIdCardDate() {
        return idCardDate;
    }

    public void setIdCardDate(String idCardDate) {
        this.idCardDate = idCardDate;
    }

    public int getIsManual() {
        return isManual;
    }

    public void setIsManual(int isManual) {
        this.isManual = isManual;
    }

    public byte[] getBackup() {
        return backup;
    }

    public void setBackup(byte[] backup) {
        this.backup = backup;
    }

    public byte[] getHeadImage() {
        return headImage;
    }

    public void setHeadImage(byte[] headImage) {
        this.headImage = headImage;
    }

    public byte[] getIdcardPhoto() {
        return idcardPhoto;
    }

    public void setIdcardPhoto(byte[] idcardPhoto) {
        this.idcardPhoto = idcardPhoto;
    }

    public byte[] getCollectPhoto() {
        return collectPhoto;
    }

    public void setCollectPhoto(byte[] collectPhoto) {
        this.collectPhoto = collectPhoto;
    }

    public byte[] getXor() {
        return xor;
    }

    public void setXor(byte[] xor) {
        this.xor = xor;
    }
}
