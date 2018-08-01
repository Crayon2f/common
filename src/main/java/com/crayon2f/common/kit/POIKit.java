package com.crayon2f.common.kit;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Cleanup;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.val;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by feifan.gou@gmail.com on 2018/7/31 15:44.
 * POI 工具
 */
public final class POIKit {

    @SneakyThrows(IOException.class)
    public static void export(SheetParam param, String outputPath, Axis axis) {

        Path path = Paths.get(outputPath);
        if (!Files.exists(path)) {
            Files.createDirectories(path.getParent());
        }
        write2stream(param, new FileOutputStream(outputPath), axis);
    }

    @SneakyThrows(IOException.class)
    public static void write2stream(SheetParam param, OutputStream stream, Axis axis) {

        @Cleanup
        SXSSFWorkbook workbook = generate(param, axis);
        if (null != stream) {
            workbook.write(stream);
            IOUtils.closeQuietly(stream);
        }
    }


    public static SXSSFWorkbook generate(SheetParam param, Axis axis) {

        SXSSFWorkbook workbook = new SXSSFWorkbook();
        SXSSFSheet sheet = Optional.ofNullable(param)
                .map(ths -> {
                    if (StringUtils.isNotEmpty(ths.name)) {
                        ths.name = ths.name.replaceAll("[/\\\\?？\\[\\]*\\s:：]+", "");
                        return workbook.createSheet(ths.name);
                    } else {
                        return workbook.createSheet();
                    }
                })
                .orElseThrow(() -> new IllegalArgumentException("param error"));
        //headers
        String[] headers = param.getHeaders();
        Row headerRow = sheet.createRow(0);
        for (int i = 0, length = headers.length; i < length; i++) {
            CellUtil.createCell(headerRow, i, headers[i]);
        }
        //content
        List<List<String>> data =
                Optional.ofNullable(param.getData()).filter(CollectionUtils::isNotEmpty).orElseGet(ArrayList::new);
        val index = new int[]{1}; //计数器
        switch (axis) {
            case x:
                data.forEach(content -> Optional.ofNullable(content).filter(CollectionUtils::isNotEmpty).ifPresent(ths -> {
                    Row row = sheet.createRow(index[0]);
                    for (int i = 0, length = ths.size(); i < length; i++) {
                        CellUtil.createCell(row, i, ths.get(i));
                    }
                    index[0]++;
                }));
                break;
            case y:
                val columnIndex = new int[]{0};
                data.forEach(content -> Optional.ofNullable(content).filter(CollectionUtils::isNotEmpty).ifPresent(ths -> {
                    ths.forEach(value -> {
                        Row row = Optional.ofNullable(sheet.getRow(index[0])).orElseGet(() -> sheet.createRow(index[0]));
                        if (StringUtils.isNotEmpty(value) && !value.equalsIgnoreCase("null")) {
                            CellUtil.createCell(row, columnIndex[0], value);
                        }
                        index[0]++;
                    });
                    index[0] = 1;
                    columnIndex[0]++;
                }));
                break;

        }
        return workbook;
    }

    public enum Axis {
        x, y
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static final class SheetParam {

        private String name;

        private List<List<String>> data;

        private String[] headers;
    }
}
