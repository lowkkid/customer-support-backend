package com.lowkkid.github.customersupport.mapper;

import com.lowkkid.github.customersupport.dto.SupportMessageDto;
import com.lowkkid.github.customersupport.model.SupportMessage;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SupportMessageMapper {

    SupportMessageDto toDto(SupportMessage entity);

    List<SupportMessageDto> toDtoList(List<SupportMessage> entities);
}
