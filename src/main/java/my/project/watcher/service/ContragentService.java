package my.project.watcher.service;

import my.project.watcher.model.Contragent;

import java.util.List;
import java.util.Map;

/**
 * Created by Morozov on 25.05.2017.
 */
public interface ContragentService {
    Map<Long,String> getDetailTypes();

    Contragent getById(long pId);

    List<Contragent> getAll();

    List<Contragent> getAllByType(String type);

    Contragent getByIdWithDetails(long pId);

    List<Contragent> getListByName(String search);

    List<Contragent> getListByIds(List<Long> ids);

    void saveOrUpdate(Contragent contragent);

    void removeById(long pId);
}
