package com.rc.temp.controller;

import com.rc.temp.db.Area;
import com.rc.temp.db.PatientRegister;
import com.rc.temp.db.Plan;
import com.rc.temp.db.entity.ResponseResult;
import com.rc.temp.repository.AreaRepository;
import com.rc.temp.repository.PatientRegisterRepository;
import com.rc.temp.repository.PlanRepository;
import com.rc.temp.service.RegisterService;
import com.rc.temp.setting.ResultState;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Log4j2
@RequestMapping("/register")
@RestController
public class RegisterController {

    @Autowired
    AreaRepository areaRepository;

    @Autowired
    PlanRepository planRepository;

    @Autowired
    PatientRegisterRepository registerRepository;

    @Autowired
    RegisterService registerService;

    @PostMapping("/register")
    public ResponseResult register(PatientRegister patientRegister,Integer areaId){
        return registerService.tempRegister(patientRegister,areaId);
    }

    @GetMapping("/getRegisters")
    public ResponseResult getRegisters(PatientRegister patientRegister,Integer areaId){
        return registerService.tempRedisCache(patientRegister,areaId);
    }

    @PostMapping("/updateRegister")
    public ResponseResult updateTempRegister(PatientRegister patientRegister,Integer areaId){
        return registerService.updateTempRegister(patientRegister,areaId);
    }

    @PostMapping("/deleteRegister")
    public ResponseResult deleteTempRegister(String registerId,Integer areaId){
        return registerService.deleteTempCache(registerId,areaId);
    }

    @GetMapping("/getAreas")
    public ResponseResult getAllArea(){
        List<Area> areaList = areaRepository.findAll();
        return new ResponseResult(ResultState.SUCCESS,"查询诊区列表成功",areaList);
    }

    @PostMapping("/getPlans")
    public ResponseResult getAllPlan(String[] planIds){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String format = sdf.format(new Date());
        Date date = null;
        try {
            date = sdf.parse(format);
        } catch (ParseException e) {
            e.printStackTrace();
            log.error("日期错误",e.getMessage());
            return new ResponseResult(ResultState.FAIL,"日期错误",e.getMessage());
        }
        List<Plan> list = planRepository.getAllPlans(date, planIds);
        return new ResponseResult(ResultState.SUCCESS,"成功",list);
    }

    @PostMapping("deleteAll")
    public ResponseResult deleteAll(String[] registerIds,Integer areaId){
        return registerService.deleteAll(registerIds,areaId);
    }
}
