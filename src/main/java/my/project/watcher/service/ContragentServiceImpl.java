package my.project.watcher.service;

import my.project.watcher.dao.ContragentDAO;
import my.project.watcher.model.Contragent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by Morozov on 18.05.2017.
 */
@Service
public class ContragentServiceImpl implements ContragentService {
    @Autowired
    private ContragentDAO contragentDAO;

    @Override
    public Map<Long,String> getDetailTypes(){
        return contragentDAO.getDetailTypes();
    }

    @Override
    public Contragent getById(long pId) {
        return contragentDAO.getById(pId);
    }

    @Override
    public List<Contragent> getAll() {
        return contragentDAO.getAll();
    }

    @Override
    public List<Contragent> getAllByType(String type) {
        return contragentDAO.getAllByType(type);
    }

    @Override
    public Contragent getByIdWithDetails(long pId) {
        return contragentDAO.getByIdWithDetails(pId);
    }

    @Override
    public List<Contragent> getListByName(String search) {
        return contragentDAO.getAllByName(search);
    }

    @Override
    public List<Contragent> getListByIds(List<Long> ids) {
        return contragentDAO.getAllByIds(ids);
    }

    @Override
    public void saveOrUpdate(Contragent contragent) {
        if(contragent.getId() == 0 ){
            contragentDAO.save(contragent);
        }else {
            contragentDAO.update(contragent);
        }
    }

    @Override
    public void removeById(long pId) {
        contragentDAO.setRemoved(pId);
    }
}
