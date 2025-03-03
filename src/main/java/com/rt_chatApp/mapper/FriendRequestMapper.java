package com.rt_chatApp.mapper;

import com.rt_chatApp.dto.FriendRequestDto;
import com.rt_chatApp.model.FriendRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface FriendRequestMapper {

    FriendRequestMapper INSTANCE = Mappers.getMapper(FriendRequestMapper.class);

    @Mapping(source = "sender.uniqueIdentifier", target = "senderName")
    @Mapping(source = "receiver.uniqueIdentifier", target = "receiverName")
    FriendRequestDto toDto(FriendRequest friendRequest);

    List<FriendRequestDto> toDtoList(List<FriendRequest> friendRequests);
}
