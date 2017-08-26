package com.egs.account.utils.domainUtils;

import com.egs.account.model.Catalog;
import com.egs.account.model.FileBucket;
import com.egs.account.model.User;
import com.egs.account.service.catalog.CatalogService;
import com.egs.account.service.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;

public class DomainUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(DomainUtils.class);

    @Autowired
    private CatalogService catalogService;

    @Autowired
    private UserService userService;

    public void saveDocument(FileBucket fileBucket, User user) throws IOException {
        Catalog document = new Catalog();
        MultipartFile multipartFile = fileBucket.getFile();

        document.setLink(multipartFile.getOriginalFilename());
        document.setComment(fileBucket.getComment());
        document.setContent(multipartFile.getBytes());
        document.setType(multipartFile.getContentType());
        document.setInsertDate(new Date());
        document.setUser(user);

        catalogService.saveDocument(document);
    }

    public void downloadDocument(HttpServletResponse response, Long docId) throws IOException {
        Catalog document = catalogService.findById(docId);
        response.setContentType(document.getType());
        response.setContentLength(document.getContent().length);
        response.setHeader("Content-Disposition", "attachment; filename=\"" + document.getLink() + "\"");

        FileCopyUtils.copy(document.getContent(), response.getOutputStream());
    }

    public String uploadDocument(FileBucket fileBucket, BindingResult result, ModelMap model, Long userId) throws IOException {

        if (result.hasErrors()) {
            LOGGER.error("validation errors");
            User user = userService.findById(userId);
            model.addAttribute("user", user);
            List<Catalog> documents = catalogService.findAllByUserId(userId);
            model.addAttribute("documents", documents);

            return "manageDocuments";
        } else {
            LOGGER.info("Fetching file");
            User user = userService.findById(userId);
            model.addAttribute("user", user);
            saveDocument(fileBucket, user);

            return "redirect:/add-document-" + userId;
        }
    }
}
