package utils;

import dto.NhanVien_DTO;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ExcelNhanVien {

    public static boolean exportExcel(ArrayList<NhanVien_DTO> list, String path) {
        try (Workbook wb = new XSSFWorkbook()) {
            Sheet sheet = wb.createSheet("NhanVien");
            Row header = sheet.createRow(0);

            header.createCell(0).setCellValue("MaNV");
            header.createCell(1).setCellValue("TenNV");
            header.createCell(2).setCellValue("SDT");
            header.createCell(3).setCellValue("NgaySinh");
            header.createCell(4).setCellValue("GioiTinh");
            header.createCell(5).setCellValue("ChucVu");
            header.createCell(6).setCellValue("NgayVaoLam");
            header.createCell(7).setCellValue("LuongCoBan");
            header.createCell(8).setCellValue("TrangThai");
            header.createCell(9).setCellValue("MaDiaChi");
            header.createCell(10).setCellValue("MaQuyen");

            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            int rowNum = 1;

            for (NhanVien_DTO nv : list) {
                Row row = sheet.createRow(rowNum++);

                row.createCell(0).setCellValue(nv.getMa());
                row.createCell(1).setCellValue(nv.getTen());
                row.createCell(2).setCellValue(nv.getSdt());
                row.createCell(3).setCellValue(nv.getNgaySinh().format(df));
                row.createCell(4).setCellValue(
                        nv.isGioiTinh() ? "Nam" : "Nữ"
                );
                row.createCell(5).setCellValue(nv.getChucVu());
                row.createCell(6).setCellValue(nv.getNgayVaoLam().format(df));
                row.createCell(7).setCellValue(nv.getLuongCoBan());
                row.createCell(8).setCellValue(nv.getTrangThai());
                row.createCell(9).setCellValue(nv.getMaDiaChi());
                row.createCell(10).setCellValue(nv.getMaQuyen());
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
}