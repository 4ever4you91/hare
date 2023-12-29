package com.example.ever.hare.mapper;

import com.example.ever.hare.entity.Option;
import com.example.ever.hare.entity.dto.OptionDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OptionMapper {
    List<OptionDTO> toOptionDTOList(List<Option> options);
}
