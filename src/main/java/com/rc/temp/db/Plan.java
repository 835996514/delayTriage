package com.rc.temp.db;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "triage_plan")
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Plan implements Serializable {

    @Id
    @Column(name = "PLAN_ID")
    private String planId;

    @Column(name = "DEPARTMENT_CODE")
    private String departmentCode;

    @Column(name = "NAME")
    private String name;

    @Column(name = "SHIFT")
    private String shift;

    @Column(name = "DATE")
    private Date date;

    @Column(name = "TYPE")
    private String type;

    public Plan(String planId,String name){
        this.planId = planId;
        this.name = name;
    }
}
