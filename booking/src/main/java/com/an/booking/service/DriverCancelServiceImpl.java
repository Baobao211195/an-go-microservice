package com.an.booking.service;

import com.an.booking.entity.DriverCancelEntity;
import com.an.booking.repository.DriverCancelRepository;
import com.an.common.bean.Booking;
import com.an.common.bean.DriverCancel;
import com.an.common.utils.Const;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Service
public class DriverCancelServiceImpl implements DriverCancelService {
    @Autowired
    DriverCancelRepository repository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    BookingService bookingService;

    @Autowired
    KafkaTemplate<String, Booking> kafkaTemplate;

    @Override
    public DriverCancel getListDriverCancel(Long bookingId) {
        DriverCancelEntity entity = repository.findByBookingId(bookingId);
        if (Objects.nonNull(entity)){
            return modelMapper.map(entity, DriverCancel.class);
        }
        return null;
    }

    @Override
    public DriverCancel updateDriverCancelForBooking(Long bookingId, Long driverId) {
        DriverCancel driverCancel = getListDriverCancel(bookingId);
        DriverCancelEntity entity = null;
        if (Objects.nonNull(driverCancel)){
            Set<Long> lstDriver = driverCancel.getLstDriver();
            lstDriver.add(driverId);
            driverCancel.setLstDriver(lstDriver);
            entity = repository.save(modelMapper.map(driverCancel, DriverCancelEntity.class));
            if (Objects.nonNull(entity)){
                return modelMapper.map(entity, DriverCancel.class);
            }
        } else {
            entity = new DriverCancelEntity();
            entity.setBookingId(bookingId);
            Set<Long> lstDriver = new HashSet<>();
            lstDriver.add(driverId);
            entity.setLstDriver(lstDriver);
            entity.setExpiration(Const.DRIVER_CANCEL.TIME_TO_LIVE);
            entity = repository.save(entity);
            if (Objects.nonNull(entity)){
                return modelMapper.map(entity, DriverCancel.class);
            }
        }

        // resend booking to kafka
        Booking booking = bookingService.findById(bookingId);
        kafkaTemplate.send("booking", booking);

        return null;
    }

    @Override
    public void removeDriverCancel(Long bookingId) {
        repository.deleteById(bookingId);
    }
}
