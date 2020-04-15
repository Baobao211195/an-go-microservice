package com.an.process.service;

import com.an.common.bean.Booking;
import com.an.process.entity.BookingEntity;
import com.an.process.repository.BookingRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    BookingRepository repository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public List<Booking> findByDriverScheduleIdAndStatus(Long driverScheduleId, String status) {
        List<Booking> output = new ArrayList<>();
        List<BookingEntity> lst = repository.findByDriverScheduleIdAndStatus(driverScheduleId, status);
        if (lst != null && !lst.isEmpty()){
            lst.forEach(x->output.add(modelMapper.map(x, Booking.class)));
        }
        return output;
    }
}
