package service;

import model.Inventory;
import repository.InventoryRepository;

import java.util.List;

/**
 * Service class for managing Inventory-related business logic.
 * Follows SRP: Handles only inventory stock operations.
 */
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    /**
     * Constructor Injection for Dependency Inversion Principle.
     * @param inventoryRepository The repository to handle data persistence.
     */
    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    /**
     * Adds stock to an existing inventory item or creates a new one (if supported).
     * @param coalType The type of coal to add.
     * @param amount The amount to add (must be positive).
     */
    public void addStock(String coalType, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive.");
        }

        Inventory inventory = inventoryRepository.findByType(coalType);
        if (inventory != null) {
            double newStock = inventory.getCurrentStock() + amount;
            inventory.setCurrentStock(newStock);
            inventoryRepository.update(inventory);
        } else {
            // Handle new item logic if required or throw error
            // For now, let's assume valid types must exist or we create new
            Inventory newInventory = new Inventory(coalType, amount);
            inventoryRepository.update(newInventory);
        }
    }

    /**
     * Removes stock from inventory if sufficient quantity is available.
     * @param coalType The type of coal to remove.
     * @param amount The amount to remove (must be positive).
     * @return true if successful, false if insufficient stock.
     */
    public boolean removeStock(String coalType, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive.");
        }

        Inventory inventory = inventoryRepository.findByType(coalType);
        if (inventory != null) {
            if (inventory.getCurrentStock() >= amount) {
                double newStock = inventory.getCurrentStock() - amount;
                inventory.setCurrentStock(newStock);
                inventoryRepository.update(inventory);
                return true;
            } else {
                return false; // Insufficient stock
            }
        }
        return false; // Item not found
    }

    /**
     * Checks the available stock for a specific coal type.
     * @param coalType The type of coal.
     * @return The available stock amount, or 0.0 if not found.
     */
    public double checkAvailableStock(String coalType) {
        Inventory inventory = inventoryRepository.findByType(coalType);
        if (inventory != null) {
            return inventory.getCurrentStock();
        }
        return 0.0;
    }

    /**
     * Retrieves all inventory items for reporting or display.
     * @return List of all Inventory items.
     */
    public List<Inventory> getAllInventory() {
        return inventoryRepository.findAll();
    }
}
