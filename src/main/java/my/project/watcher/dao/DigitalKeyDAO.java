package my.project.watcher.dao;

import my.project.watcher.model.DigitalKey;

import java.util.List;

/**
 * Created by Morozov on 21.05.2017.
 */
public interface DigitalKeyDAO {

    DigitalKey getById(long id);

    List<DigitalKey> getAll();

    List<DigitalKey> getAllByContragentId(long contragentId);

    void update(DigitalKey digitalKey);

    void save(DigitalKey digitalKey);

    void setRemoved(long id);
}
