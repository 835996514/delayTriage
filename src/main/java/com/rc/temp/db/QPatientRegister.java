package com.rc.temp.db;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QPatientRegister is a Querydsl query type for PatientRegister
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QPatientRegister extends EntityPathBase<PatientRegister> {

    private static final long serialVersionUID = 1813091170L;

    public static final QPatientRegister patientRegister = new QPatientRegister("patientRegister");

    public final NumberPath<Integer> age = createNumber("age", Integer.class);

    public final DateTimePath<java.util.Date> appointmentBeginTime = createDateTime("appointmentBeginTime", java.util.Date.class);

    public final DateTimePath<java.util.Date> appointmentEndTime = createDateTime("appointmentEndTime", java.util.Date.class);

    public final BooleanPath cancelFlag = createBoolean("cancelFlag");

    public final StringPath cardNo = createString("cardNo");

    public final StringPath departmentCode = createString("departmentCode");

    public final StringPath departmentName = createString("departmentName");

    public final StringPath doctorCode = createString("doctorCode");

    public final StringPath doctorName = createString("doctorName");

    public final StringPath idNo = createString("idNo");

    public final BooleanPath isAutoSignIn = createBoolean("isAutoSignIn");

    public final StringPath job = createString("job");

    public final DateTimePath<java.util.Date> latestSignInTime = createDateTime("latestSignInTime", java.util.Date.class);

    public final StringPath level = createString("level");

    public final StringPath medId = createString("medId");

    public final StringPath name = createString("name");

    public final StringPath nextSignInRegisterId = createString("nextSignInRegisterId");

    public final BooleanPath outsideTransfer = createBoolean("outsideTransfer");

    public final StringPath planId = createString("planId");

    public final StringPath registerId = createString("registerId");

    public final NumberPath<Integer> registerNum = createNumber("registerNum", Integer.class);

    public final DateTimePath<java.util.Date> registerTime = createDateTime("registerTime", java.util.Date.class);

    public final StringPath sex = createString("sex");

    public final StringPath shift = createString("shift");

    public final StringPath source = createString("source");

    public final StringPath transferPlanId = createString("transferPlanId");

    public QPatientRegister(String variable) {
        super(PatientRegister.class, forVariable(variable));
    }

    public QPatientRegister(Path<? extends PatientRegister> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPatientRegister(PathMetadata metadata) {
        super(PatientRegister.class, metadata);
    }

}

