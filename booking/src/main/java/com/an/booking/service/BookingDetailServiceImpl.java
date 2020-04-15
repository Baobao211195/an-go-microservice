package com.an.booking.service;

import com.an.booking.entity.BookingDetailEntity;
import com.an.booking.repository.BookingDetailRepository;
import com.an.common.bean.BookingDetail;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class BookingDetailServiceImpl implements BookingDetailService {

    @Autowired
    BookingDetailRepository repository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public List<BookingDetail> findByBookingId(Long bookingId) {
        List<BookingDetail> output = new ArrayList<>();
        List<BookingDetailEntity> lst = repository.findByBookingId(bookingId);
        if (lst != null && !lst.isEmpty()){
            lst.forEach(x -> modelMapper.map(x,BookingDetail.class));
        }
        return output;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BookingDetail createBookingDetail(Long bookingId, String name, Long quantity, Long fee) {
        BookingDetailEntity entity = new BookingDetailEntity();
        entity.setBookingId(bookingId);
        entity.setName(name);
        entity.setQuantity(quantity);
        entity.setFee(fee);
        entity = repository.save(entity);
        if (Objects.nonNull(entity)){
            return modelMapper.map(entity, BookingDetail.class);
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<BookingDetail> createListBookingDetail(Long bookingId, List<BookingDetail> lstBookingDetail) {
        List<BookingDetail> output = new ArrayList<>();
        if (lstBookingDetail != null && !lstBookingDetail.isEmpty()){
            lstBookingDetail.forEach( x -> {
                output.add(createBookingDetail(bookingId, x.getName(), x.getQuantity(), x.getFee()));
            });
        }
        return output;
    }
}
