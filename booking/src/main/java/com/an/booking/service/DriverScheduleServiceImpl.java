package com.an.booking.service;

import com.an.booking.entity.DriverScheduleEntity;
import com.an.booking.fcm.FCMService;
import com.an.booking.repository.DriverScheduleRepository;
import com.an.common.bean.Booking;
import com.an.common.bean.DriverSchedule;
import com.an.common.bean.DriverSchedulePath;
import com.an.common.exception.LogicException;
import com.an.common.utils.Const;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class DriverScheduleServiceImpl implements DriverScheduleService {

    @Autowired
    DriverScheduleRepository repository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    DriverSchedulePathService driverSchedulePathService;

    @Autowired
    BookingService bookingService;

    @Autowired
    FCMService fcmService;

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public DriverSchedule findById(Long driverScheduleId) {
        Optional<DriverScheduleEntity> optional = repository.findById(driverScheduleId);
        if (Objects.nonNull(optional) && optional.isPresent()){
            return modelMapper.map(optional.get(), DriverSchedule.class);
        }
        return null;
    }

    @Override
    public List<DriverSchedule> findByDriverIdAndSchedule(Long driverId, Long serviceId, Date scheduleDate) {
        List<DriverSchedule> output = new ArrayList<>();
        List<DriverScheduleEntity> lst = repository.findByDriverIdAndServiceIdAndScheduleDateGreaterThan(driverId, serviceId, scheduleDate);
        if (lst != null && !lst.isEmpty()){
            lst.forEach(x->output.add(modelMapper.map(x, DriverSchedule.class)));
        }
        return output;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DriverSchedule insertDriverSchedule(String driverScheduleGroupPathId, Long driverId, Long serviceId, String fromPoint, String toPoint, String strScheduleDate, Long fromHour, Long fromMinute, Long toHour, Long toMinute) throws LogicException, Exception{
        DriverScheduleEntity entity = new DriverScheduleEntity();
        entity.setDriverSchedulePathGroupId(driverScheduleGroupPathId);
        entity.setDriverId(driverId);
        entity.setServiceId(serviceId);
        entity.setFromPoint(fromPoint);
        entity.setToPoint(toPoint);
        Date scheduleDate;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMyyyy");
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            scheduleDate = simpleDateFormat.parse(strScheduleDate);
        } catch (Exception ex){
            throw new LogicException(Const.WS.NOK, "invalid schedule date");
        }
        entity.setScheduleDate(scheduleDate);
        entity.setFromHour(fromHour);
        entity.setFromMinute(fromMinute);
        entity.setToHour(toHour);
        entity.setToMinute(toMinute);
        entity.setCreateDatetime(repository.getSysdate());
        entity.setStatus(Const.DRIVER_SCHEDULE.STATUS_ON);
        entity = repository.save(entity);
        if (Objects.nonNull(entity)){
            return modelMapper.map(entity, DriverSchedule.class);
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<DriverSchedule> insertListDriverSchedule(String driverScheduleGroupPathId, List<DriverSchedule> lstDriverSchedule) throws LogicException, Exception{
        List<DriverSchedule> output = new ArrayList<>();
        if(lstDriverSchedule != null && !lstDriverSchedule.isEmpty()){
            for (DriverSchedule x : lstDriverSchedule){
                try {
                    insertDriverSchedule(driverScheduleGroupPathId, x.getDriverId(), x.getServiceId(), x.getFromPoint(), x.getToPoint(), x.getStrScheduleDate(), x.getFromHour(), x.getFromMinute(), x.getToHour(), x.getToMinute());
                } catch (Exception e) {
                    throw e;
                }
            }
        }
        return output;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DriverSchedule updateDriverSchedule(Long driverScheduleId, String strScheduleDate, Long fromHour, Long fromMinute, Long toHour, Long toMinute, String status) throws Exception, LogicException {
        Optional<DriverScheduleEntity> optional = repository.findById(driverScheduleId);
        if (Objects.nonNull(optional) && optional.isPresent()){
            DriverScheduleEntity entity = optional.get();
            if (!StringUtils.isEmpty(strScheduleDate) ){
                Date scheduleDate = null;
                try {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMyyyy");
                    simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                    scheduleDate = simpleDateFormat.parse(strScheduleDate);
                } catch (Exception ex) {
                    throw new LogicException(Const.WS.NOK, "invalid schedule date");
                }
                if (!Objects.equals(scheduleDate, entity.getScheduleDate())) {
                    entity.setScheduleDate(scheduleDate);
                }
            }
            if (Objects.nonNull(fromHour) && !Objects.equals(fromHour, entity.getFromHour())){
                entity.setFromHour(fromHour);
            }
            if (Objects.nonNull(fromMinute) && !Objects.equals(fromMinute, entity.getFromMinute())){
                entity.setFromMinute(fromMinute);
            }
            if (Objects.nonNull(toHour) && !Objects.equals(toHour, entity.getToHour())){
                entity.setToHour(toHour);
            }
            if (Objects.nonNull(toMinute) && !Objects.equals(toMinute, entity.getToMinute())){
                entity.setToMinute(toMinute);
            }
            if (Objects.nonNull(status) && !Objects.equals(status, entity.getStatus())){
                entity.setStatus(status);
            }
            entity = repository.save(entity);
            if (Objects.nonNull(entity)){
                return modelMapper.map(entity, DriverSchedule.class);
            }
        } else {
            throw new LogicException(Const.WS.NOK, "invalid driver schedule id");
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveDriverSchedule(List<DriverSchedule> lstDriverSchedule, List<DriverSchedulePath> lstDriverSchedulePath) throws LogicException, Exception {
        // generate path group id
        String pathGroupId = UUID.randomUUID().toString();
        // save driver schedule
        insertListDriverSchedule(pathGroupId, lstDriverSchedule);
        // save driver path
        driverSchedulePathService.insertListDriverSchedulePath(pathGroupId, lstDriverSchedulePath);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addDriverSchedule(String pathGroupId, List<DriverSchedule> lstDriverSchedule) throws LogicException, Exception {
        // add driver schedule
        insertListDriverSchedule(pathGroupId, lstDriverSchedule);
    }

    @Override
    public List<DriverSchedule> getDriverScheduleByFromPointAndScheduleDate(Long serviceId, Double x, Double y, Date fromScheduleDate, Date toScheduleDate, List<Date> lstScheduleDate, Long catchHour, Long catchMinute, int page, int size) throws Exception {
        List<DriverSchedule> output = new ArrayList<>();
        String sql = "select a from DriverScheduleEntity a " +
                " where a.serviceId = :serviceId " +
                " and exists (select b.driverSchedulePathGroupId from DriverSchedulePathEntity b where a.driverSchedulePathGroupId = b.driverSchedulePathGroupId " +
                " and 111.111 * DEGREES(ACOS(LEAST(1.0, COS(RADIANS(b.x)) * COS(RADIANS(:x)) * COS(RADIANS(b.y - :y)) + SIN(RADIANS(b.x)) * SIN(RADIANS(:x))))) < :limit)";
        Map<String, Object> map = new HashMap<>();
        if (lstScheduleDate != null && !lstScheduleDate.isEmpty()){
            sql += " and date(a.scheduleDate) in :lstScheduleDate";
            map.put("lstScheduleDate", lstScheduleDate);
        }
//        if (Objects.nonNull(fromScheduleDate)){
//           sql += " and a.scheduleDate >= :fromScheduleDate";
//           map.put("fromScheduleDate", fromScheduleDate);
//        }
//        if (Objects.nonNull(toScheduleDate)){
//           sql += " and a.scheduleDate <= :toScheduleDate";
//           map.put("toScheduleDate", toScheduleDate);
//        }
        if (Objects.nonNull(catchHour)){
            sql += " and a.fromHour <= :catchHour";
            sql += " and a.toHour >= :catchHour";
            map.put("catchHour", catchHour);
        }
//        if (Objects.nonNull(toHour)){
//            sql += " and a.fromHour <= :toHour";
//            map.put("toHour", toHour);
//        }
        if (Objects.nonNull(catchMinute)){
            sql += " and a.fromMinute <= :catchMinute";
            map.put("catchMinute", catchMinute);
        }
//        if (Objects.nonNull(toMinute)){
//            sql += " and a.fromMinute <= :toMinute";
//            map.put("toMinute", toMinute);
//        }
        if (Objects.equals(Const.SERVICE.SHARE_BIKE, serviceId)){
            sql += " and not exists (select c.driverScheduleId from BookingEntity c where a.driverScheduleId = c.driverScheduleId and c.bookingDatetime >= :fromScheduleDate and c.bookingDatetime <= :toScheduleDate )";
            map.put("fromScheduleDate", fromScheduleDate);
            map.put("toScheduleDate", toScheduleDate);
        } else if (Objects.equals(Const.SERVICE.SHARE_CAR, serviceId)){
            sql += " and not exists (select count(c.driverScheduleId) from BookingEntity c where a.driverScheduleId = c.driverScheduleId and c.bookingDatetime >= :fromScheduleDate and c.bookingDatetime <= :toScheduleDate group by c.driverScheduleId having count(driverScheduleId) >= 3)";
            map.put("fromScheduleDate", fromScheduleDate);
            map.put("toScheduleDate", toScheduleDate);
        } else if (Objects.equals(Const.SERVICE.SHARE_CAR7, serviceId)){
            sql += " and not exists (select count(c.driverScheduleId) from BookingEntity c where a.driverScheduleId = c.driverScheduleId and c.bookingDatetime >= :fromScheduleDate and c.bookingDatetime <= :toScheduleDate group by c.driverScheduleId having count(driverScheduleId) >= 6)";
            map.put("fromScheduleDate", fromScheduleDate);
            map.put("toScheduleDate", toScheduleDate);
        }
//        sql += " group by a.driverId " +
//                " order by count(a.driverId) desc";
        Query query = entityManager.createQuery(sql);
        query.setParameter("serviceId", serviceId);
        query.setParameter("x", x);
        query.setParameter("y", y);
        query.setParameter("limit", Const.SHARE_LOCATION.DEFAULT_DISTANCE);
        if (!map.isEmpty()){
            map.entrySet().forEach(entry->query.setParameter(entry.getKey(), entry.getValue()));
        }
        query.setMaxResults(size);
        query.setFirstResult(page*size);
        List<DriverScheduleEntity> lst = query.getResultList();
        if (lst != null && !lst.isEmpty()){
            lst.forEach(entity->output.add(modelMapper.map(entity, DriverSchedule.class)));
        }
        return output;
    }

    @Override
    public List<DriverSchedule> findByDriverIdAndServiceIdAndScheduleDate(Long driverId, Long serviceId, Date fromSchedule, Date toSchedule) {
        List<DriverSchedule> output = new ArrayList<>();

        String sql = " select a from DriverScheduleEntity a where a.driverId = :driverId and a.serviceId = :serviceId " +
                " and date(a.scheduleDate) >= date(:fromSchedule) ";
        if (Objects.nonNull(toSchedule)) {
            sql += " and date(a.scheduleDate) <= date(:toSchedule) ";
        }
        sql += " order by a.driverSchedulePathGroupId, a.scheduleDate";
        Query query = entityManager.createQuery(sql);
        query.setParameter("driverId", driverId);
        query.setParameter("serviceId", serviceId);
        query.setParameter("fromSchedule", new SimpleDateFormat("yyyyMMdd").format(fromSchedule));
        if (Objects.nonNull(toSchedule)) {
            query.setParameter("toSchedule", new SimpleDateFormat("yyyyMMdd").format(toSchedule));
        }
        List<DriverScheduleEntity> lst = query.getResultList();
        if (lst != null && !lst.isEmpty()){
            lst.forEach(entity->output.add(modelMapper.map(entity, DriverSchedule.class)));
        }
        return output;
    }

    @Override
    public List<DriverSchedule> findByDriverIdAndServiceIdAndListScheduleDate(Long driverId, Long serviceId, List<Date> lstScheduleDate) {
        List<DriverSchedule> output = new ArrayList<>();

        String sql = " select a from DriverScheduleEntity a where a.driverId = :driverId and a.serviceId = :serviceId  and date(a.scheduleDate) in :lstScheduleDate ";

        Query query = entityManager.createQuery(sql);
        query.setParameter("driverId", driverId);
        query.setParameter("serviceId", serviceId);
        query.setParameter("lstScheduleDate", lstScheduleDate);
        List<DriverScheduleEntity> lst = query.getResultList();
        if (lst != null && !lst.isEmpty()){
            lst.forEach(entity->output.add(modelMapper.map(entity, DriverSchedule.class)));
        }
        return output;
    }

    @Override
    public List<DriverSchedule> findByDriverIdAndServiceIdAndListScheduleDateAndTime(Long driverId, Long serviceId, List<Date> lstScheduleDate, Long fromHour, Long fromMinute, Long toHour, Long toMinute) {
        List<DriverSchedule> output = new ArrayList<>();

        String sql = " select a from DriverScheduleEntity a where a.status = '1' and a.driverId = :driverId and a.serviceId = :serviceId  and date(a.scheduleDate) in :lstScheduleDate " +
                " and ((a.fromHour <= :fromHour and a.toHour >= :fromHour and a.fromMinute <= :fromMinute) or (a.fromHour <= :toHour and a.toHour >= :toHour and a.toMinute >= :toMinute))";

        Query query = entityManager.createQuery(sql);
        query.setParameter("driverId", driverId);
        query.setParameter("serviceId", serviceId);
        query.setParameter("lstScheduleDate", lstScheduleDate);
        query.setParameter("fromHour", fromHour);
        query.setParameter("fromMinute", fromMinute);
        query.setParameter("toHour", toHour);
        query.setParameter("toMinute", toMinute);
        List<DriverScheduleEntity> lst = query.getResultList();
        if (lst != null && !lst.isEmpty()){
            lst.forEach(entity->output.add(modelMapper.map(entity, DriverSchedule.class)));
        }
        return output;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelDriverSchedule(List<Long> lstDriverScheduleId) throws LogicException, Exception {
        lstDriverScheduleId.forEach(x->{
            Optional<DriverScheduleEntity> optional = repository.findById(x);
            if (Objects.nonNull(optional) && optional.isPresent()){
                // update off booking
                List<Booking> lstBooking = bookingService.findByDriverScheduleId(x);
                boolean isOff = true;
                if (lstBooking != null && !lstBooking.isEmpty()){
                    for (Booking b : lstBooking) {
                        if (Objects.equals(b.getStatus(), Const.BOOKING.STATUS_FINISH)) {
                            isOff = false;
                            break;
                        } else {
                            try {
                                bookingService.cancelBooking(b.getBookingId(), b.getDriverId(), Const.USER.TYPE_DRIVER, "driver canceled booking");
                            } catch (Exception e) {
                                throw new LogicException(e.getMessage(), e.getMessage());
                            }
                        }
                    }
                }
                if (isOff) {
                    // update off schedule
                    DriverScheduleEntity entity = optional.get();
                    entity.setStatus(Const.DRIVER_SCHEDULE.STATUS_OFF);
                    repository.save(entity);
                }
            } else {
                throw new LogicException(Const.WS.NOK, "invalid driver schedule id: " + x);
            }
        });
    }
}
