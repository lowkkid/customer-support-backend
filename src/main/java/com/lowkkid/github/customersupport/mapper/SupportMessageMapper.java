package com.lowkkid.github.customersupport.mapper;

import com.lowkkid.github.customersupport.dto.SupportMessageDto;
import com.lowkkid.github.customersupport.domain.entity.SupportMessage;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SupportMessageMapper {

    SupportMessageDto toDto(SupportMessage entity);
}
