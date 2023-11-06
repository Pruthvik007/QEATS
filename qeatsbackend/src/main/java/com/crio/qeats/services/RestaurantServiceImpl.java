
/*
 *
 *  * Copyright (c) Crio.Do 2019. All rights reserved
 *
 */

package com.crio.qeats.services;

import lombok.extern.log4j.Log4j2;
import java.time.LocalTime;
import java.util.List;
import com.crio.qeats.dto.Restaurant;
import com.crio.qeats.exchanges.GetRestaurantsRequest;
import com.crio.qeats.exchanges.GetRestaurantsResponse;
import com.crio.qeats.repositoryservices.RestaurantRepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class RestaurantServiceImpl implements RestaurantService {

  private final Double peakHoursServingRadiusInKms = 3.0;
  private final Double normalHoursServingRadiusInKms = 5.0;

  @Autowired
  private RestaurantRepositoryService restaurantRepositoryService;

  @Override
  public GetRestaurantsResponse findAllRestaurantsCloseBy(
      GetRestaurantsRequest getRestaurantsRequest, LocalTime currentTime) {
      List<Restaurant> restaurants = restaurantRepositoryService.findAllRestaurantsCloseBy(getRestaurantsRequest.getLatitude(), getRestaurantsRequest.getLongitude(), currentTime, getServingRadius(currentTime));
      return new GetRestaurantsResponse(restaurants);
  }

  private Double getServingRadius(LocalTime currentTime){
    return isPeakHours(currentTime) ? peakHoursServingRadiusInKms : normalHoursServingRadiusInKms;
  }

  private boolean isPeakHours(LocalTime currentTime){
    return ((currentTime.getHour()>=8 && currentTime.getHour()<=10) || 
      (currentTime.getHour()>=13 && currentTime.getHour()<=14) || 
      (currentTime.getHour()>=19 && currentTime.getHour()<=21));
  }

}

