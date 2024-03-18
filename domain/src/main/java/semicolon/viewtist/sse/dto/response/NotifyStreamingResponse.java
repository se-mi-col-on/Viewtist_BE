package semicolon.viewtist.sse.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import semicolon.viewtist.sse.entity.Notify;

@AllArgsConstructor
@Builder
@NoArgsConstructor
@Getter
@Setter
public class NotifyStreamingResponse {

  String notifyId;
  String receiverName;
  String content;
  String type;

  public static NotifyStreamingResponse from(Notify notify) {
    return NotifyStreamingResponse.builder()
        .content(notify.getContent())
        .notifyId(notify.getId().toString())
        .receiverName(notify.getReceiver().getNickname())
        .type(notify.getNotificationType().toString())
        .build();

  }
}
