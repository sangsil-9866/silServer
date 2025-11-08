package com.na.silserver.domain.user.service;

import com.na.silserver.domain.user.dto.UserDto;
import com.na.silserver.domain.user.entity.User;
import com.na.silserver.domain.user.repository.UserRepository;
import com.na.silserver.global.util.UtilCommon;
import com.na.silserver.global.util.UtilFile;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class UserExcelDownloadService {

    private final UserRepository userRepository;

    public void excelDownload(UserDto.Search search, HttpServletResponse response) throws IOException {

        StringBuffer fileName = new StringBuffer();
        fileName.append("사용자");
        fileName.append("_");
        fileName.append(UtilCommon.datetimeStrFormatterNow());
        fileName.append(".xlsx");
        String encodedFilename = UtilFile.encodeFileName(fileName.toString());

        // 파일 응답 설정
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition","attachment; filename*=UTF-8''" + encodedFilename);

        // ① SXSSFWorkbook 사용 (대용량 대응)
        try (SXSSFWorkbook workbook = new SXSSFWorkbook(1000)) { // 1000행 단위로 flush
            Sheet sheet = workbook.createSheet("Users");

            // ② 스타일 생성
            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle bodyStyle = createBodyStyle(workbook);

            // ③ 헤더 작성
            String[] headers = {"아이디", "이름", "Email", "최근 로그인 일시", "가입일"};
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
                sheet.setColumnWidth(i, 5000);
            }

            // ④ 데이터 작성
            int rowNum = 1;
            for (User user : userRepository.searchUsers(search)) {
                Row row = sheet.createRow(rowNum++);
                createCell(row, 0, user.getUsername(), bodyStyle);
                createCell(row, 1, user.getName(), bodyStyle);
                createCell(row, 2, user.getEmail(), bodyStyle);
                createCell(row, 3, user.getSignindAt() != null ? UtilCommon.korFormat(user.getSignindAt()) : "", bodyStyle);
                createCell(row, 4, user.getSignupAt() != null ? UtilCommon.korFormat(user.getSignindAt()) : "", bodyStyle);
            }

            // ⑤ 엑셀 파일 출력
            workbook.write(response.getOutputStream());
            workbook.dispose(); // 메모리 정리 (임시 파일 삭제)
        }
    }

    // 셀 생성 헬퍼
    private void createCell(Row row, int col, String value, CellStyle style) {
        Cell cell = row.createCell(col);
        cell.setCellValue(value);
        cell.setCellStyle(style);
    }

    // 헤더 스타일
    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);

        style.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);

        return style;
    }

    // 본문 스타일
    private CellStyle createBodyStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;
    }
}
