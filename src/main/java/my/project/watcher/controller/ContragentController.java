package my.project.watcher.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import my.project.watcher.model.Contragent;
import my.project.watcher.model.DigitalKey;
import my.project.watcher.service.ContragentService;
import my.project.watcher.service.DigitalKeyService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Morozov on 18.05.2017.
 */
@Controller
public class ContragentController {
    private Logger logger = LogManager.getLogger(getClass());

    @Autowired
    private ContragentService contragentService;

    @Autowired
    private DigitalKeyService digitalKeyService;

    @RequestMapping("/contragents")
    public String getAllPersons(
            @RequestParam(required = false) String holders,
            @RequestParam(required = false) String contacts,
            Model model
    ) {

        List<Contragent> contragents;
        if (holders != null) {
            contragents = contragentService
                    .getAllByType(DigitalKey.ContactType.HOLDER.toString());
        } else if (contacts != null) {
            contragents = contragentService
                    .getAllByType(DigitalKey.ContactType.CONTACT.toString());
        } else {
            contragents = contragentService.getAll();
        }
        model.addAttribute("contragents", contragents);
        return "contragent/list";
    }

    @RequestMapping("/contragents.json")
    @ResponseBody
    public List<Contragent> searchByName(
            @RequestParam String search
    ) {
        logger.info("search: {}", search);
        return contragentService.getListByName(search);
    }

    @RequestMapping("/contragents/{pId}")
    public String getPersonById(
            @PathVariable long pId,
            Model model
    ) {
        Contragent contragent = contragentService.getByIdWithDetails(pId);
        model.addAttribute("contragent", contragent);
        model.addAttribute("digitalKeys", digitalKeyService.getByContragent(contragent, true));
        return "contragent/detail";
    }

    @ResponseBody
    @RequestMapping("/contragents/{pId}.json")
    public Contragent test(
            @PathVariable long pId
    ) {
        return contragentService.getById(pId);
    }

    @RequestMapping("/contragents/edit/{pId}")
    public String edit(
            @PathVariable long pId,
            Model model
    ) throws JsonProcessingException {
        model.addAttribute("contragent", contragentService.getByIdWithDetails(pId));
        model.addAttribute("types", contragentService.getDetailTypes());
        return "contragent/edit";
    }

    @RequestMapping("contragents/new")
    public String create(
            Model model
    ) {
        model.addAttribute("contragent", new Contragent());
        model.addAttribute("types", contragentService.getDetailTypes());
        return "contragent/edit";
    }

    @PostMapping("contragents/save")
    public String save(
            @RequestParam long id,
            @RequestParam String name,
            @RequestParam(required = false) String description,
            @RequestParam(value = "ctype", required = false) List<Long> contactTypes,
            @RequestParam(value = "cvalue", required = false) List<String> contactValues
    ) {
        Contragent contragent = id == 0 ? new Contragent() : contragentService.getById(id);
        contragent.setName(name);
        contragent.setDescription(description);
        if (contactTypes != null && contactValues != null) {
            for (int i = 0; i < contactTypes.size() && i < contactValues.size(); i++) {
                String value = contactValues.get(i);
                if (value != null && !value.isEmpty()) {
                    Contragent.ContactDetail detail = new Contragent.ContactDetail();
                    detail.setTypeId(contactTypes.get(i));
                    detail.setValue(value);
                    contragent.getContactDetails().add(detail);
                }
            }
        }
        contragentService.saveOrUpdate(contragent);
        return "redirect:/contragents/" + contragent.getId();
    }

    @RequestMapping("/contragents/remove/{pId}")
    public String remove(
            @PathVariable long pId
    ) {
        contragentService.removeById(pId);
        return "redirect:/contragents";
    }
}
