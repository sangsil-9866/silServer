package com.na.silserver.domain.board.service;

import com.na.silserver.domain.board.dto.ReplyDto;
import com.na.silserver.domain.board.entity.Board;
import com.na.silserver.domain.board.entity.Reply;
import com.na.silserver.domain.board.repository.BoardRepository;
import com.na.silserver.domain.board.repository.ReplyRepository;
import com.na.silserver.domain.user.entity.User;
import com.na.silserver.domain.user.enums.UserRole;
import com.na.silserver.domain.user.repository.UserRepository;
import com.na.silserver.global.exception.CustomException;
import com.na.silserver.global.response.ResponseCode;
import com.na.silserver.global.util.UtilCommon;
import com.na.silserver.global.util.UtilMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReplyService {

    private final ReplyRepository replyRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final UtilMessage utilMessage;


    @Transactional
    public ReplyDto.Response replyCreate(String boardId, String username, ReplyDto.CreateRequest request) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new CustomException(ResponseCode.EXCEPTION_NODATA, utilMessage.getMessage("reply.notfound.board")));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(ResponseCode.EXCEPTION_NODATA, utilMessage.getMessage("reply.notfound.user")));

        Reply reply = Reply.builder()
                .board(board)
                .user(user)
                .content(request.getContent())
                .build();

        if(UtilCommon.isNotEmpty(request.getParentId())){
            Reply parent = replyRepository.findById(request.getParentId())
                    .orElseThrow(() -> new CustomException(ResponseCode.EXCEPTION_NODATA, utilMessage.getMessage("reply.notfound.parent")));
            reply.setParent(parent);
            parent.getChildren().add(reply);
        }

        Reply saved = replyRepository.save(reply);
        return toResponse(saved);
    }

    public List<ReplyDto.Response> replyList(String boardId) {
        List<Reply> replies = replyRepository.findByBoardIdOrderByCreatedAtAsc(boardId);
        Map<String, ReplyDto.Response> map = new HashMap<>();

        // 1단계: 모든 댓글을 DTO로 변환
        for(Reply reply : replies){
            ReplyDto.Response response = toResponse(reply);
            map.put(response.getId(), response);
        }

        // 2단계: 부모-자식 구조 생성
        List<ReplyDto.Response> roots = new ArrayList<>();
        for(Reply reply : replies){
            if(UtilCommon.isEmpty(reply.getParent())){
                roots.add(map.get(reply.getId()));
            } else {
                map.get(reply.getParent().getId()).getChildren().add(map.get(reply.getId()));
            }
        }
        return roots;
    }

    @Transactional
    public void replyModify(String id, String username, String content) {
        Reply reply = replyRepository.findById(id)
                .orElseThrow(() -> new CustomException(ResponseCode.EXCEPTION_NODATA, utilMessage.getMessage("reply.notfound.data")));

        if(!reply.isOwner(username) && !reply.getUser().getRole().equals(UserRole.ADMIN)){
            throw new CustomException(ResponseCode.EXCEPTION_NODATA, utilMessage.getMessage("reply.modify.auth.forbidden"));
        }

        reply.modify(content);
    }

    @Transactional
    public void replyDelete(String id, String username) {
        Reply reply = replyRepository.findById(id)
                .orElseThrow(() -> new CustomException(ResponseCode.EXCEPTION_NODATA, utilMessage.getMessage("reply.notfound.data")));

        if(!reply.isOwner(username) && !reply.getUser().getRole().equals(UserRole.ADMIN)){
            throw new CustomException(ResponseCode.EXCEPTION_NODATA, utilMessage.getMessage("reply.delete.auth.forbidden"));
        }

        replyRepository.delete(reply);
    }

    private ReplyDto.Response toResponse(Reply reply){
        return ReplyDto.Response.builder()
                .id(reply.getId())
                .content(reply.getContent())
                .username(reply.getUser().getUsername())
                .createdAt(reply.getCreatedAt())
                .modifiedAt(reply.getModifiedAt())
                .children(new ArrayList<>())
                .build();
    }

}
