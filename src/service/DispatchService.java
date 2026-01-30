package service;

import model.DispatchRecord;
import model.Inventory;
import repository.DispatchRepository;
import repository.InventoryRepository;

import java.util.Date;
import java.util.UUID;

/**
 * Service class for handling Vehicle Dispatch logic.
 */
public class DispatchService {

    private final DispatchRepository dispatchRepository;
    private final InventoryRepository inventoryRepository;

    /**
     * Constructor Injection of required repositories.
     * @param dispatchRepository For saving dispatch records.
     * @param inventoryRepository For checking and updating stock.
     */
    public DispatchService(DispatchRepository dispatchRepository, InventoryRepository inventoryRepository) {
        this.dispatchRepository = dispatchRepository;
        this.inventoryRepository = inventoryRepository;
    }

    /**
     * Dispatches a vehicle with a specific amount of coal.
     * Validates stock availability before processing.
     * @param vehicleId The ID of the vehicle.
     * @param coalType The type of coal to dispatch.
     * @param quantity The amount to dispatch.
     * @return true if dispatch was successful, false otherwise.
     */
    public boolean dispatchVehicle(String vehicleId, String coalType, double quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive.");
        }

        // 1. Check Inventory
        Inventory inventory = inventoryRepository.findByType(coalType);
        if (inventory == null) {
            System.err.println("Error: Coal type not found: " + coalType);
            return false;
        }

        if (inventory.getCurrentStock() < quantity) {
            System.err.println("Error: Insufficient stock. Available: " + inventory.getCurrentStock());
            return false;
        }

        // 2. Reduce Inventory
        double newStock = inventory.getCurrentStock() - quantity;
        inventory.setCurrentStock(newStock);
        inventoryRepository.update(inventory);

        // 3. Create Dispatch Record
        String dispatchId = UUID.randomUUID().toString();
        DispatchRecord record = new DispatchRecord(dispatchId, vehicleId, quantity, new Date());
        
        // 4. Save Record
        dispatchRepository.save(record);
        
        System.out.println("Dispatch successful for Vehicle: " + vehicleId);
        return true;
    }
}
