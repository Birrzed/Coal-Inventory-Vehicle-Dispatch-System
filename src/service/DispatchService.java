package service;

import model.Dispatch;
import java.util.List;

public interface DispatchService {
    void createDispatch(double mass, int sellerId, int transporterId, int destinationId);

    void confirmDispatch(int dispatchId);

    void confirmArrival(int dispatchId);

    List<Dispatch> getDispatchesForUser(String role, int userId);

    List<Dispatch> getAllDispatches();
}
