package cn.xiaoyaoji.service.util;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * @author zhoujingjie
 *         created on 2017/7/15
 */
public class ZipUtils {
    /**
     * 解压缩
     *
     * @param file
     * @param outputDir
     * @throws IOException
     */
    public static void uncompression(File file, String outputDir) throws IOException {
        ZipFile zipFile = new ZipFile(file);
        try {
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                File entryDestination = new File(outputDir, entry.getName());
                if (entry.isDirectory()) {
                    entryDestination.mkdirs();
                } else {
                    entryDestination.getParentFile().mkdirs();
                    try (InputStream in = zipFile.getInputStream(entry);
                         OutputStream out = new FileOutputStream(entryDestination);
                    ) {
                        IOUtils.copy(in, out);
                    }
                }
            }
        } finally {
            zipFile.close();
        }
    }
}
