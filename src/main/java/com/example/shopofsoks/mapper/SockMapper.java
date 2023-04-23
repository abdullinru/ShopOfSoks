package com.example.shopofsoks.mapper;

import com.example.shopofsoks.dto.SockDto;
import com.example.shopofsoks.model.Sock;
import com.example.shopofsoks.model.SockCount;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface SockMapper {

//    SockMapper INSTANCE = Mappers.getMapper(SockMapper.class);

    @Mapping(source = "color", target = "color")
    @Mapping(source = "cottonPart", target = "cottonPart")
    Sock dtoToSock(SockDto sockDto);

    @Mapping(source = "quantity", target = "count")
    SockCount dtoToSockCount(SockDto sockDto);

    @Mapping(source = "color", target = "color")
    @Mapping(source = "cottonPart", target = "cottonPart")
    @Mapping(source = "sockCount.count", target = "quantity")
    SockDto toDtoSock(Sock sock);
}
