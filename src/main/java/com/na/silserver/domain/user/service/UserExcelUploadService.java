package com.na.silserver.domain.user.service;

import com.github.pjfanning.xlsx.StreamingReader;
import com.na.silserver.domain.user.dto.UserDto;
import com.na.silserver.domain.user.entity.User;
import com.na.silserver.domain.user.repository.UserRepository;
import com.na.silserver.global.exception.ExcelUploadException;
import com.na.silserver.global.util.UtilCommon;
import com.na.silserver.global.util.UtilMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class UserExcelUploadService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UtilMessage utilMessage;

    private final Integer batchSize = 1000; // 1000개 단위로 DB 반영

    @Transactional
    public Integer excelUpload(MultipartFile file) throws Exception {
        List<ExcelUploadException.ExcelRowError> errors = new ArrayList<>();
        List<User> users = new ArrayList<>();
        Set<String> usernameSet = new HashSet<>();

        // 1️⃣ DB에 이미 존재하는 username 미리 조회
        Set<String> existingUsernames = new HashSet<>(userRepository.findAllUsernames());

        try (InputStream is = file.getInputStream();
             Workbook workbook = StreamingReader.builder()
                     .rowCacheSize(100)
                     .bufferSize(4096)
                     .open(is)) {

            Sheet sheet = workbook.getSheetAt(0);
            boolean isHeader = true;
            int rowIndex = 0;

            UserDto.ExcelUploadRequest excelUploadRequest;
            for (Row row : sheet) {
                rowIndex++;

                // ✅ 첫 행(헤더)은 스킵
                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                // 비어 있는 행은 무시
                if (row == null || row.getCell(0) == null) continue;

                excelUploadRequest = new UserDto.ExcelUploadRequest();
                String username = getValue(row.getCell(0));
                String password = getValue(row.getCell(1));
                String name = getValue(row.getCell(2));
                String email = getValue(row.getCell(3));

                List<String> rowErrors = new ArrayList<>();

                // ✅ 필수 값 검증
                if (UtilCommon.isEmpty(username)) rowErrors.add(utilMessage.getMessage("validation.field.empty", new String[]{"아이디"}));
                if (UtilCommon.isEmpty(password)) rowErrors.add(utilMessage.getMessage("validation.field.empty", new String[]{"비밀번호"}));
                if (UtilCommon.isEmpty(name)) rowErrors.add(utilMessage.getMessage("validation.field.empty", new String[]{"이름"}));
                if (UtilCommon.isEmpty(email)) rowErrors.add(utilMessage.getMessage("validation.field.empty", new String[]{"이메일"}));

                // ✅ 엑셀 내부 중복 체크
                if (UtilCommon.isNotEmpty(username) && !usernameSet.add(username)) {
                    rowErrors.add(utilMessage.getMessage("validation.excel.field.duplicate", new String[]{"아이디", username}));
                }

                // ✅ DB 내 중복 체크
                if (UtilCommon.isNotEmpty(username) && existingUsernames.contains(username)) {
                    rowErrors.add(utilMessage.getMessage("validation.field.duplicate", new String[]{"아이디", username}));
                }

                if (!rowErrors.isEmpty()) {
                    errors.add(new ExcelUploadException.ExcelRowError(rowIndex, rowErrors));
                    continue;
                }

                excelUploadRequest.setUsername(username);
                excelUploadRequest.setPassword(passwordEncoder.encode(password));
                excelUploadRequest.setName(name);
                excelUploadRequest.setEmail(email);
                users.add(excelUploadRequest.toEntity());
            }

            // 오류가 있다면 전체 저장하지 않는다(대용량의 경우 속도 및 메모리 이슈있음). 상황에 따라 선택적으로 정상데이타는 저장하는 방법도 고려
            if (UtilCommon.isNotEmpty(errors)) {
                throw new ExcelUploadException(utilMessage.getMessage("exception.excel.upload"), errors);
            }

            // ✅ 배치 단위 저장
            for(User user : users){
                if (users.size() >= batchSize) {
                    userRepository.saveAll(users);
                    users.clear();
                }
            }
            if (!users.isEmpty()) {
                userRepository.saveAll(users);
            }
        }
        return users.size();
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
