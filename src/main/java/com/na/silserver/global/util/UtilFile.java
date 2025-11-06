package com.na.silserver.global.util;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

/**
 * 파일유틸
 */
@Slf4j
public class UtilFile {

    /**
     * 상위폴더 모두 생성
     * @param folderPath
     */
	public static void makeFolders(Path folderPath) {
		// 해당 디렉토리가 없다면 디렉토리를 생성.
        try {
            if (!Files.exists(folderPath)) {
                Files.createDirectories(folderPath);
//                System.out.println("폴더 생성 완료: " + folderPath);
            } else {
//                System.out.println("이미 폴더가 존재합니다: " + folderPath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

    /**
     * 파일 확장자
     * @param fileName
     * @return
     */
    public static String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex == -1 || dotIndex == fileName.length() - 1) {
            return ""; // 확장자 없음
        }
        return fileName.substring(dotIndex + 1);
    }

    /**
     * 원본 파일명을 UUID.확장자 형태로 변환
     * @param originalFileName 원본 파일명 (예: "photo.png")
     * @return 변환된 파일명 (예: "uuid.png")
     */
    public static String renameToUUID(String originalFileName) {
        if (originalFileName == null || originalFileName.isBlank()) {
            throw new IllegalArgumentException("파일명이 비어있습니다.");
        }

        String ext = "";
        int dotIndex = originalFileName.lastIndexOf('.');

        if (dotIndex > 0 && dotIndex < originalFileName.length() - 1) {
            ext = originalFileName.substring(dotIndex + 1);
        }

        String uuid = UUID.randomUUID().toString();

        if (ext.isEmpty()) {
            return uuid; // 확장자 없으면 그냥 UUID만 반환
        } else {
            return uuid + "." + ext;
        }
    }
}
