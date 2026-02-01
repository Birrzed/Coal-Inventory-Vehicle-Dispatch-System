package service.impl;

import dao.DispatchDAO;
import model.Dispatch;
import service.DispatchService;
import java.sql.Date;
import java.util.List;

public class DispatchServiceImpl implements DispatchService {
    private DispatchDAO dispatchDAO;

    public DispatchServiceImpl() {
        this.dispatchDAO = new DispatchDAO();
    }

    @Override
    public void createDispatch(double mass, int sellerId, int transporterId, int destinationId) {
        // Dispatch date is NOW
        Date today = new Date(System.currentTimeMillis());
        Dispatch dispatch = new Dispatch(0, mass, today, null, null, sellerId, transporterId, destinationId, "Pending");
        dispatchDAO.createDispatch(dispatch);
    }

    @Override
    public void confirmDispatch(int dispatchId) {
        Date today = new Date(System.currentTimeMillis());
        dispatchDAO.updateConfirmDate(dispatchId, today);
    }

    @Override
    public void confirmArrival(int dispatchId) {
        Date today = new Date(System.currentTimeMillis());
        dispatchDAO.updateArrivalDate(dispatchId, today);
    }

    @Override
    public List<Dispatch> getDispatchesForUser(String role, int userId) {
        return dispatchDAO.getDispatchesByRole(role, userId);
    }

    @Override
    public List<Dispatch> getAllDispatches() {
        return dispatchDAO.getDispatchesByRole("Admin", 0);
    }
}
