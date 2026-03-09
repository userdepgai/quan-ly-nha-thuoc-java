package utils;

import dto.TaiKhoan_DTO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class ExcelTaiKhoan {
    public static boolean exportExcel(ArrayList<TaiKhoan_DTO> list, String path) {
        try (Workbook wb = new XSSFWorkbook()) {
            Sheet sheet = wb.createSheet("TaiKhoan");
            Row header = sheet.createRow(0);

            header.createCell(0).setCellValue("MaTK");
            header.createCell(1).setCellValue("SDT");
            header.createCell(2).setCellValue("MatKhau");
            header.createCell(3).setCellValue("MaQuyen");
            header.createCell(4).setCellValue("NgayKichHoat");
            header.createCell(5).setCellValue("TrangThai");
            int rowNum = 1;
            for (TaiKhoan_DTO tk : list) {
                Row row = sheet.createRow(rowNum++);

                row.createCell(0).setCellValue(tk.getMaTK());
                row.createCell(1).setCellValue(tk.getSdt());
                row.createCell(2).setCellValue(tk.getMatKhau());
                row.createCell(3).setCellValue(tk.getMaQuyen());
                row.createCell(4).setCellValue(tk.getNgayKichHoat().toString());
                row.createCell(5).setCellValue(tk.getTrangThai());
            }

            FileOutputStream fos = new FileOutputStream(path);
            wb.write(fos);
            fos.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public static ArrayList<TaiKhoan_DTO> importExcel(String path){
        ArrayList<TaiKhoan_DTO> list = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(path);
             Workbook wb = new XSSFWorkbook(fis)) {
            Sheet sheet = wb.getSheetAt(0);
            for(int i=1;i<=sheet.getLastRowNum();i++){
                Row row = sheet.getRow(i);

                TaiKhoan_DTO tk = new TaiKhoan_DTO();

                tk.setMaTK(row.getCell(0).getStringCellValue());
                tk.setSdt(row.getCell(1).getStringCellValue());
                tk.setMatKhau(row.getCell(2).getStringCellValue());
                tk.setMaQuyen(row.getCell(3).getStringCellValue());
                tk.setNgayKichHoat(LocalDate.parse(row.getCell(4).getStringCellValue()));
                tk.setTrangThai((int)row.getCell(5).getNumericCellValue());
                list.add(tk);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

}