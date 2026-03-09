package utils;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import javax.swing.*;
import java.io.FileOutputStream;

public class PDFExporter {

    public static void exportHoaDon(
            JTable table,
            String maHD,
            String nhanVien,
            String ngayLap,
            String tenKH,
            String sdt,
            boolean keToa,
            String tongTien,
            String tongKM,
            String diemThuong,
            String thanhTien,
            String voucher,
            String path
    ) throws Exception {

        Document doc = new Document(PageSize.A4);

        PdfWriter.getInstance(doc, new FileOutputStream(path));
        doc.open();

        BaseFont bf = BaseFont.createFont(
                "C:/Windows/Fonts/arial.ttf",
                BaseFont.IDENTITY_H,
                BaseFont.EMBEDDED
        );

        Font font = new Font(bf, 12);
        Font fontBold = new Font(bf, 12, Font.BOLD);
        Font fontTitle = new Font(bf, 22, Font.BOLD);
        Font fontHeader = new Font(bf, 16, Font.BOLD);

        Paragraph store = new Paragraph("NHÀ THUỐC XÌ TRUM", fontTitle);
        store.setAlignment(Element.ALIGN_CENTER);

        Paragraph bill = new Paragraph("HÓA ĐƠN BÁN HÀNG", fontHeader);
        bill.setAlignment(Element.ALIGN_CENTER);

        doc.add(store);
        doc.add(bill);
        doc.add(new Paragraph(" "));

        Paragraph date = new Paragraph("Ngày lập: " + ngayLap, font);
        date.setAlignment(Element.ALIGN_RIGHT);
        doc.add(date);

        doc.add(new Paragraph(" "));

        doc.add(new Paragraph("Mã hóa đơn: " + maHD, font));
        doc.add(new Paragraph("Nhân viên: " + nhanVien, font));
        doc.add(new Paragraph("Khách hàng: " + tenKH, font));
        doc.add(new Paragraph("SĐT: " + sdt, font));

        if (keToa) {
            doc.add(new Paragraph("Kê toa: Có", font));
        }

        doc.add(new Paragraph(" "));

        PdfPTable pdfTable = new PdfPTable(7);
        pdfTable.setWidthPercentage(100);
        pdfTable.setWidths(new float[]{1, 4, 2, 2, 2, 1, 2});

        pdfTable.addCell(new Phrase("STT", fontBold));
        pdfTable.addCell(new Phrase("Tên SP", fontBold));
        pdfTable.addCell(new Phrase("Giá bán", fontBold));
        pdfTable.addCell(new Phrase("Khuyến mãi", fontBold));
        pdfTable.addCell(new Phrase("Giá sau KM", fontBold));
        pdfTable.addCell(new Phrase("SL", fontBold));
        pdfTable.addCell(new Phrase("Thành tiền", fontBold));

        for (int i = 0; i < table.getRowCount(); i++) {

            pdfTable.addCell(new Phrase(String.valueOf(table.getValueAt(i, 0)), font));
            pdfTable.addCell(new Phrase(String.valueOf(table.getValueAt(i, 2)), font));
            pdfTable.addCell(new Phrase(String.valueOf(table.getValueAt(i, 4)), font));

            String km = String.valueOf(table.getValueAt(i, 5));
            if (km == null || km.equals("null") || km.trim().isEmpty()) {
                km = "Không có";
            }

            pdfTable.addCell(new Phrase(km, font));
            pdfTable.addCell(new Phrase(String.valueOf(table.getValueAt(i, 6)), font));
            pdfTable.addCell(new Phrase(String.valueOf(table.getValueAt(i, 7)), font));
            pdfTable.addCell(new Phrase(String.valueOf(table.getValueAt(i, 8)), font));
        }

        doc.add(pdfTable);
        doc.add(new Paragraph(" "));

        doc.add(new Paragraph("Tổng tiền: " + tongTien, font));

        if (tongKM != null && !tongKM.trim().isEmpty() && !tongKM.equals("0")) {
            doc.add(new Paragraph("Tổng giá trị khuyến mãi: " + tongKM, font));
        }

        if (voucher != null && !voucher.trim().isEmpty() && !voucher.equals("null") && !voucher.equals("Không voucher")) {
            doc.add(new Paragraph("Voucher: " + voucher, font));
        }

        if (diemThuong != null && !diemThuong.trim().isEmpty() && !diemThuong.equals("0")) {
            doc.add(new Paragraph("Điểm thưởng đã dùng: " + diemThuong, font));
        }

        doc.add(new Paragraph("Thuế VAT: 5%", font));

        doc.add(new Paragraph("---------------------------------------", font));
        doc.add(new Paragraph("Thành tiền: " + thanhTien, fontBold));

        doc.add(new Paragraph(" "));

        try {

            String so = thanhTien
                    .replace("đ", "")
                    .replace(".", "")
                    .trim();

            double tien = Double.parseDouble(so);

            int diemNhan = (int) (tien / 10000);

            doc.add(new Paragraph("Điểm thưởng nhận được: " + diemNhan + " điểm", fontBold));

        } catch (Exception e) {

            doc.add(new Paragraph("Điểm thưởng nhận được: 0 điểm", fontBold));

        }

        doc.close();
    }

}
