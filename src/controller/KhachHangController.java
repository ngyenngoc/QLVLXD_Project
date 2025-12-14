package controller;


import dao.KhachHangDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Pane;
import model.KhachHang;
import view.KhachHangView;

import java.util.List;
import java.util.Optional;


public class KhachHangController  {
    private final KhachHangDAO dao= new KhachHangDAO();
    private final KhachHangView view= new KhachHangView();
    private final ObservableList<KhachHang> khachhangList = FXCollections.observableArrayList();
    public Pane getKHManagementView(){
        initialize();
        return view.createRootNode();
    }
    public void initialize(){
        view.tblKhachHang.setItems(khachhangList);
        setupEventHandlers();
        loadKhachHangData();
    }
    private void loadKhachHangData(){
        List<KhachHang> list = dao.getAll();
        khachhangList.clear();
        khachhangList.addAll(list);
    }
    private void setupEventHandlers() {
        view.btnThem.setOnAction(e -> handleThemKhachHang());
        view.btnSua.setOnAction(e -> handleSuaKhachHang());
        view.btnXoa.setOnAction(e -> handleXoaKhachHang());
        view.btnRefresh.setOnAction(e -> loadKhachHangData());
        // Gán Listener cho TableView
        view.tblKhachHang.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        hienThiChiTiet(newSelection);
                    }
                });
    }
    //Xử lý CRUD
    private void handleThemKhachHang(){
        KhachHang newKH = new KhachHang(dao.generateNewId(), view.txtTenKhachHang.getText(), view.txtDiaChi.getText(), view.txtSoDienThoai.getText(), view.txtEmail.getText());
        if (dao.insert(newKH)){
            view.lblMessage.setText("Thêm Khách Hàng " +newKH.getTenKhachHang() + " Thành Công!");
            khachhangList.add(newKH);
            xoaTrangForm();
        }
        else{
            view.lblMessage.setText("Thêm thất bại!");
        }
    }
    private void handleSuaKhachHang(){
        KhachHang selectedKH = view.tblKhachHang.getSelectionModel().getSelectedItem();
        if (selectedKH == null){
            view.lblMessage.setText("Vui lòng chọn Khách Hàng để sửa");
            return;
        }
        KhachHang updatedKH = new KhachHang(
                view.txtMaKhachHang.getText(), view.txtTenKhachHang.getText(), view.txtDiaChi.getText(), view.txtSoDienThoai.getText(),view.txtEmail.getText()
        );
        if(dao.update(updatedKH)){
            view.lblMessage.setText("Cập nhật thành công!");
            int index = khachhangList.indexOf(selectedKH);
            if(index != -1){
                khachhangList.set(index, updatedKH);
            }
        }
        else{
            view.lblMessage.setText("Cập nhật thất bại!");
        }
    }
    private void handleXoaKhachHang(){
        KhachHang selectedKH = view.tblKhachHang.getSelectionModel().getSelectedItem();
        if(selectedKH == null){
            view.lblMessage.setText("Vui lòng chọn Khách Hàng để xóa");
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Xác nhận xóa Khách Hàng " +selectedKH.getTenKhachHang() + "?");
        Optional<ButtonType> result = confirm.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK){
            if (dao.delete(selectedKH.getMaKhachHang())){
                view.lblMessage.setText("Xóa Khách Hàng " + selectedKH.getTenKhachHang() + "Thành công");
                khachhangList.remove(selectedKH);
                xoaTrangForm();
            }
            else{
                view.lblMessage.setText("Xóa thất bại!");
            }
        }
    }
    private void hienThiChiTiet(KhachHang KH){
        view.txtMaKhachHang.setText(KH.getMaKhachHang());
        view.txtTenKhachHang.setText(KH.getTenKhachHang());
        view.txtDiaChi.setText(KH.getDiaChi());
        view.txtSoDienThoai.setText(KH.getSoDienThoai());
        view.txtEmail.setText(KH.getEmail());
        view.lblMessage.setText("");
    }
    private void xoaTrangForm(){
        view.txtMaKhachHang.clear();
        view.txtTenKhachHang.clear();
        view.txtDiaChi.clear();
        view.txtSoDienThoai.clear();
        view.txtEmail.clear();
    }
}
