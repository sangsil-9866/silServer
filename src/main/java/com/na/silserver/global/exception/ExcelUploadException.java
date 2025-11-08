package com.na.silserver.global.exception;


import com.na.silserver.global.response.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Excel업로드 익셉션
 * 익셉션 강제발생 시킬 경우 주로 사용한다
 */
@Getter
public class ExcelUploadException extends RuntimeException  {

    private final List<ExcelRowError> errors;

	public ExcelUploadException(String message, List<ExcelRowError> errors) {
		super(message);
        this.errors = errors;
	}

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ExcelRowError {
        private int rowNumber;       // 몇 번째 행인지
        private List<String> messages; // 어떤 오류가 발생했는지
    }
}
