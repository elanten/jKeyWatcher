package my.project.watcher.service;

import my.project.watcher.model.Contragent;
import my.project.watcher.model.DigitalKey;

import java.util.List;

/**
 * Created by Morozov on 24.05.2017.
 */
public interface DigitalKeyService {
    List<DigitalKey> getByContragent(Contragent contragent, boolean withContacts);

    List<DigitalKey> getDigitalKeys();

    DigitalKey getDigitalKeyWithContacts(long id);

    DigitalKey getById(long id);

    void saveOrUpdate(DigitalKey digitalKey);

    void removeById(long id);
}
