package client.controller;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import shared.Request;
import shared.Response;
import client.service.SocketClient;

import java.util.Map;

public class DashboardController {

    @FXML private Label lblTotalMaterial;
    @FXML private Label lblLowStockWarning;
    @FXML private BarChart<String, Number> barChartRevenue;
    @FXML private BarChart<String, Number> barChartTopSelling;
    @FXML private PieChart pieChartCategory;

    @FXML
    public void initialize() {
        loadStatistics();
    }

    private void loadStatistics() {
        // Gửi MỘT yêu cầu duy nhất lên Server để lấy toàn bộ dữ liệu thống kê
        Request request = new Request("GET_DASHBOARD_STATS", null);
        Response response = SocketClient.sendRequest(request);

        if (response.isSuccess()) {
            // Ép kiểu dữ liệu Server trả về thành 1 Map chứa mọi thông tin
            Map<String, Object> statsData = (Map<String, Object>) response.getData();

            // 1. Thẻ số liệu: Tổng vật liệu trong kho
            if (lblTotalMaterial != null && statsData.containsKey("totalMaterial")) {
                lblTotalMaterial.setText(String.valueOf(statsData.get("totalMaterial")));
            }

            // 2. Thẻ số liệu: Cảnh báo vật liệu sắp hết hàng
            if (lblLowStockWarning != null && statsData.containsKey("lowStock")) {
                lblLowStockWarning.setText(String.valueOf(statsData.get("lowStock")));
            }

            // 3. Biểu đồ tròn: Cơ cấu loại vật liệu
            if (pieChartCategory != null && statsData.containsKey("categoryStats")) {
                pieChartCategory.getData().clear();
                Map<String, Integer> categoryStats = (Map<String, Integer>) statsData.get("categoryStats");
                categoryStats.forEach((name, count) -> {
                    pieChartCategory.getData().add(new PieChart.Data(name, count));
                });

                // Hiển thị chú thích bảng màu cho biểu đồ tròn
                pieChartCategory.setLegendVisible(true);
                pieChartCategory.setLegendSide(javafx.geometry.Side.RIGHT);
                pieChartCategory.setLabelsVisible(true);
            }

            // 4. Biểu đồ cột: Top 5 Vật liệu bán chạy nhất
            if (barChartTopSelling != null && statsData.containsKey("topSelling")) {
                barChartTopSelling.getData().clear();
                XYChart.Series<String, Number> topSellingSeries = new XYChart.Series<>();
                topSellingSeries.setName("Số lượng bán ra");

                Map<String, Integer> topSellingData = (Map<String, Integer>) statsData.get("topSelling");
                topSellingData.forEach((materialName, totalSold) -> {

                    // Giữ nguyên tên gốc, không cắt ngắn nữa
                    topSellingSeries.getData().add(new XYChart.Data<>(materialName, totalSold));

                });
                barChartTopSelling.getData().add(topSellingSeries);
            }

        } else {
            System.err.println(" Lỗi lấy dữ liệu thống kê: " + response.getMessage());
        }

        // 5. Biểu đồ cột: Doanh thu (Mô phỏng dữ liệu tĩnh giữ nguyên do chưa có Hóa đơn)
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
        loadStatistics();
        System.out.println("Đã gửi yêu cầu cập nhật dữ liệu thống kê mới nhất qua Socket!");
    }
}