package com.an.common.logger;

import com.google.gson.Gson;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

@Aspect
@Component
public class SpringAOPHandle {

    private Gson gson = new Gson();

    private Logger logger = LoggerFactory.getLogger("ApiService");

    @Around("@annotation(com.an.common.logger.LogsActivityAnnotation)")
    public Object logsActivityAnnotation(ProceedingJoinPoint point) throws Throwable {

        // set dataResponse
        Object objectResponse = point.proceed();

        try {
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            Date startTime = calendar.getTime();

            // get dataRequest
            Object[] objectRequest = point.getArgs();
            String input = "";
            if (objectRequest != null && objectRequest.length > 0){
                for (Object request : objectRequest){
                    if (Objects.nonNull(request)){
                        input += "," + gson.toJson(request);
                    }
                }
                if (!input.isEmpty()){
                    input = input.substring(1);
                }
            }

            MethodSignature signature = (MethodSignature) point.getSignature();
            Method m = signature.getMethod();
            LogsActivityAnnotation customAnnotation = m.getAnnotation(LogsActivityAnnotation.class);
            String actionName = customAnnotation.actionName();
            if (StringUtils.isEmpty(actionName)){
                actionName = m.getName();
            }
            Date endTime = calendar.getTime();

            String output = Objects.nonNull(objectResponse) ? gson.toJson(objectResponse) : "";

//            System.out.println(String.format("%s|%s|%s|%s|%s|%s", actionName, input, output, simpleDateFormat.format(startTime), simpleDateFormat.format(endTime), endTime.getTime()-startTime.getTime()));
            logger.info(String.format("%s|%s|%s|%s|%s|%s", actionName, input, output, simpleDateFormat.format(startTime), simpleDateFormat.format(endTime), endTime.getTime()-startTime.getTime()));

        } catch (Exception ex){
            logger.error("logger error", ex.getMessage());
        }

        // continue response
        return objectResponse;
    }
}
