package com.rc.temp.service.impl;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.rc.temp.db.PatientRegister;
import com.rc.temp.db.QPatientRegister;
import com.rc.temp.db.entity.ResponseResult;
import com.rc.temp.repository.AreaRepository;
import com.rc.temp.repository.PatientRegisterRepository;
import com.rc.temp.repository.PlanRepository;
import com.rc.temp.service.RegisterService;
import com.rc.temp.setting.RedisCache;
import com.rc.temp.setting.ResultState;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Log4j2
@Service
public class RegisterServiceImpl implements RegisterService {

    @Autowired
    PatientRegisterRepository registerRepository;

    @Autowired
    PlanRepository planRepository;

    @Autowired
    AreaRepository areaRepository;

    @Autowired
    RedisCache redisCache;

    private final QPatientRegister qPatientRegister = QPatientRegister.patientRegister;

    @Override
    public ResponseResult tempRegister(PatientRegister patientRegister,Integer areaId) {
        if(areaId != null){
            String registerId = UUID.randomUUID().toString().replaceAll("-", "");
            patientRegister.setRegisterId(registerId);
            patientRegister.setSource(String.valueOf(areaId));
            patientRegister.setAutoSignIn(true);
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = sdf.parse(sdf.format(new Date()));
                patientRegister.setRegisterTime(date);
            } catch (ParseException e) {
                e.printStackTrace();
                log.error("日期转换异常",e.getMessage());
                return new ResponseResult(ResultState.FAIL,"日期转换异常",e.getMessage());
            }
            try {
                registerRepository.save(patientRegister);
                log.info("患者信息登记成功："+patientRegister.getName());
            }catch (Exception e){
                e.printStackTrace();
                log.error("登记患者临时挂号信息失败",e.getMessage());
                throw new RuntimeException("登记患者临时挂号信息失败");
            }

        }
        return new ResponseResult(ResultState.SUCCESS,"登记患者临时挂号信息成功");
    }

    @Override
    public ResponseResult tempRedisCache(PatientRegister patientRegister,Integer areaId) {
        String name = patientRegister.getName();
        String cardNo = patientRegister.getCardNo();
        String idNo = patientRegister.getIdNo();
        String shift = patientRegister.getShift();
        String planId = patientRegister.getPlanId();
        Predicate predicate = qPatientRegister.source.eq(String.valueOf(areaId));
        if(StringUtils.hasText(name)){
            predicate = ExpressionUtils.and(predicate,qPatientRegister.name.like("%"+name+"%"));
        }
        if(StringUtils.hasText(cardNo)){
            predicate = ExpressionUtils.and(predicate,qPatientRegister.cardNo.like("%"+cardNo+"%"));
        }
        if(StringUtils.hasText(idNo)){
            predicate = ExpressionUtils.and(predicate,qPatientRegister.idNo.like("%"+idNo+"%"));
        }
        if(StringUtils.hasText(shift)){
            predicate = ExpressionUtils.and(predicate,qPatientRegister.shift.eq(shift));
        }
        if(StringUtils.hasText(planId)){
            predicate = ExpressionUtils.and(predicate,qPatientRegister.planId.eq(planId));
        }
        List<PatientRegister> registers = (List<PatientRegister>) registerRepository.findAll(predicate);
        registers.stream().forEach(item -> {
            Integer existQueue = registerRepository.existQueue(item.getRegisterId());
            if (existQueue != null){
                item.setMedId("已签到");
                try {
                    registerRepository.save(item);
                    log.info("更新签到状态成功："+item.getName()+":"+item.getMedId());
                }catch (Exception e){
                    log.error("更新签到状态失败",e.getMessage());
                    e.printStackTrace();
                }
            }
        });
        return new ResponseResult(ResultState.SUCCESS,"查询缓存患者信息成功",registers);
    }

    @Override
    public ResponseResult updateTempRegister(PatientRegister patientRegister, Integer areaId) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = sdf.parse(sdf.format(new Date()));
            patientRegister.setRegisterTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
            log.error("日期转换异常",e.getMessage());
            return new ResponseResult(ResultState.FAIL,"日期转换异常",e.getMessage());
        }
        patientRegister.setAutoSignIn(true);
        try {
            registerRepository.save(patientRegister);
            return new ResponseResult(ResultState.SUCCESS,"编辑成功",null);
        }catch (Exception e){
            e.printStackTrace();
            log.error("编辑错误",e.getMessage());
            return new ResponseResult(ResultState.FAIL,"编辑失败",e.getMessage());
        }
    }

    @Override
    public ResponseResult deleteTempCache(String registerId, Integer areaId) {
        Integer existQueue = registerRepository.existQueue(registerId);
        if (existQueue != null){
            return new ResponseResult(ResultState.WARNING,"该临时挂号信息已用于签到，不可删除！！！");
        }
        try {
            registerRepository.deleteById(registerId);
            log.info("删除临时挂号信息成功！！！");
            return new ResponseResult(ResultState.SUCCESS,"删除成功",null);
        }catch (Exception e){
            e.printStackTrace();
            log.error("删除临时挂号信息失败！！！");
            return new ResponseResult(ResultState.FAIL,"删除失败",e.getMessage());
        }
    }

    @Override
    public ResponseResult deleteAll(String[] registerIds, Integer areaId) {
        try {
            registerRepository.deleteAllById(Arrays.asList(registerIds));
            return new ResponseResult(ResultState.SUCCESS,"删除所有已选患者信息成功",null);
        }catch (Exception e){
            log.info("删除操作异常",e.getMessage());
            e.printStackTrace();
            return new ResponseResult(ResultState.FAIL,"删除所有已选患者信息失败",e.getMessage());
        }
    }
}
