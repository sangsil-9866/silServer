package com.na.silserver.global.util;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

/**
 * 파일유틸
 */
@Slf4j
public class UtilFile {

    /* ✅ 상위 폴더 모두 생성 */
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

    /* ✅ 파일 확장자 가져오기*/
    public static String getExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) return "";
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    /* ✅ UUID 파일명 만들기 */
    public static String makeUuidFileName(String originalFileName) {
        String ext = getExtension(originalFileName);
        return UUID.randomUUID() + (ext.isEmpty() ? "" : "." + ext);
    }

    /* ✅ 파일 다운로드용 인코딩 */
    public static String encodeFileName(String fileName) {
        return URLEncoder.encode(fileName, StandardCharsets.UTF_8)
                .replaceAll("\\+", "%20");
    }
}
