package com.an.catalog.repository;

import com.an.catalog.entity.DailyTitleEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DailyTitleRepository extends CrudRepository<DailyTitleEntity, Long> {

    @Query("select a from DailyTitleEntity a where a.showDatetime = date(sysdate())")
    List<DailyTitleEntity> findByShowDatetime();
}
