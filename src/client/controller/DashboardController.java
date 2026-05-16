package client.controller;

import server.dao.MaterialDAO;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;

import java.util.Map;

public class DashboardController {

    @FXML
    private Label lblTotalMaterial;

    @FXML
    private Label lblLowStockWarning;

    @FXML
    private BarChart<String, Number> barChartRevenue;

    @FXML
    private BarChart<String, Number> barChartTopSelling;

    @FXML
    private PieChart pieChartCategory;

    private MaterialDAO materialDAO = new MaterialDAO();

    @FXML
    public void initialize() {
        System.out.println("🔄 Đang gửi lệnh test GET_ALL_MATERIALS lên Server qua Socket...");
        shared.Request requestTest = new shared.Request("GET_ALL_MATERIALS", null);
        shared.Response responseTest = client.service.SocketClient.sendRequest(requestTest);

        if (responseTest.isSuccess()) {
            System.out.println("TUYỆT VỜI! SERVER TRẢ VỀ: " + responseTest.getMessage());
        } else {
            System.out.println(" KẾT NỐI THẤT BẠI: " + responseTest.getMessage());
        }
        loadStatistics();
    }

    private void loadStatistics() {
        // 1. Thẻ số liệu: Tổng vật liệu trong kho
        int total = materialDAO.countAll();
        if (lblTotalMaterial != null) {
            lblTotalMaterial.setText(String.valueOf(total));
        }

        // 2. Thẻ số liệu: Cảnh báo vật liệu sắp hết hàng (< 10)
        int lowStock = materialDAO.countLowStock();
        if (lblLowStockWarning != null) {
            lblLowStockWarning.setText(String.valueOf(lowStock));
        }

        // 3. Biểu đồ tròn: Cơ cấu loại vật liệu
        if (pieChartCategory != null) {
            pieChartCategory.getData().clear();
            Map<String, Integer> categoryStats = materialDAO.getCategoryStats();
            categoryStats.forEach((name, count) -> {
                pieChartCategory.getData().add(new PieChart.Data(name, count));
            });
        }

        // 4. Biểu đồ cột: Top 5 Vật liệu bán chạy nhất
        if (barChartTopSelling != null) {
            barChartTopSelling.getData().clear();
            XYChart.Series<String, Number> topSellingSeries = new XYChart.Series<>();
            topSellingSeries.setName("Số lượng bán ra");

            Map<String, Integer> topSellingData = materialDAO.getTopSellingMaterials();
            topSellingData.forEach((materialName, totalSold) -> {
                topSellingSeries.getData().add(new XYChart.Data<>(materialName, totalSold));
            });
            barChartTopSelling.getData().add(topSellingSeries);
        }

        // 5. Biểu đồ cột: Doanh thu (Mô phỏng dữ liệu tĩnh chờ module Hóa đơn hoàn thiện)
        if (barChartRevenue != null) {
            barChartRevenue.getData().clear();
            XYChart.Series<String, Number> revenueSeries = new XYChart.Series<>();
            revenueSeries.setName("Năm 2026");
            revenueSeries.getData().add(new XYChart.Data<>("Tháng 1", 15000000));
            revenueSeries.getData().add(new XYChart.Data<>("Tháng 2", 28000000));
            revenueSeries.getData().add(new XYChart.Data<>("Tháng 3", 22000000));
            barChartRevenue.getData().add(revenueSeries);
        }
    }
    @FXML
    public void handleRefresh(javafx.event.ActionEvent event) {
        // Chỉ cần gọi lại hàm này, nó sẽ tự động chọc xuống DB lấy số mới và vẽ lại biểu đồ
        loadStatistics();
        System.out.println("Đã cập nhật dữ liệu thống kê mới nhất!");
    }
}