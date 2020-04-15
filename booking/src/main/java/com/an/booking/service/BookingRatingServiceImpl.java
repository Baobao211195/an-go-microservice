package com.an.booking.service;

import com.an.booking.entity.BookingRatingEntity;
import com.an.booking.repository.BookingRatingRepository;
import com.an.common.bean.BookingRating;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class BookingRatingServiceImpl implements BookingRatingService {

    @Autowired
    BookingRatingRepository repository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public BookingRating findByBookingId(Long bookingId) {
        BookingRatingEntity entity = repository.findByBookingId(bookingId);
        if (Objects.nonNull(entity)){
            return modelMapper.map(entity, BookingRating.class);
        }
        return null;
    }

    @Override
    public List<BookingRating> findByDriverId(Long driverId) {
        List<BookingRating> output = new ArrayList<>();
        List<BookingRatingEntity> lst = repository.findByDriverId(driverId);
        if (lst != null && !lst.isEmpty()){
            lst.forEach(x->output.add(modelMapper.map(x, BookingRating.class)));
        }
        return output;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BookingRating createBookingRating(Long bookingId, Long driverId, Long custId, String ratingNotes, Double rating) {
        BookingRatingEntity entity = new BookingRatingEntity();
        entity.setBookingId(bookingId);
        entity.setDriverId(driverId);
        entity.setCustId(custId);
        entity.setRatingNotes(ratingNotes);
        entity.setRating(rating);
        entity.setRatingDatetime(repository.getSysdate());
        entity = repository.save(entity);
        if (Objects.nonNull(entity)){
            return modelMapper.map(entity, BookingRating.class);
        }
        return null;
    }
}
