package my.project.watcher.controller;

import com.sun.net.httpserver.HttpServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.support.ServletContextResource;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by Morozov on 25.05.2017.
 */
@Controller
public class DocumentController {
    Logger logger = LogManager.getLogger();

    @Autowired
    ResourceLoader resourceLoader;

    @RequestMapping("/docs/add")
    public String loadForm() {
        return "test";
    }

    @RequestMapping("/docs/upload")
    public String handleLoad(
            @RequestParam CommonsMultipartFile file,
            RedirectAttributes redirectAttributes
    ) throws IOException {
        redirectAttributes.addFlashAttribute("file", file);
        Resource resource = resourceLoader.getResource("/WEB-INF/docs/file.jpg");
        File file1 = resource.getFile();
        logger.info("resource.exists(): {}", resource.exists());
        logger.info("resource.getFilename(): {}", resource.getFilename());

        logger.info("file.getContentType() {}", file.getContentType());
        logger.info("file.getName() {}", file.getName());
        logger.info("file.getStorageDescription() {}", file.getStorageDescription());

        file.transferTo(file1);
        return "redirect:/docs/add";
    }


    @RequestMapping("/docs/get/file.jpg")
    @ResponseBody
    public FileSystemResource getFile(
    ) throws IOException{
        Resource resource = resourceLoader.getResource("/WEB-INF/docs/file.jpg");
        return new FileSystemResource(resource.getFile());
    }
}
