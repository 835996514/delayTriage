package com.rc.temp.service;

import com.rc.temp.db.PatientRegister;
import com.rc.temp.db.entity.ResponseResult;

public interface RegisterService {

    ResponseResult tempRegister(PatientRegister patientRegister,Integer areaId);

    ResponseResult tempRedisCache(PatientRegister patientRegister,Integer areaId);

    ResponseResult deleteTempCache(String register_id,Integer areaId);

    ResponseResult deleteAll(String[] registerIds,Integer areaId);

    ResponseResult updateTempRegister(PatientRegister patientRegister,Integer areaId);
}