package semicolon.viewtist.service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import semicolon.viewtist.chatting.dto.request.ChatRoomRequest;
import semicolon.viewtist.chatting.dto.response.ChatRoomResponse;
import semicolon.viewtist.chatting.entity.ChatRoom;
import semicolon.viewtist.chatting.entity.type.Status;
import semicolon.viewtist.chatting.repository.ChatRoomRepository;
import semicolon.viewtist.exception.ChattingException;
import semicolon.viewtist.global.exception.ErrorCode;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatRoomService {
  private final ChatRoomRepository chatRoomRepository;

  @Transactional
  public void createRoom(ChatRoomRequest request) {
    if(chatRoomRepository.existsByStreamKey(request.getStreamKey())){
      throw new ChattingException(ErrorCode.ALREADY_EXIST_STREAMKEY);
    }
    if(chatRoomRepository.existsByStreamerId(request.getStreamerId())){
      throw new ChattingException(ErrorCode.ALREADY_CREATE_ANOTHER_ROOM);
    }
    ChatRoom chatRoom = chatRoomRepository.save(ChatRoom.from(request));
    setStatus(chatRoom,true);
  }
  // 채팅방 상태 설정
  @Transactional
  public String setChatRoomStatus(String streamKey, String status) {
    ChatRoom chatRoom = chatRoomRepository.findByStreamKey(streamKey).orElseThrow(
        () -> new ChattingException(ErrorCode.NOT_EXIST_STREAMKEY)
    );
    if(status.equals(Status.ON.toString())){
      setStatus(chatRoom,true);
    }else{
      setStatus(chatRoom,false);
    }
    return status;
  }
  private void setStatus(ChatRoom chatRoom, boolean status){
    chatRoom.setChatRoomActivate(status);
  }
  public Page<ChatRoomResponse> findActivatedRoom(Pageable pageable) {
    Page<ChatRoom> activatedRooms = chatRoomRepository.findByActiveIsTrue(pageable);
    return activatedRooms.map(ChatRoomResponse::from);
  }
}
