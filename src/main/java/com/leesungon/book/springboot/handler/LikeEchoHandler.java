package com.leesungon.book.springboot.handler;

import com.leesungon.book.springboot.config.auth.LoginUser;
import com.leesungon.book.springboot.config.auth.dto.SessionUser;
import com.leesungon.book.springboot.domain.posts.user.User;
import com.nimbusds.oauth2.sdk.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.servlet.http.HttpSession;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class LikeEchoHandler extends TextWebSocketHandler {

    Map<String, WebSocketSession> userSessions = new HashMap<>();


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception{
        System.out.println("afterConnectionEstablished" + session);
        String senderId = getId(session);
        userSessions.put(senderId, session);
    }

    public String getId(WebSocketSession session){
        Map<String, Object> attributes = session.getAttributes();
        SessionUser user = (SessionUser) attributes.get("user");
        return user.getName();
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception{
        System.out.println("handleTextMessage" + session + " : " + message);

        String msg = message.getPayload();
        if(StringUtils.isNotBlank(msg)){
            String [] strs = msg.split(",");
            if(strs != null && strs.length == 3){
                String user = strs[0];
                String bno = strs[1];
                String title = strs[2];

                WebSocketSession postLikeSession = userSessions.get(user);
                if(postLikeSession != null){
                    TextMessage textMessage = new TextMessage(user + "님이" + "<a href='/posts/detail/"+ bno + "'>" + title + "게시글에 좋아요를 눌렀습니다");
                    postLikeSession.sendMessage(textMessage);
                }
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception{
        System.out.println("afterConnectionClosed" + session + " : " + status);
    }
}
