package com.example.bastionmiddleware.maurabastion.api;

import com.example.bastionmiddleware.dto.BastionDto;
import com.example.bastionmiddleware.dto.BastionRequest;
import com.example.bastionmiddleware.dto.BastionResponse;
import com.example.bastionmiddleware.dto.FacilityDto;
import com.example.bastionmiddleware.dto.NewspaperDataDto;
import com.example.bastionmiddleware.dto.UserDto;

import java.util.List;

public interface BastionApi {
    BastionResponse createBastion(BastionRequest request);

    BastionResponse getBastion(Long id);

    UserDto createUser(UserDto request);

    UserDto getUser(String id);

    List<BastionDto> getAllBastions();

    BastionDto getBastionById(String id);

    BastionDto createBastion(BastionDto request);

    BastionDto updateBastion(String id, BastionDto request);

    void deleteBastion(String id);

    List<FacilityDto> getAllFacilities();

    FacilityDto getFacilityById(String id);

    NewspaperDataDto createNewspaper(NewspaperDataDto request);

    List<NewspaperDataDto> getAllNewspapers();

    NewspaperDataDto getNewspaperByName(String newspaperName);
}
