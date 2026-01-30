
package service;

import model.DispatchRecord;
import model.Inventory;
import repository.DispatchRepository;
import repository.InventoryRepository;

import java.util.List;

/**
 * Service class for generating reports.
 * Aggregates data from various repositories to produce summaries.
 */
public class ReportService {

    private final InventoryRepository inventoryRepository;
    private final DispatchRepository dispatchRepository;

    /**
     * Constructor Injection.
     * @param inventoryRepository For inventory data.
     * @param dispatchRepository For dispatch history.
     */
    public ReportService(InventoryRepository inventoryRepository, DispatchRepository dispatchRepository) {
        this.inventoryRepository = inventoryRepository;
        this.dispatchRepository = dispatchRepository;
    }

    /**
     * Generates a summary of the current inventory.
     * @return A formatted string string containing inventory details.
     */
    public String generateInventorySummary() {
        List<Inventory> inventoryList = inventoryRepository.findAll();
        StringBuilder sb = new StringBuilder();
        sb.append("=== INVENTORY SUMMARY ===\n");
        sb.append(String.format("%-20s %-10s\n", "Coal Type", "Stock (Tons)"));
        sb.append("--------------------------------\n");
        
        for (Inventory item : inventoryList) {
            sb.append(String.format("%-20s %-10.2f\n", item.getCoalType(), item.getCurrentStock()));
        }
        sb.append("================================\n");
        return sb.toString();
    }

    /**
     * Generates a summary of all vehicle dispatches.
     * @return A formatted string containing dispatch history.
     */
    public String generateDispatchSummary() {
        List<DispatchRecord> records = dispatchRepository.findAll();
        StringBuilder sb = new StringBuilder();
        sb.append("=== DISPATCH SUMMARY ===\n");
        sb.append(String.format("%-20s %-15s %-10s %-20s\n", "Dispatch ID", "Vehicle ID", "Qty", "Date"));
        sb.append("----------------------------------------------------------------------\n");

        for (DispatchRecord record : records) {
            sb.append(String.format("%-20s %-15s %-10.2f %-20s\n", 
                record.getDispatchId(), 
                record.getVehicleId(), 
                record.getQuantity(), 
                record.getDispatchDate()));
        }
        sb.append("======================================================================\n");
        return sb.toString();
    }
}
