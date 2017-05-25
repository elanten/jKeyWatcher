package my.project.watcher.service;

import my.project.watcher.dao.ContragentDAO;
import my.project.watcher.dao.DigitalKeyDAO;
import my.project.watcher.model.Contragent;
import my.project.watcher.model.DigitalKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Morozov on 18.05.2017.
 */
@Service
public class DigitalKeyServiceImpl implements DigitalKeyService {

    @Autowired
    private DigitalKeyDAO digitalKeyDAO;

    @Autowired
    private ContragentDAO contragentDAO;

    @Override
    public List<DigitalKey> getByContragent(Contragent contragent, boolean withContacts) {
        if (contragent == null) {
            return null;
        }
        List<DigitalKey> list = digitalKeyDAO.getAllByContragentId(contragent.getId());
        if (withContacts) {
            for (DigitalKey key : list) {
                long id = key.getId();
                key.setHolders(contragentDAO.getDigitalKeyContacts(id, DigitalKey.ContactType.HOLDER.toString()));
                key.setContacts(contragentDAO.getDigitalKeyContacts(id, DigitalKey.ContactType.CONTACT.toString()));
            }
        }
        return list;
    }

    @Override
    public List<DigitalKey> getDigitalKeys() {
        return digitalKeyDAO.getAll();
    }

    @Override
    public DigitalKey getDigitalKeyWithContacts(long id) {
        DigitalKey digitalKey = getById(id);
        digitalKey.setContacts(contragentDAO.getDigitalKeyContacts(id, DigitalKey.ContactType.CONTACT.toString()));
        digitalKey.setHolders(contragentDAO.getDigitalKeyContacts(id, DigitalKey.ContactType.HOLDER.toString()));
        return digitalKey;
    }

    @Override
    public DigitalKey getById(long id) {
        return digitalKeyDAO.getById(id);
    }

    @Override
    public void saveOrUpdate(DigitalKey digitalKey) {
        if (digitalKey == null) return;
        if (digitalKey.getId() == 0) {
            digitalKeyDAO.save(digitalKey);
        } else {
            digitalKeyDAO.update(digitalKey);
            contragentDAO.removeDigitalKeyContacts(digitalKey.getId());
        }
        contragentDAO.saveDigitalKeyContacts(
                digitalKey.getId(), digitalKey.getHolders(), DigitalKey.ContactType.HOLDER.toString());
        contragentDAO.saveDigitalKeyContacts(
                digitalKey.getId(), digitalKey.getContacts(), DigitalKey.ContactType.CONTACT.toString());
    }

    @Override
    public void removeById(long digitalKeyId) {
        digitalKeyDAO.setRemoved(digitalKeyId);
    }
}
