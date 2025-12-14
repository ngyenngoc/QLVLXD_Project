package controller;


import dao.NhaCungCapDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Pane;
import model.NhaCungCap;
import view.NhaCungCapView;

import java.util.List;
import java.util.Optional;


public class NhaCungCapController {
    private final NhaCungCapDAO dao= new NhaCungCapDAO();
    private final NhaCungCapView view= new NhaCungCapView();
    private final ObservableList<NhaCungCap> nhacungcapList = FXCollections.observableArrayList();
    // Phương thức chính để lấy View
    public Pane getNCCManagementView(){
        initialize();
        return view.createRootNode();
    }
    public void initialize(){
        view.tblNhaCungCap.setItems(nhacungcapList);
        setupEventHandlers();
        loadNhaCungCapData();
    }
    private void loadNhaCungCapData(){
        List<NhaCungCap> list = dao.getAll();
    nhacungcapList.clear();
    nhacungcapList.addAll(list);
    }
    private void setupEventHandlers() {
        view.btnThem.setOnAction(e -> handleThemNhaCungCap());
        view.btnSua.setOnAction(e -> handleSuaNhaCungCap());
        view.btnXoa.setOnAction(e -> handleXoaNhaCungCap());
        view.btnRefresh.setOnAction(e -> loadNhaCungCapData());
        // Gán Listener cho TableView
        view.tblNhaCungCap.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        hienThiChiTiet(newSelection);
                    }
                });
    }
    //Xử lý CRUD
    private void handleThemNhaCungCap(){
        NhaCungCap newNCC = new NhaCungCap(dao.generateNewID(), view.txtTenNhaCungCap.getText(), view.txtDiaChi.getText(), view.txtSoDienThoai.getText(), view.txtEmail.getText());
            if (dao.insert(newNCC)){
                view.lblMessage.setText("Thêm Nhà Cung Cấp " +newNCC.getTenNhaCungCap() + " Thành Công!");
                nhacungcapList.add(newNCC);
                xoaTrangForm();
            }
            else{
                view.lblMessage.setText("Thêm thất bại!");
            }
        }
    private void handleSuaNhaCungCap(){
    NhaCungCap selectedNCC = view.tblNhaCungCap.getSelectionModel().getSelectedItem();
    if (selectedNCC == null){
        view.lblMessage.setText("Vui lòng chọn Nhà Cung Cấp để sửa");
        return;
    }
    NhaCungCap updatedNCC = new NhaCungCap(
            view.txtMaNhaCungCap.getText(), view.txtTenNhaCungCap.getText(), view.txtDiaChi.getText(), view.txtSoDienThoai.getText(),view.txtEmail.getText()
        );
    if(dao.update(updatedNCC)){
        view.lblMessage.setText("Cập nhật thành công!");
        int index = nhacungcapList.indexOf(selectedNCC);
        if(index != -1){
            nhacungcapList.set(index, updatedNCC);
        }
    }
    else{
        view.lblMessage.setText("Cập nhật thất bại!");
    }
    }
    private void handleXoaNhaCungCap(){
    NhaCungCap selectedNCC = view.tblNhaCungCap.getSelectionModel().getSelectedItem();
    if(selectedNCC == null){
        view.lblMessage.setText("Vui lòng chọn Nhà Cung Cấp để xóa");
        return;
    }
    Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Xác nhận xóa Nhà Cung Cấp " +selectedNCC.getTenNhaCungCap() + "?");
        Optional<ButtonType> result = confirm.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK){
            if (dao.delete(selectedNCC.getMaNhaCungCap())){
                view.lblMessage.setText("Xóa Nhà Cung Cấp " + selectedNCC.getTenNhaCungCap() + "Thành công");
                nhacungcapList.remove(selectedNCC);
                xoaTrangForm();
            }
            else{
                view.lblMessage.setText("Xóa thất bại!");
            }
        }
    }
    private void hienThiChiTiet(NhaCungCap NCC){
        view.txtMaNhaCungCap.setText(NCC.getMaNhaCungCap());
        view.txtTenNhaCungCap.setText(NCC.getTenNhaCungCap());
        view.txtDiaChi.setText(NCC.getDiaChi());
        view.txtSoDienThoai.setText(NCC.getSoDienThoai());
        view.txtEmail.setText(NCC.getEmail());
        view.lblMessage.setText("");
    }
    private void xoaTrangForm(){
        view.txtMaNhaCungCap.clear();
        view.txtTenNhaCungCap.clear();
        view.txtDiaChi.clear();
        view.txtSoDienThoai.clear();
        view.txtEmail.clear();
    }
}