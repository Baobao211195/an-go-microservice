package com.an.booking.service;

import com.an.booking.entity.BookingCancelEntity;
import com.an.booking.repository.BookingCancelRepository;
import com.an.common.bean.BookingCancel;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class BookingCancelServiceImpl implements BookingCancelService {

    @Autowired
    BookingCancelRepository repository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public List<BookingCancel> findByUserIdAndUserTypeAndCancelDatetime(Long userId, String userType, Date cancelDatetime) {
        List<BookingCancel> output = new ArrayList<>();
        List<BookingCancelEntity> lst = repository.findByUserIdAndUserTypeAndCancelDatetimeGreaterThanEqual(userId, userType, cancelDatetime);
        if (lst != null && !lst.isEmpty()){
            lst.forEach(x -> output.add(modelMapper.map(x,BookingCancel.class)));
        }
        return output;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BookingCancel saveBookingCancel(Long bookingId, Long userId, String userType, String reason, Date cancelDatetime) {
        BookingCancelEntity entity = new BookingCancelEntity();
        entity.setBookingId(bookingId);
        entity.setUserId(userId);
        entity.setUserType(userType);
        entity.setCancelReason(reason);
        entity.setCancelDatetime(cancelDatetime);
        entity = repository.save(entity);
        if (Objects.nonNull(entity)){
            return modelMapper.map(entity, BookingCancel.class);
        }
        return null;
    }
}
