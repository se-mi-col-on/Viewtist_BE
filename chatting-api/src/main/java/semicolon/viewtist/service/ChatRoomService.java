package semicolon.viewtist.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import semicolon.viewtist.chatting.dto.request.ChatRoomRequest;
import semicolon.viewtist.chatting.dto.response.ChatRoomResponse;
import semicolon.viewtist.chatting.entity.ChatRoom;
import semicolon.viewtist.chatting.entity.type.Status;
import semicolon.viewtist.chatting.repository.ChatRoomRepository;
import semicolon.viewtist.chatting.exception.ChattingException;
import semicolon.viewtist.global.exception.ErrorCode;
import semicolon.viewtist.user.entity.User;
import semicolon.viewtist.user.exception.UserException;
import semicolon.viewtist.user.repository.UserRepository;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatRoomService {
  private final ChatRoomRepository chatRoomRepository;
  private final UserRepository userRepository;
  // 채팅방 상태 설정
  @Transactional
  public String setChatRoomStatus(String streamKey, String status, Authentication authentication) {
    ChatRoom chatRoom = chatRoomRepository.findByStreamKey(streamKey).orElseThrow(
        () -> new ChattingException(ErrorCode.NOT_EXIST_STREAMKEY)
    );
    User user = userRepository.findByEmail(authentication.getName()).orElseThrow(
        () -> new UserException(ErrorCode.ALREADY_EXISTS_EMAIL)
    );
    if(!user.getId().equals(chatRoom.getStreamerId())){
        throw new UserException(ErrorCode.ACCESS_DENIED);
    }
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
}
