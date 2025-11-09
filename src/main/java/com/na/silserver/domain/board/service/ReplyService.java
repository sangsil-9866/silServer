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

    /**
     * 댓글목록
     * @param boardId
     * @return
     */
    public List<ReplyDto.Response> replyList(String boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new CustomException(ResponseCode.EXCEPTION_NODATA_BOARD, utilMessage.getMessage("reply.notfound.board")));

        Map<String, ReplyDto.Response> map = new HashMap<>();

        List<Reply> replies = board.getReplies();

        // 1단계: 모든 댓글을 DTO로 변환
        for(Reply reply : replies){
            ReplyDto.Response response = ReplyDto.Response.toDto(reply);
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

    /**
     * 댓글등록
     * @param boardId
     * @param username
     * @param request
     * @return
     */
    @Transactional
    public ReplyDto.Response replyCreate(String boardId, String username, ReplyDto.CreateRequest request) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new CustomException(ResponseCode.EXCEPTION_NODATA_BOARD, utilMessage.getMessage("reply.notfound.board")));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(ResponseCode.EXCEPTION_NODATA_USER, utilMessage.getMessage("reply.notfound.user")));

        // Entity 생성
        Reply reply = request.toEntity();
        reply.setUser(user);

        // 상위가 있다면 추가 저장
        if(UtilCommon.isNotEmpty(request.getParentId())){
            Reply parent = replyRepository.findById(request.getParentId())
                    .orElseThrow(() -> new CustomException(ResponseCode.EXCEPTION_NODATA_PARENT, utilMessage.getMessage("reply.notfound.parent")));
            reply.setParent(parent);
            parent.getChildren().add(reply);
        }

        board.getReplies().add(reply);
        Board savedBoard = boardRepository.saveAndFlush(board);
        // DB INSERT 후 ID를 반영해야 하는데 컬렉션에 추가된 자식(Reply)은 Board를 통해 persist 되었기 때문에,
        // reply 객체 자체에는 즉시 ID가 채워지지 않을 수 있음.
        Reply savedReply = savedBoard.getReplies().get(savedBoard.getReplies().size() - 1);
        return ReplyDto.Response.toDto(savedReply);
    }

    /**
     * 댓글수정
     * @param id
     * @param username
     * @param request
     */
    @Transactional
    public void replyModify(String id, String username, ReplyDto.ModifyRequest request) {
        Reply reply = replyRepository.findById(id)
                .orElseThrow(() -> new CustomException(ResponseCode.EXCEPTION_NODATA, utilMessage.getMessage("reply.notfound.data")));

        if(!reply.isOwner(username) && !reply.getUser().getRole().equals(UserRole.ADMIN)){
            throw new CustomException(ResponseCode.DATA_AUTH_FORBIDDEN, utilMessage.getMessage("reply.modify.auth.forbidden"));
        }
        reply.modify(request);
    }

    /**
     * 댓글삭제
     * @param id
     * @param username
     */
    @Transactional
    public void replyDelete(String id, String username) {
        Reply reply = replyRepository.findById(id)
                .orElseThrow(() -> new CustomException(ResponseCode.EXCEPTION_NODATA, utilMessage.getMessage("reply.notfound.data")));

        if(!reply.isOwner(username) && !reply.getUser().getRole().equals(UserRole.ADMIN)){
            throw new CustomException(ResponseCode.DATA_AUTH_FORBIDDEN, utilMessage.getMessage("reply.delete.auth.forbidden"));
        }
        replyRepository.delete(reply);
    }
}
