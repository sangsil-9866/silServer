package com.na.silserver.domain.board.service;

import com.na.silserver.domain.board.dto.BoardDto;
import com.na.silserver.domain.board.dto.BoardFileDto;
import com.na.silserver.domain.board.entity.Board;
import com.na.silserver.domain.board.repository.BoardFileRepository;
import com.na.silserver.domain.board.repository.BoardRepository;
import com.na.silserver.global.util.UtilFile;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {
    private final BoardRepository boardRepository;
    private final BoardFileRepository boardFileRepository;

    // BOARD 게시판
    @Value("${custom.file.board.dir}") private String FILE_BOARD_DIR;
    @Value("${custom.file.board.path}") private String FILE_BOARD_PATH;

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
                String storedFileName = UtilFile.renameToUUID(file.getOriginalFilename());
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
}
