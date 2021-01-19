package com.apex.hrssmachineexecute.domain;

import java.util.Date;

public class CustomWorker {
    /**
     * 工人唯一标识
     */
    private Integer workerId;

//	private String workeId;

    /**
     * 工作姓名
     */
    private String workerName;

    /**
     * 班组唯 一标识
     */
    private Integer teamId;

    /**
     * 身份证号
     */
    private String idcardNo;

    /**
     * 在职状态  1在职,0离职
     */
    private int status;

    /**
     * 是否培训
     */
    private boolean isTrain;

    /**
     * 最后培训时间
     */
    private Date lastTrainTime;

    /**
     * 工人工种
     */
    private String workType;

    /**
     * 工人类型
     */
    private Integer workerRole;

    /**
     * 民族
     */
    private String nation;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 身份证地址
     */
    private String address;
    /**
     * 生日
     */
    private Date birthday;
    /**
     * 发证机关
     */
    private String grantOrg;
    /**
     * 有效期开始
     */
    private Date startDate;
    /**
     * 有效期结束
     */
    private Date expiryDate;

    /**
     * 特征信息
     */

    private byte[] feature;

    private byte[] headImage;

//	public String getWorkeId() {
//		return workeId;
//	}

//	public void setWorkeId(String workeId) {
//		this.workeId = workeId;
//	}

    public Integer getWorkerId() {
        return workerId;
    }

    public void setWorkerId(Integer workerId) {
        this.workerId = workerId;
    }

    public String getWorkerName() {
        return workerName;
    }

    public void setWorkerName(String workerName) {
        this.workerName = workerName;
    }

    public String getIdcardNo() {
        return idcardNo;
    }

    public void setIdcardNo(String idcardNo) {
        this.idcardNo = idcardNo;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isTrain() {
        return isTrain;
    }

    public void setTrain(boolean isTrain) {
        this.isTrain = isTrain;
    }

    public Date getLastTrainTime() {
        return lastTrainTime;
    }

    public void setLastTrainTime(Date lastTrainTime) {
        this.lastTrainTime = lastTrainTime;
    }

    public String getWorkType() {
        return workType;
    }

    public void setWorkType(String workType) {
        this.workType = workType;
    }

    public byte[] getFeature() {
        return feature;
    }

    public void setFeature(byte[] feature) {
        this.feature = feature;
    }

    public Integer getTeamId() {
        return teamId;
    }

    public void setTeamId(Integer teamId) {
        this.teamId = teamId;
    }

    public Integer getWorkerRole() {
        return workerRole;
    }

    public void setWorkerRole(Integer workerRole) {
        this.workerRole = workerRole;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getGrantOrg() {
        return grantOrg;
    }

    public void setGrantOrg(String grantOrg) {
        this.grantOrg = grantOrg;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public byte[] getHeadImage() {
        return headImage;
    }

    public void setHeadImage(byte[] headImage) {
        this.headImage = headImage;
    }
}
