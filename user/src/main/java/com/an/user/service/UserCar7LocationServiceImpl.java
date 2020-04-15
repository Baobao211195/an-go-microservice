package com.an.user.service;

import com.an.common.bean.UserLocation;
import com.an.common.utils.Const;
import com.an.user.entity.UserCar7LocationEntity;
import com.an.user.entity.UserCarLocationEntity;
import com.an.user.repository.UserCar7LocationRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserCar7LocationServiceImpl implements UserCar7LocationService {

    private static Logger logger = LoggerFactory.getLogger(UserCar7LocationService.class);

    @Autowired
    UserCar7LocationRepository repository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public UserCar7LocationEntity updateUserLocation(Long userId, String token, Double x, Double y) {
        UserCar7LocationEntity entity = new UserCar7LocationEntity();
        entity.setUserId(userId);
        entity.setToken(token);
        entity.setLocation(new Point(x,y));
        entity.setExpiration(Const.USER_LOCATION.TIME_TO_LIVE);
        return repository.save(entity);
    }

    @Override
    public List<UserLocation> findTop10UserByLocationAndDistance(Double x, Double y, Long distance) {
        List<UserLocation> output = new ArrayList<>();
        List<UserCar7LocationEntity> lst = repository.findTop10ByLocationNear(new Point(x, y), new Distance(distance, Metrics.KILOMETERS));
        if (lst != null && !lst.isEmpty()){
            lst.forEach(entity -> output.add(modelMapper.map(entity, UserLocation.class)));
        }
        return output;
    }

    @Override
    public UserLocation findById(Long id) {
        Optional<UserCar7LocationEntity> optional = repository.findById(id);
        if (Objects.nonNull(optional) && optional.isPresent()){
            UserCar7LocationEntity entity = optional.get();
            UserLocation userLocation = modelMapper.map(entity, UserLocation.class);
            userLocation.setX(entity.getLocation().getX());
            userLocation.setY(entity.getLocation().getY());
            return userLocation;
        }
        return null;
    }

    @Override
    public void evictById(Long id) {
        repository.deleteById(id);
    }
}
