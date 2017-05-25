package my.project.watcher.dao;

import my.project.watcher.model.Contragent;
import java.util.List;
import java.util.Map;

/**
 * Created by Morozov on 21.05.2017.
 */
public interface ContragentDAO {

    Contragent getById(long id);

    Contragent getByIdWithDetails(long id);

    List<Contragent> getAll();

    List<Contragent> getAllByName(String search);

    List<Contragent> getAllByIds(List<Long> ids);

    List<Contragent> getDigitalKeyContacts(long digitalKeyId, String contactType);

    void saveDigitalKeyContacts(long digitalKeyId, List<Contragent> contacts, String contactType);

    void removeDigitalKeyContacts(long digitalKeyId);

    void save(Contragent contragent);

    void update(Contragent contragent);

    Map<Long,String> getDetailTypes();

    void setRemoved(long pId);

    List<Contragent> getAllByType(String type);
}
