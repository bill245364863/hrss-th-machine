package com.apex.hrss.provider;

import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.apex.hrss.constant.EnumDeviceModel;
import com.apex.hrss.constants.HttpCode;
import com.apex.hrss.domain.*;
import com.apex.hrss.utils.*;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class JsgDeviceServiceProvider implements DeviceServiceProvider {

    private static final Logger logger = ThLogger.getLogger(JsgDeviceServiceProvider.class);
    @Value("${project.secret.key}")
    private   String SECRET_KEY;
    @Value("${project.address.connect}")
    private  String address;
    @Value("${project.appCore.key}")
    private   String APPCORE_KEY;
    @Value("${project.appId}")
    private   String APPID;
    @Override
    public CustomDevice loadDevice(String deviceVerify, String deviceCode) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("deviceVerify", deviceVerify);
        params.put("deviceCode", deviceCode);
        HttpResponse httpResponse = HttpUtil.createPost(address + HttpCode.SELECTBYDEVICECODE).form(params).execute();
        JSONObject json = JSON.parseObject(httpResponse.body());
        JSONArray data = json.getJSONArray("data");
        JSONObject entity = data.getJSONObject(0);
//        TProjectDevice entity = deviceMapper.selectByDeviceCode(params);
//        TBillTestData tBillTestData = deviceMapper.selectBill(params);
//        if (tBillTestData == null) {
//            logger.info("数据不为空"+ tBillTestData.toString());
//        }
        if (null == entity) {
            logger.info("查不到数据");
            return null;
        }


        CustomDevice device = new CustomDevice();
        device.setDeviceVerify(deviceVerify);
        device.setDeviceCode(deviceCode);
        device.setDeviceModel(EnumDeviceModel.formCode(entity.getString("deviceModels")));
        return device;
    }

    @Override
    public CustomProject loadProject(CustomDevice customDevice) {
        String deviceCode = customDevice.getDeviceCode();
        String deviceVerify = customDevice.getDeviceVerify();

        Map<String, Object> params = new HashMap<>();
        params.put("deviceVerify", deviceVerify);
        params.put("deviceCode", deviceCode);
        HttpResponse httpResponse = HttpUtil.createPost(address + HttpCode.SELECTBYDEVICECODE).form(params).execute();
        JSONObject json = JSON.parseObject(httpResponse.body());
        JSONArray data = json.getJSONArray("data");
        JSONObject device = data.getJSONObject(0);
//        TProjectDevice device = deviceMapper.selectByDeviceCode(params);
        if (null == device) {
            return null;
        }
//
//        // 建设项目名
//        String buildProjectId = device.getBuildProjectId();
//        TBasicProjectBuild buildProject = basicProjectBuildMapper.selectByPrimaryKey(buildProjectId);
//        if (null == buildProject) {
//            return null;
//        }
        //获取工程信息
//        String projectId = device.getProjectId();
        String projectId = device.getString("projectId");
        params.clear();
        params.put("projectId", projectId);
        httpResponse = HttpUtil.createPost(address + HttpCode.SELECTBYPROJECTINFO).form(params).execute();
        json = JSON.parseObject(httpResponse.body());
        data = json.getJSONArray("data");
        JSONObject project = data.getJSONObject(0);
//        TBasicProjectInfo project = basicProjectInfoMapper.selectByPrimaryKey(projectId);

        if (null == project) {
//            Integer seqNo = buildProject.getSeqNo();
//            if (null == seqNo) {
//                seqNo = seqService.put(SeqCode.PROJECT);
//
//                TBasicProjectBuild tmp = new TBasicProjectBuild();
//                tmp.setId(buildProjectId);
//                tmp.setSeqNo(seqNo);
//                basicProjectBuildMapper.updateByPrimaryKeySelective(tmp);
//            }
//            logger.info("【设备编号】：{} - 【建设项目】 - [seqNo]: {}", deviceCode,seqNo);
//            CustomProject result = new CustomProject();
//            result.setProjectName(buildProject.getBuildName());
//            result.setProjectCode(seqNo);
//            return result;
            return null;
        } else {
//            Integer seqNo = project.getSeqNo();
//            if (null == seqNo) {
//                seqNo = seqService.put(SeqCode.PROJECT);
//
//                TBasicProjectInfo tmp = new TBasicProjectInfo();
//                tmp.setId(projectId);
//                tmp.setSeqNo(seqNo);
//                basicProjectInfoMapper.updateByPrimaryKeySelective(tmp);
//            }
            logger.info("【设备编号】：{} - 【工程编号】 - [prijectCode]: {}", deviceCode, project.getString("projectCode"));
            CustomProject result = new CustomProject();
            result.setProjectName(project.getString("projectName"));
            result.setProjectCode(project.getString("projectCode"));
            return result;
        }


    }

    @Override
    public boolean processFeature(CustomDevice customDevice, CustomFeature feature) {
        return false;
    }

    @Override
    public Map<String, Object> processAttendance(CustomDevice customDevice, List<CustomAttendance> attendanceList) {
        Map<String, Object> resultMap = new HashMap<>(2);
        String deviceCode = customDevice.getDeviceCode();
        String deviceVerify = customDevice.getDeviceVerify();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("deviceVerify", deviceVerify);
        map.put("deviceCode", deviceCode);
        map.put("deviceType", 1);


        HttpResponse httpResponse = HttpUtil.createPost(address + HttpCode.SELECTBYDEVICECODE).form(map).execute();
//        System.out.println(httpResponse.body());
        JSONObject json = JSON.parseObject(httpResponse.body());
        JSONArray data = json.getJSONArray("data");
        JSONObject dataJSONObject = data.getJSONObject(0);

        if (data.size() < 0) {
            String msg = "处理考勤数据失败，缺失TProjectDevice数据";
            logger.info(msg + ",deviceCode:{}", deviceCode);
            return getResultMsg(msg, false);
        }

        String projectId = dataJSONObject.getString("projectId");

        for (CustomAttendance attendance : attendanceList) {
            map.clear();
            map.put("seqNo", attendance.getWorkerId());
            map.put("projectId", projectId);
            httpResponse = HttpUtil.createPost(address + HttpCode.SELECTTEAMWORKERINFO).form(map).execute();
//            System.out.println(httpResponse.body());
            json = JSON.parseObject(httpResponse.body());
            data = json.getJSONArray("data");
            if (data.size() < 0) {
                logger.info("处理考勤数据跳过，工人信息不存在-DeviceNo:{},WokerID:{}", deviceCode, attendance.getWorkerId());
                continue;
            }
            dataJSONObject = data.getJSONObject(0);
            String projectCode = dataJSONObject.getString("projectCode");
            Integer teamCode = dataJSONObject.getInteger("teamCode");
            String idcardtype = dataJSONObject.getString("fidcardtype");
            String idcardnum = dataJSONObject.getString("fidcardnum");
            Integer attendDir = attendance.getAttendDir();
            String attendDate = DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("projectCode", projectCode);
            jsonObject.put("teamSysNo", teamCode);
            JSONArray array = new JSONArray();
            JSONObject workerAttendInfo = new JSONObject();
            workerAttendInfo.put("idCardType", idcardtype);
            workerAttendInfo.put("idCardNumber", AESApopUtil.encrypt(idcardnum, SECRET_KEY));
            workerAttendInfo.put("date", attendDate);
            workerAttendInfo.put("direction", attendDir);

            array.add(workerAttendInfo);
            jsonObject.put("dataList", array);
            logger.debug(jsonObject.toString());
            ExecutorService executorService = Executors.newCachedThreadPool();
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    String sign = null;
                    try {
                        map.clear();
                        String time = DateUtils.format(new Date(), "yyyyMMddHHmmss");
                        map.put("method", "WorkerAttendance.Add");
                        map.put("version", "1.0");
                        map.put("appid", APPID);
                        map.put("format", "json");
                        map.put("timestamp", time);
                        map.put("nonce", UUID.randomUUID().toString().replaceAll("-", ""));
                        map.put("corpsign", AESApopUtil.encrypt(APPCORE_KEY + time, SECRET_KEY));
                        map.put("data", jsonObject.toString());
                        String urlParam = FormatParamUtil.formatUrlMap(map);
                        urlParam += "&appsecret=" + SECRET_KEY;


                        sign = SHAApopUtil.getSHA256(urlParam.toLowerCase());
                        map.put("sign", sign);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    HttpUtil.createPost(address + HttpCode.THIRD_PARTY_INTERFACE).form(map).execute();
                }
            });


        }


        return getResultMsg("考勤数据插入成功", true);
    }

    private Map<String, Object> getResultMsg(String msg, Object object) {
        Map<String, Object> resultMap = new HashMap<>(2);
        resultMap.put("msg", msg);
        resultMap.put("isDone", object);
        return resultMap;
    }

    @Override
    public List<CustomWorker> thLoadAllWorkers(CustomDevice customDevice) {
        String deviceCode = customDevice.getDeviceCode();
        String deviceVerify = customDevice.getDeviceVerify();

//        Map<String, Object> params = new HashMap<>();
//        params.put("deviceVerify", deviceVerify);
//        params.put("deviceCode", deviceCode);
//        params.put("deviceType", DeviceTypeEnum.ATTENDANCE.getValue());
//        //获取设备信息
//        TProjectDevice device = deviceMapper.selectByDeviceCode(params);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("deviceVerify", deviceVerify);
        map.put("deviceCode", deviceCode);
        map.put("deviceType", 1);


        HttpResponse httpResponse = HttpUtil.createPost(address + HttpCode.SELECTBYDEVICECODE).form(map).execute();

        JSONObject json = JSON.parseObject(httpResponse.body());
        JSONArray data = json.getJSONArray("data");
        if (data.size() < 0) {
            return Collections.emptyList();
        }
        String projectId = "";
        String sendTime = "";
        for (int i = 0; i < data.size(); i++) {
            JSONObject jsonObject = data.getJSONObject(i);
            projectId = jsonObject.getString("projectId");
            sendTime = json.getString("sendTime");
        }
//        String buildProjectId = device.getBuildProjectId();

//        String projectId = device.getProjectId();


//      获取人员下发名单时间 拿去人员,根据上一次人员下发时间进行获取
//        Date sendTime = device.getSendTime();

//        String featureType = device.getDeviceFeatureType();

//        map.clear();
        //35d0cf3eb2dc429a9037404c43b87794
//        params.put("buildProjectId", buildProjectId);
        //b755983c46fb465cb18ae0b88b58ba2b
//        map.put("projectId", projectId);
        //特征类型100
//        params.put("featureType", featureType);
//        取设备上的名单下发时间
//        map.put("updateTime", sendTime);
//        map.put("att_mark", "mark");
        //824拿人员
//        List<TeamWorkerFeatureVO> featureList = null;
        JSONArray featureListData = null;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("projectId", projectId);
        jsonObject.put("sendTime", sendTime);
        jsonObject.put("att_mark", "mark");
        try {
            String sign = null;
            map.clear();
            String time = DateUtils.format(new Date(), "yyyyMMddHHmmss");
            map.put("method", "ProjectWorker.Query");
            map.put("version", "1.0");
            map.put("appid", APPID);
            map.put("format", "json");
            map.put("timestamp", time);
            map.put("nonce", UUID.randomUUID().toString().replaceAll("-", ""));
            map.put("corpsign", AESApopUtil.encrypt(APPCORE_KEY + time, SECRET_KEY));
            map.put("data", jsonObject.toString());
            String urlParam = FormatParamUtil.formatUrlMap(map);
            urlParam += "&appsecret=" + SECRET_KEY;


            sign = SHAApopUtil.getSHA256(urlParam.toLowerCase());
            map.put("sign", sign);

            HttpResponse httpResponse1 = HttpUtil.createPost(address + HttpCode.THIRD_PARTY_INTERFACE).form(map).execute();
            logger.debug(httpResponse1.body());
            JSONObject projectWorker = JSON.parseObject(httpResponse1.body());
            featureListData = projectWorker.getJSONArray("rows");
        } catch (Exception e) {
            logger.error("发生了数据错误");
            e.printStackTrace();
        }

        if (featureListData.isEmpty()) {
            logger.info("featureList 为空");
            return Collections.emptyList();
        }
        List<CustomWorker> result = new ArrayList<>();
        for (int i = 0; i < featureListData.size(); i++) {
//        for (TeamWorkerFeatureVO f : featureList) {
            JSONObject feature = featureListData.getJSONObject(i);
            Integer seqNo = feature.getInteger("seqNo");
//            String workerID = f.getWorkerID();
//            if (null == seqNo) {
//                String id = f.getId();
//                seqNo = seqService.put(SeqCode.WORKER);
//                TProjectTeamWorker tmp = new TProjectTeamWorker();
//                tmp.setId(id);
//                tmp.setSeqNo(seqNo);
//                projectTeamWorkerMapper.updateByPrimaryKeySelective(tmp);
//            }

            CustomWorker worker = new CustomWorker();
            worker.setWorkerId(seqNo);
//            worker.setWorkeId(workerID);
            worker.setWorkerName(feature.getString("workerName"));

            worker.setIdcardNo(AESApopUtil.decrypt(feature.getString("idCardNumber"), SECRET_KEY));
            worker.setStatus(feature.getInteger("status"));
            result.add(worker);
        }
//        获取设备上的名单下发时间之后,需要更新设备的名单下发时间为当前时间
        map.clear();
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.submit(new Runnable() {
            @Override
            public void run() {

                map.put("deviceVerify", deviceVerify);
                map.put("deviceCode", deviceCode);
                map.put("sendTime", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
                HttpUtil.createPost(address + HttpCode.UPDATESENDTIME).form(map).execute();

            }
        });


        return result;
    }

    @Override
    public List<CustomWorker> thLoadWorkerFeature(CustomDevice customDevice, List<String> idCardList) {
        String deviceCode = customDevice.getDeviceCode();
        String deviceVerify = customDevice.getDeviceVerify();

//        Map<String, Object> params = new HashMap<>();
//        params.put("deviceVerify", deviceVerify);
//        params.put("deviceCode", deviceCode);
//        params.put("deviceType", DeviceTypeEnum.ATTENDANCE.getValue());
//
//        TProjectDevice device = deviceMapper.selectByDeviceCode(params);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("deviceVerify", deviceVerify);
        map.put("deviceCode", deviceCode);
        map.put("deviceType", 1);


        HttpResponse httpResponse = HttpUtil.createPost(address + HttpCode.SELECTBYDEVICECODE).form(map).execute();
        JSONObject json = JSON.parseObject(httpResponse.body());
        JSONArray data = json.getJSONArray("data");
        JSONObject device = data.getJSONObject(0);
        if (null == device) {
            return Collections.emptyList();
        }

//        String buildProjectId = device.getBuildProjectId();
//        String buildProjectId = device.getString("buildProjectId");
//        String projectId = device.getProjectId();
        String projectId = device.getString("projectId");
//        String featureType = device.getDeviceFeatureType();
//        String featureType = device.getString("deviceFeatureType");

//        map.clear();
//        params.put("buildProjectId", buildProjectId);
//        map.put("projectId", projectId);
//        params.put("featureType", featureType);
//        根据身份证找特征
//        map.put("idCardList", StringUtils.join(idCardList, ","));
//        map.put("status", 1);
//        List<TeamWorkerFeatureVO> featureList = projectTeamWorkerMapper.selectTeamWorkerFeature(params);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("projectId", projectId);
        jsonObject.put("idCardNumber", AESApopUtil.encrypt(StringUtils.join(idCardList, ","),SECRET_KEY));
        jsonObject.put("status", 1);
        //考勤标志
        jsonObject.put("att_mark", "mark");
        String sign = null;
        try {
            map.clear();
            String time = DateUtils.format(new Date(), "yyyyMMddHHmmss");
            map.put("method", "ProjectWorker.Query");
            map.put("version", "1.0");
            map.put("appid", APPID);
            map.put("format", "json");
            map.put("timestamp", time);
            map.put("nonce", UUID.randomUUID().toString().replaceAll("-", ""));
            map.put("corpsign", AESApopUtil.encrypt(APPCORE_KEY + time, SECRET_KEY));
            map.put("data", jsonObject.toString());
            String urlParam = FormatParamUtil.formatUrlMap(map);
            urlParam += "&appsecret=" + SECRET_KEY;


            sign = SHAApopUtil.getSHA256(urlParam.toLowerCase());
            map.put("sign", sign);
        } catch (Exception e) {
            e.printStackTrace();
        }

        httpResponse = HttpUtil.createPost(address + HttpCode.THIRD_PARTY_INTERFACE).form(map).execute();
        json = JSON.parseObject(httpResponse.body());
        data = json.getJSONArray("rows");


        if (data.isEmpty()) {
            logger.info("featuredataList 为空");
            return Collections.emptyList();
        }


        List<CustomWorker> result = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            JSONObject dataJSONObject = data.getJSONObject(i);

//        for (TeamWorkerFeatureVO f : featureList) {
//            Integer seqNo = f.getSeqNo();
            Integer seqNo = dataJSONObject.getInteger("seqNo");
//            工人信息的id
//            String fid = f.getFid();
//            String fid = dataJSONObject.getString("fid");
//            final String workerID = f.getWorkerID();
            if (null == seqNo) {
//                String id = f.getId();
//                seqNo = seqService.put(SeqCode.WORKER);
//                TProjectTeamWorker tmp = new TProjectTeamWorker();
//                tmp.setId(id);
//                tmp.setSeqNo(seqNo);
//                projectTeamWorkerMapper.updateByPrimaryKeySelective(tmp);
            }

            CustomWorker worker = new CustomWorker();
            try {
                worker.setWorkerId(seqNo);
//            worker.setWorkeId(workerID);
//            worker.setTeamId(f.getTeamSeqNo());
                worker.setTeamId(dataJSONObject.getInteger("teamSysNo"));
//            worker.setWorkerName(f.getName());
                worker.setWorkerName(dataJSONObject.getString("workerName"));
//            worker.setIdcardNo(f.getIdcardNo());
                worker.setIdcardNo(AESApopUtil.decrypt(dataJSONObject.getString("idCardNumber"),SECRET_KEY));
//            worker.setStatus(f.getStatus());
                worker.setStatus(dataJSONObject.getInteger("status"));
                worker.setTrain(false);
//            worker.setWorkerRole(f.getWorkerRole());
                worker.setWorkerRole(dataJSONObject.getInteger("workRole"));
//            worker.setNation(f.getNation());
                worker.setNation(dataJSONObject.getString("nation"));
                worker.setGender(dataJSONObject.getInteger("gender"));
                worker.setAddress(dataJSONObject.getString("address"));

                worker.setBirthday(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dataJSONObject.getString("birthDate")));
                worker.setGrantOrg(dataJSONObject.getString("grantOrg"));
//            worker.setStartDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dataJSONObject.getString("startDate")));
//            worker.setExpiryDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dataJSONObject.getString("expiryDate")));
            } catch (ParseException e) {
                e.printStackTrace();
            }

//            String workTypeName = dictService.findValueByCode(TDictConstant.DICT_CATALOG_18, f.getWorkType());
            //worker.setWorkType(workTypeName);

//            InputStream featureInput = FileUtils.get(f.getFeatureFileUrl());
//            作为白名单头像,考勤打卡使用
            byte[] featureFiles = Base64.getDecoder().decode(dataJSONObject.getString("featureFile"));
            ByteArrayInputStream featureInput = new ByteArrayInputStream(featureFiles);
//            attnPath = !attnPath.endsWith("/") && !attnPath.endsWith("\\") ? attnPath + File.separator : attnPath;
//            String basePath = StringUtils.join(attnPath, "T_PSM_WorkerInfo",File.separator,fid,"." ,"FHeadImage");
//            String basePath = StringUtils.join(attnPath, "T_PSM_WorkerInfo", File.separator, "5f09ab243b6c4c57bd4c27c20608efb1", ".", "FHeadImage");
//            InputStream featureInput = FileUtils.get(basePath);
            ByteArrayOutputStream featureOutput = new ByteArrayOutputStream();
            try {
                IOUtils.copy(featureInput, featureOutput);
            } catch (IOException e) {
                logger.error("thLoadWorkerFeature - featureInput error!");
            } finally {
                IOUtils.closeQuietly(featureInput);
            }
            worker.setFeature(featureOutput.toByteArray());

//            InputStream headImageInput = FileUtils.get(f.getHeadImageUrl());
//            InputStream headImageInput = FileUtils.get(basePath);
//            ByteArrayOutputStream headImageOutput = new ByteArrayOutputStream();
//            try {
//                IOUtils.copy(headImageInput, headImageOutput);
//            } catch (Exception e) {
//                logger.error("thLoadWorkerFeature - headImageInput error!");
//            } finally {
//                IOUtils.closeQuietly(headImageInput);
//            }

            worker.setHeadImage(featureOutput.toByteArray());
            result.add(worker);
        }
        return result;
    }

    @Override
    public void refresh(CustomDevice customDevice) {
        if (null == customDevice) {
            return;
        }
        String deviceCode = customDevice.getDeviceCode();
        String deviceVerify = customDevice.getDeviceVerify();

        Map<String, Object> params = new HashMap<>();
        params.put("deviceVerify", deviceVerify);
        params.put("deviceCode", deviceCode);
        params.put("connTime", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        params.put("upStatus", 0);

        HttpUtil.createPost(address + HttpCode.UPDATECONNTIME).form(params).execute();
//        deviceMapper.updateConnTime(params);
    }
}
