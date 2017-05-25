package my.project.watcher.model;

import com.fasterxml.jackson.annotation.JsonView;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Morozov on 18.05.2017.
 */
public class DigitalKey {

    @JsonView(Summary.class)
    private long id;
    private String serial;
    @JsonView(Summary.class)
    private String name;
    private Date expire;
    private String description;
    private List<Contragent> holders = new ArrayList<>();
    private List<Contragent> contacts = new ArrayList<>();
    private State state = State.EXPIRED;

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getExpire() {
        return expire;
    }

    public void setExpire(Date expire) {
        DateTime dateTime = new DateTime(expire);
        if (dateTime.minusMonths(2).isAfterNow()) {
            setState(State.NORMAL);
        } else if (dateTime.minusMonths(1).isAfterNow()) {
            setState(State.WARNING);
        }
        this.expire = expire;
    }

    public List<Contragent> getHolders() {
        return holders;
    }

    public void setHolders(List<Contragent> holders) {
        this.holders = holders;
    }

    public List<Contragent> getContacts() {
        return contacts;
    }

    public void setContacts(List<Contragent> contacts) {
        this.contacts = contacts;
    }

    public enum State {
        NORMAL,
        WARNING,
        EXPIRED
    }

    public enum ContactType {
        HOLDER,
        CONTACT
    }

    public interface Summary {

    }
}
