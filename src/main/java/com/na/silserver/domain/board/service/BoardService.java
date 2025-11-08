package com.na.silserver.domain.board.service;

import com.na.silserver.domain.board.dto.BoardDto;
import com.na.silserver.domain.board.dto.BoardFileDto;
import com.na.silserver.domain.board.entity.Board;
import com.na.silserver.domain.board.entity.BoardFile;
import com.na.silserver.domain.board.repository.BoardFileRepository;
import com.na.silserver.domain.board.repository.BoardRepository;
import com.na.silserver.global.exception.CustomException;
import com.na.silserver.global.response.ResponseCode;
import com.na.silserver.global.util.UtilFile;
import com.na.silserver.global.util.UtilMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {
    private final BoardRepository boardRepository;
    private final BoardFileRepository boardFileRepository;
    private final UtilMessage utilMessage;

    // BOARD 게시판
    @Value("${custom.file.board.dir}") private String FILE_BOARD_DIR;
    @Value("${custom.file.board.path}") private String FILE_BOARD_PATH;

    public Page<BoardDto.Response> boardList(BoardDto.Search search) {
        // Search 정보로 Pageable 객체 생성
        Pageable pageable = PageRequest.of(
                search.getPage(),
                search.getSize(),
                search.isDesc() ? Sort.Direction.DESC : Sort.Direction.ASC,
                search.getSortBy()
        );
        return boardRepository.searchBoards(search, pageable);
    }

    /**
     * 게시판 상세
     * @param id
     * @return
     */
    @Transactional
    public BoardDto.Response boardDetail(String id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new CustomException(ResponseCode.EXCEPTION_NODATA, utilMessage.getMessage("notfound.data", null)));
        // 조회수 증가
        board.viewsModify();
        return BoardDto.Response.toDto(board);
    }

    /**
     * 게시판 등록
     * @param request
     * @return
     * @throws IOException
     */
    @Transactional
    public BoardDto.Response boardCreate(BoardDto.CreateRequest request) throws IOException {
        Board board = request.toEntity();

        // 파일 업로드 처리
        if (request.getBoardFiles() != null) {
            // 저장폴더생성
            Path basePath = Paths.get(FILE_BOARD_DIR);
            Path uploadPath = Paths.get(FILE_BOARD_PATH);
            Path fullPath = basePath.resolve(uploadPath);
            UtilFile.makeFolders(fullPath);

            for (MultipartFile file : request.getBoardFiles()) {
                String storedFileName = UtilFile.makeUuidFileName(file.getOriginalFilename());
                // 실제파일 저장
                file.transferTo(fullPath.resolve(storedFileName).toFile());

                // 파일정보 저장
                BoardFileDto.CreateRequest boardFile = new BoardFileDto.CreateRequest();
                boardFile.setUploadPath(uploadPath.toString());
                boardFile.setOriginalFileName(file.getOriginalFilename());
                boardFile.setStoredFileName(storedFileName);
                boardFile.setFileSize(file.getSize());
                board.addBoardFile(boardFile.toEntity());
            }
        }

        return BoardDto.Response.toDto(boardRepository.save(board));
    }

    /**
     * 게시판 수정
     * @param request
     * @return
     * @throws IOException
     */
    @Transactional
    public BoardDto.Response boardModify(String id, BoardDto.ModifyRequest request) throws IOException {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new CustomException(ResponseCode.EXCEPTION_NODATA, utilMessage.getMessage("notfound.data", null)));

        // 1️⃣ 게시글 내용 수정
        board.modify(request);

        // 2️⃣ 특정 파일만 삭제
        if (request.getDeleteFileIds() != null && !request.getDeleteFileIds().isEmpty()) {
            List<BoardFile> filesToDelete = boardFileRepository.findAllById(request.getDeleteFileIds());

            Path basePath = Paths.get(FILE_BOARD_DIR);
            Path uploadPath = Paths.get(FILE_BOARD_PATH);
            Path fullPath = basePath.resolve(uploadPath);
            for (BoardFile file : filesToDelete) {
                // 실제 파일 삭제
                File realFile = new File(fullPath.resolve(file.getStoredFileName()).toUri());
                if (realFile.exists()) {
                    realFile.delete();
                }

                // 연관관계 제거
                board.getBoardFiles().remove(file);
                boardFileRepository.delete(file);
            }
        }

        // 3️⃣ 새 파일 추가
        if (request.getBoardFiles() != null) {
            // 저장폴더생성
            Path basePath = Paths.get(FILE_BOARD_DIR);
            Path uploadPath = Paths.get(FILE_BOARD_PATH);
            Path fullPath = basePath.resolve(uploadPath);
            UtilFile.makeFolders(fullPath);

            for (MultipartFile file : request.getBoardFiles()) {
                String storedFileName = UtilFile.makeUuidFileName(file.getOriginalFilename());
                // 실제파일 저장
                file.transferTo(fullPath.resolve(storedFileName).toFile());

                // 파일정보 저장
                BoardFileDto.CreateRequest boardFile = new BoardFileDto.CreateRequest();
                boardFile.setUploadPath(uploadPath.toString());
                boardFile.setOriginalFileName(file.getOriginalFilename());
                boardFile.setStoredFileName(storedFileName);
                boardFile.setFileSize(file.getSize());
                board.addBoardFile(boardFile.toEntity());
            }
        }

        return BoardDto.Response.toDto(boardRepository.save(board));
    }

    @Transactional
    public void boardDelete(String id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new CustomException(ResponseCode.EXCEPTION_NODATA, utilMessage.getMessage("notfound.data", null)));

        Path basePath = Paths.get(FILE_BOARD_DIR);
        Path uploadPath = Paths.get(FILE_BOARD_PATH);
        Path fullPath = basePath.resolve(uploadPath);
        // 1️⃣ 실제 파일 삭제
        for (BoardFile file : board.getBoardFiles()) {
            File localFile = new File(fullPath.resolve(file.getStoredFileName()).toUri());
            if (localFile.exists()) {
                localFile.delete();
            }
        }

        // 2️⃣ 게시글 삭제 (파일도 cascade로 함께 삭제)
        boardRepository.delete(board);
    }
}
