package service;

import model.DemandRequest;
import model.Inventory;
import repository.DemandRepository;
import repository.InventoryRepository;

/**
 * Service class for managing Demand Requests.
 */
public class DemandService {

    private final DemandRepository demandRepository;
    private final InventoryRepository inventoryRepository;

    /**
     * Constructor Injection.
     * @param demandRepository For retrieving and updating requests.
     * @param inventoryRepository For checking stock availability.
     */
    public DemandService(DemandRepository demandRepository, InventoryRepository inventoryRepository) {
        this.demandRepository = demandRepository;
        this.inventoryRepository = inventoryRepository;
    }

    /**
     * Processes a demand request by approving or rejecting it based on inventory.
     * @param requestId The ID of the demand request.
     * @param coalType The type of coal requested (assuming DemandRequest is linked to a type, 
     *                 though model currently lacks it. Will assume passed as arg or add to model validation).
     *                 For this implementation, we'll assume the request *would* have a type, 
     *                 but since the model is simple, we'll pass it here or assume a default for demo.
     *                 Let's pass it as a parameter for clarity.
     * @return The updated status ("APPROVED" or "REJECTED").
     */
    public String processDemand(String requestId, String coalType) {
        DemandRequest request = demandRepository.findById(requestId);
        
        if (request == null) {
            throw new IllegalArgumentException("Demand Request not found: " + requestId);
        }

        if (!"PENDING".equalsIgnoreCase(request.getStatus())) {
            return request.getStatus(); // Already processed
        }

        Inventory inventory = inventoryRepository.findByType(coalType);
        
        // Business Rule: Approve if inventory exists and sufficient stock is available.
        // Note: This does NOT reserve or reduce stock (based on prompt requirements), 
        // merely checks feasibility. DispatchService handles reduction.
        if (inventory != null && inventory.getCurrentStock() >= request.getQuantity()) {
            request.setStatus("APPROVED");
        } else {
            request.setStatus("REJECTED");
        }

        demandRepository.save(request);
        return request.getStatus();
    }
}
