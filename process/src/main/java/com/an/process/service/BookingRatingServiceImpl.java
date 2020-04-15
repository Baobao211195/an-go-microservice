package com.an.process.service;

import com.an.common.bean.BookingRating;
import com.an.process.entity.BookingRatingEntity;
import com.an.process.repository.BookingRatingRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class BookingRatingServiceImpl implements BookingRatingService {

    private static Logger logger = LoggerFactory.getLogger(BookingRatingService.class);

    @Autowired
    BookingRatingRepository repository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public List<BookingRating> findByDriverIdAndRatingNotNull(Long driverId) {
        List<BookingRating> output = new ArrayList<>();
        List<BookingRatingEntity> lst = repository.findByDriverIdAndRatingNotNull(driverId);
        if (Objects.nonNull(lst) && !lst.isEmpty()){
            lst.forEach(x->output.add(modelMapper.map(x, BookingRating.class)));
        }
        return output;
    }

    @Override
    public List<BookingRating> findAvgRating() {
        List<BookingRating> output = new ArrayList<>();
        List<BookingRatingEntity> lst = repository.findAvgRating();
        if (Objects.nonNull(lst) && !lst.isEmpty()){
            lst.forEach(x->output.add(modelMapper.map(x, BookingRating.class)));
        }
        return output;
    }
}
