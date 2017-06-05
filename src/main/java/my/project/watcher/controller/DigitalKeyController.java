package my.project.watcher.controller;

import com.fasterxml.jackson.annotation.JsonView;
import my.project.watcher.model.DigitalKey;
import my.project.watcher.service.ContragentService;
import my.project.watcher.service.DigitalKeyService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;
import java.util.List;


/**
 * Created by Morozov on 18.05.2017.
 */
@Controller
public class DigitalKeyController {
    private Logger logger = LogManager.getLogger(getClass());

    @Autowired
    private DigitalKeyService digitalKeyService;

    @Autowired
    private ContragentService contragentService;

    @RequestMapping("/")
    public String index() {
        return "redirect:/keys";
    }

    @RequestMapping("/keys")
    public String showAll(
            Model model
    ) {
        model.addAttribute("digitalKeys", digitalKeyService.getDigitalKeys());
        return "digitalKey/list";
    }


    @RequestMapping("/keys/{keyId}")
    public String showById(
            @PathVariable long keyId,
            Model model
    ) {
        model.addAttribute("digitalKey", digitalKeyService.getDigitalKeyWithContacts(keyId));
        return "digitalKey/detail";
    }

    @RequestMapping("/keys/edit/{keyId}")
    public String edit(
            @PathVariable long keyId,
            Model model
    ) {
        model.addAttribute("digitalKey", digitalKeyService.getDigitalKeyWithContacts(keyId));
        return "digitalKey/edit";
    }

    @RequestMapping("keys/new")
    public String create(
            Model model
    ) {
        model.addAttribute("digitalKey", new DigitalKey());
        return "digitalKey/edit";
    }

    @PostMapping("keys/save")
    public String save(
            @RequestParam long keyId,
            @RequestParam String name,
            @RequestParam String serial,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date expire,
            @RequestParam(required = false) List<Long> holders,
            @RequestParam(required = false) List<Long> contacts,
            @RequestParam(required = false) String description
    ) {
        DigitalKey digitalKey = keyId == 0 ? new DigitalKey() : digitalKeyService.getById(keyId);
        digitalKey.setName(name);
        digitalKey.setSerial(serial);
        digitalKey.setExpire(expire);
        digitalKey.setDescription(description);
        digitalKey.setHolders(contragentService.getListByIds(holders));
        digitalKey.setContacts(contragentService.getListByIds(contacts));
        digitalKeyService.saveOrUpdate(digitalKey);
        return "redirect:/keys/" + digitalKey.getId();
    }

    @RequestMapping("/keys/remove/{keyId}")
    public String remove(
            @PathVariable long keyId
    ){
        digitalKeyService.removeById(keyId);
        return "redirect:/keys";
    }

    @JsonView(DigitalKey.Summary.class)
    @RequestMapping("/keys/test.json")
    @ResponseBody
    public List<DigitalKey> test() {
        return digitalKeyService.getDigitalKeys();
    }
}
