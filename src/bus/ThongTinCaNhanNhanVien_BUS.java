package bus;

import dto.NhanVien_DTO;
import dto.TaiKhoan_DTO;
import utils.Session;

import javax.swing.*;

public class ThongTinCaNhanNhanVien_BUS {
    private NhanVien_BUS nvBus = NhanVien_BUS.getInstance();

    public NhanVien_DTO getNhanVienDangNhap() {
        TaiKhoan_DTO tk = Session.getCurrentUser();
        if (tk == null) {
            return null;
        }
        return NhanVien_BUS.getInstance().getBysdt(tk.getSdt().trim());
    }

    public boolean capNhatThongTin(NhanVien_DTO nvMoi) {
        NhanVien_DTO nvCu = nvBus.getBysdt(Session.getCurrentUser().getSdt());
        if (nvCu == null) {
            return false;
        }

        String sdtCu = nvCu.getSdt();
        String sdtMoi = nvMoi.getSdt();

        if (!sdtCu.equals(sdtMoi)) {
            if (kiemTraTonTaiSDT(sdtMoi)) {
                return false;
            }

            boolean resultTK = TaiKhoan_BUS.getInstance()
                    .capNhatSDTNhanVien(sdtCu, sdtMoi);

            if (!resultTK) {
                return false;
            }
        }

        boolean resultNV = nvBus.capNhat(nvMoi);
        if (!resultNV) {
            return false;
        }

        return true;
    }

    public boolean kiemTraTonTaiSDT(String sdt) {
        for (NhanVien_DTO nv : NhanVien_BUS.getInstance().getAll()) {
            if (nv.getSdt().equals(sdt)) {
                return true;
            }
        }
        return false;
    }
}