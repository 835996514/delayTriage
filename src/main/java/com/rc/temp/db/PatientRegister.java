package com.rc.temp.db;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "triage_patient_register")
@NoArgsConstructor
@AllArgsConstructor
public class PatientRegister implements Serializable {

    @Id
    @Column(
            name = "REGISTER_ID"
    )
    private String registerId;
    @Column(
            name = "PLAN_ID"
    )
    private String planId;
    @Column(
            name = "MED_ID"
    )
    private String medId;
    @Column(
            name = "NAME"
    )
    private String name;
    @Column(
            name = "CARD_NO"
    )
    private String cardNo;
    @Column(
            name = "AGE"
    )
    private Integer age;
    @Column(
            name = "SEX"
    )
    private String sex;
    @Column(
            name = "DEPARTMENT_CODE"
    )
    private String departmentCode;
    @Column(
            name = "DEPARTMENT_NAME"
    )
    private String departmentName;
    @Column(
            name = "SOURCE"
    )
    private String source;
    @Column(
            name = "ID_NO"
    )
    private String idNo;
    @Column(
            name = "DOCTOR_CODE"
    )
    private String doctorCode;
    @Column(
            name = "DOCTOR_NAME"
    )
    private String doctorName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(
            name = "REGISTER_TIME"
    )
    private Date registerTime;
    @Column(
            name = "JOB"
    )
    private String job;
    @Column(
            name = "SHIFT"
    )
    private String shift;
    @Column(
            name = "REGISTER_NUM"
    )
    private Integer registerNum;
    @Column(
            name = "LEVEL"
    )
    private String level;
    @Column(
            name = "CANCEL_FLAG"
    )
    private Boolean cancelFlag;
    @Column(
            name = "TRANSFER_PLAN_ID"
    )
    private String transferPlanId;
    @Column(
            name = "OUTSIDE_TRANSFER"
    )
    private Boolean outsideTransfer;
    @Column(
            name = "LATEST_SIGN_IN_TIME"
    )
    private Date latestSignInTime;
    @Column(
            name = "IS_AUTO_SIGN_IN"
    )
    private boolean isAutoSignIn;
    @Column(
            name = "NEXT_SIGN_IN_REGISTER_ID"
    )
    private String nextSignInRegisterId;
    @Column(
            name = "APPOINTMENT_BEGIN_TIME"
    )
    private Date appointmentBeginTime;
    @Column(
            name = "APPOINTMENT_END_TIME"
    )
    private Date appointmentEndTime;
}
