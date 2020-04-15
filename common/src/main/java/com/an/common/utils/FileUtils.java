package com.an.common.utils;

import org.apache.commons.io.FilenameUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class FileUtils {

    public static String storeFile(String targetDir, MultipartFile file) {
        File dir = new File(targetDir);
        // create directory if has not
        if ((!dir.exists() || !dir.isDirectory())) {
            dir.mkdirs();
        }
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = Paths.get(dir.getAbsolutePath()).resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return targetLocation.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return ""; // can not create dir
    }

    public static void unzipFile (String unzipTarget, InputStream inputStream){
        try {
            File dir = new File(unzipTarget);
            // create directory if has not
            if ((!dir.exists() || !dir.isDirectory())) {
                dir.mkdirs();
            }

            File destDir = new File(unzipTarget);
            byte[] buffer = new byte[1024];
            ZipInputStream zis = new ZipInputStream(inputStream);
            ZipEntry zipEntry = zis.getNextEntry();
            while (zipEntry != null) {
                File newFile = new File(destDir, zipEntry.getName());
                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
                zipEntry = zis.getNextEntry();
            }
            zis.closeEntry();
            zis.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static File[] getListFile (String dir, String... fileExtend){
        File file = new File(dir);
        if (file.exists() && file.isDirectory()){
            if (!StringUtils.isEmpty(fileExtend)) {
                return file.listFiles(new FileFilter() {
                    @Override
                    public boolean accept(File pathname) {
                        return FilenameUtils.isExtension(pathname.getName(), fileExtend);
                    }
                });
            } else {
                return file.listFiles();
            }
        }

        return null;
    }
}
