package com.an.booking.service;

import com.an.common.bean.DriverSchedule;
import com.an.common.bean.DriverSchedulePath;
import com.an.common.exception.LogicException;

import java.util.Date;
import java.util.List;

public interface DriverScheduleService {

    DriverSchedule findById(Long driverScheduleId);

    List<DriverSchedule> findByDriverIdAndSchedule(Long driverId, Long serviceId, Date scheduleDate);

    DriverSchedule insertDriverSchedule(String driverScheduleGroupPathId, Long driverId, Long serviceId, String fromPoint, String toPoint, String strScheduleDate, Long fromHour, Long fromMinute, Long toHour, Long toMinute) throws LogicException, Exception;

    List<DriverSchedule> insertListDriverSchedule(String driverScheduleGroupPathId, List<DriverSchedule> lstDriverSchedule) throws LogicException, Exception;

    DriverSchedule updateDriverSchedule(Long driverScheduleId, String strScheduleDate, Long fromHour, Long fromMinute, Long toHour, Long toMinute, String status) throws Exception, LogicException;

    public void saveDriverSchedule(List<DriverSchedule> lstDriverSchedule, List<DriverSchedulePath> lstDriverSchedulePath) throws LogicException, Exception;

    public void addDriverSchedule(String pathGroupId, List<DriverSchedule> lstDriverSchedule) throws LogicException, Exception;

    public List<DriverSchedule> getDriverScheduleByFromPointAndScheduleDate(Long serviceId, Double x, Double y, Date fromScheduleDate, Date toScheduleDate, List<Date> lstScheduleDate, Long catchHour, Long catchMinute, int page, int size) throws Exception;

    public List<DriverSchedule> findByDriverIdAndServiceIdAndScheduleDate(Long driverId, Long serviceId, Date fromSchedule, Date toSchedule);

    public List<DriverSchedule> findByDriverIdAndServiceIdAndListScheduleDate(Long driverId, Long serviceId, List<Date> lstScheduleDate);

    public List<DriverSchedule> findByDriverIdAndServiceIdAndListScheduleDateAndTime(Long driverId, Long serviceId, List<Date> lstScheduleDate, Long fromHour, Long fromMinute, Long toHour, Long toMinute);

    public void cancelDriverSchedule(List<Long> lstDriverScheduleId) throws LogicException, Exception;
}
