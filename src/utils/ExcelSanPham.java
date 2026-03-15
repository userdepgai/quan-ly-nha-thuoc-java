package utils;

import dto.SanPham_DTO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ExcelSanPham {
    public static boolean exportExcel(ArrayList<SanPham_DTO> list, String path) {
        try (Workbook wb = new XSSFWorkbook()) {
            Sheet sheet = wb.createSheet("SanPham");
            Row header = sheet.createRow(0);
            String[] columns = {"MaSP", "TenSP", "DonViTinh", "LoiNhuan", "HinhAnh", "KeDon", "TrangThai", "MaDM", "MaQC"};
            for (int i = 0; i < columns.length; i++) {
                header.createCell(i).setCellValue(columns[i]);
            }
            int rowNum = 1;
            for (SanPham_DTO sp : list) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(sp.getMaSP());
                row.createCell(1).setCellValue(sp.getTenSP());
                row.createCell(2).setCellValue(sp.getDonViTinh());
                row.createCell(3).setCellValue(sp.getLoiNhuan());
                row.createCell(4).setCellValue(sp.getHinhAnh());
                row.createCell(5).setCellValue(sp.getKeDon());
                row.createCell(6).setCellValue(sp.getTrangThai());
                row.createCell(7).setCellValue(sp.getMaDM());
                row.createCell(8).setCellValue(sp.getMaQC());
            }
            try (FileOutputStream fos = new FileOutputStream(path)) {
                wb.write(fos);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static ArrayList<Map<String, Object>> importExcel(String path) {
        ArrayList<Map<String, Object>> list = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(path);
             Workbook wb = new XSSFWorkbook(fis)) {
            Sheet sheet = wb.getSheetAt(0);
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null || row.getCell(1) == null) continue;
                Map<String, Object> data = new HashMap<>();
                data.put("tenSP", getCellValueAsString(row.getCell(1)));
                data.put("dvt", getCellValueAsString(row.getCell(2)));
                data.put("loiNhuan", getCellValueAsDouble(row.getCell(3)));
                data.put("hinhAnh", getCellValueAsString(row.getCell(4)));
                data.put("keDon", (int) getCellValueAsDouble(row.getCell(5)));
                data.put("trangThai", (int) getCellValueAsDouble(row.getCell(6)));
                data.put("maDM", getCellValueAsString(row.getCell(7)));
                data.put("slTrongHop", (int) getCellValueAsDouble(row.getCell(8)));
                data.put("slHopTrongThung", (int) getCellValueAsDouble(row.getCell(9)));
                list.add(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    private static String getCellValueAsString(Cell cell) {
        if (cell == null) return "";
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> String.valueOf(cell.getNumericCellValue());
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            default -> "";
        };
    }

    private static double getCellValueAsDouble(Cell cell) {
        if (cell == null) return 0.0;
        try {
            if (cell.getCellType() == CellType.NUMERIC) {
                return cell.getNumericCellValue();
            } else if (cell.getCellType() == CellType.STRING) {
                return Double.parseDouble(cell.getStringCellValue().replace(",", "."));
            }
        } catch (Exception e) {
            return 0.0;
        }
        return 0.0;
    }
}