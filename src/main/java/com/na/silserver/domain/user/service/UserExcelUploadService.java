package com.na.silserver.domain.user.service;

import com.github.pjfanning.xlsx.StreamingReader;
import com.na.silserver.domain.user.dto.UserDto;
import com.na.silserver.domain.user.entity.User;
import com.na.silserver.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class UserExcelUploadService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final Integer batchSize = 1000; // 1000개 단위로 DB 반영

    @Async
    @Transactional
    public void excelUpload(MultipartFile file) throws Exception {
        try (InputStream is = file.getInputStream();
             Workbook workbook = StreamingReader.builder()
                     .rowCacheSize(100)
                     .bufferSize(4096)
                     .open(is)) {

            Sheet sheet = workbook.getSheetAt(0);

            List<User> batch = new ArrayList<>();
            UserDto.UploadRequest uploadRequest;
            boolean isHeaderRow = true;
            for (Row row : sheet) {
                // ✅ 첫 번째 줄(헤더)은 건너뜀
                if (isHeaderRow) {
                    isHeaderRow = false;
                    continue;
                }

                // 비어 있는 행은 무시
                if (row == null || row.getCell(0) == null) continue;

                uploadRequest = new UserDto.UploadRequest();
                uploadRequest.setUsername(getValue(row.getCell(0)));
                uploadRequest.setPassword(passwordEncoder.encode(getValue(row.getCell(1))));
                uploadRequest.setName(getValue(row.getCell(2)));
                uploadRequest.setEmail(getValue(row.getCell(3)));
                batch.add(uploadRequest.toEntity());

                if (batch.size() >= batchSize) {
                    saveBatch(batch);
                    batch.clear();
                }
            }

            if (!batch.isEmpty()) {
                saveBatch(batch);
            }
        }
    }

    private void saveBatch(List<User> batch) {
        userRepository.saveAll(batch);
    }

    private String getValue(Cell cell) {
        if (cell == null) return "";
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue().trim();
            case NUMERIC -> String.valueOf((long) cell.getNumericCellValue());
            default -> "";
        };
    }
}
