package com.an.booking.service;

import com.an.booking.entity.BookingDetailEntity;
import com.an.booking.entity.BookingIssueEntity;
import com.an.booking.repository.BookingIssueRepository;
import com.an.common.bean.BookingDetail;
import com.an.common.bean.BookingIssue;
import com.an.common.utils.Const;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class BookingIssueServiceImpl implements BookingIssueService {

    @Autowired
    BookingIssueRepository repository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<BookingIssue> findByBookingId(Long bookingId) {
        List<BookingIssue> output = new ArrayList<>();
        List<BookingIssueEntity> lst = repository.findByBookingId(bookingId);
        if (lst != null && !lst.isEmpty()){
            lst.forEach(x -> modelMapper.map(x,BookingIssue.class));
        }
        return output;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BookingIssue createBookingIssue(Long bookingId, String issueContent) {
        BookingIssueEntity entity = new BookingIssueEntity();
        entity.setBookingId(bookingId);
        entity.setIssueContent(issueContent);
        entity.setIssueDatetime(repository.getSysdate());
        entity.setStatus(Const.BOOKING_ISSUE.STATUS_ON);
        entity = repository.save(entity);
        if (Objects.nonNull(entity)){
            return modelMapper.map(entity, BookingIssue.class);
        }
        return null;
    }
}
