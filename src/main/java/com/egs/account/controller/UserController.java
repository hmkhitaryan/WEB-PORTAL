package com.egs.account.controller;

import com.egs.account.error.DocumentNotFoundException;
import com.egs.account.error.UserNotFoundException;
import com.egs.account.model.Catalog;
import com.egs.account.model.FileBucket;
import com.egs.account.model.User;
import com.egs.account.service.catalog.CatalogService;
import com.egs.account.service.user.UserService;
import com.egs.account.utils.domainUtlis.DomainUtils;
import com.egs.account.validator.FileValidator;
import com.egs.account.validator.UserValidator;
import com.egs.account.service.security.SecurityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@Controller
public class UserController {

    private UserService userService;

    @Autowired
    private CatalogService catalogService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private UserValidator userValidator;

    @Autowired
    private HttpServletRequest context;

    @Autowired
    FileValidator fileValidator;

    @Autowired
    MessageSource messageSource;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    DomainUtils domainUtils;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @InitBinder("fileBucket")
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(fileValidator);
    }

    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public String registration(Model model) {
        model.addAttribute("userForm", new User());

        return "registration";
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public String registration(@ModelAttribute("userForm") User userForm, BindingResult bindingResult) {
        userValidator.validate(userForm, bindingResult);

        if (bindingResult.hasErrors()) {
            return "registration";
        }
        userService.saveUser(userForm);
        securityService.autoLogin(userForm.getUsername(), userForm.getPasswordConfirm());
        LOGGER.info("user with username {} successfully registered", userForm.getUsername());

        return "redirect:/welcome";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Model model, String error, String logout) {
        if (error != null)
            model.addAttribute("error", "Your username and password is invalid.");

        if (logout != null)
            model.addAttribute("message", "You have been logged out successfully.");

        return "login";
    }

    @RequestMapping(value = "/toMap", method = RequestMethod.GET)
    public ModelAndView getPages() {

        return new ModelAndView("map");
    }

    @RequestMapping(value = {"/", "/welcome"}, method = RequestMethod.GET)
    public String welcome(Model model) {
        String userName = context.getUserPrincipal().getName();
        User userForm = userService.findByUsername(userName);
        if (userForm == null) {
            throw new UserNotFoundException(String.format("No user found with this userName %s", userName));
        }
        model.addAttribute("userForm", userForm);

        return "welcomeUser";
    }

    @RequestMapping(value = {"/edit-user-{id}"}, method = RequestMethod.GET)
    public String editUser(@PathVariable Long id, ModelMap model) {
        User user = userService.findById(id);
        try {
            if (user.getUsername() != null && !user.getUsername().equals(context.getUserPrincipal().getName())) {
                return "login";
            }
        } catch (Exception ex) {
            throw new UserNotFoundException();
        }
        model.addAttribute("userForm", user);

        return "editUser";
    }

    @RequestMapping(value = {"/edit-user-{id}"}, method = RequestMethod.POST)
    public String updateUser(@Valid User userForm, BindingResult bindingResult, ModelMap model, @PathVariable Long id) {
        userValidator.validateEdit(userForm, bindingResult);

        if (bindingResult.hasErrors()) {
            return "redirect:/edit-user-" + id;
        }
        userService.updateUser(userForm);
        model.addAttribute("userForm", userForm);
        model.addAttribute("success", "User " + userForm.getFirstName() + " " + userForm.getLastName() + " updated successfully");

        return "registrationSuccess";
    }

    @RequestMapping(value = {"/delete-user-{id}"}, method = RequestMethod.GET)
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);

        return "deleteSuccess";
    }

    @RequestMapping(value = {"/add-document-{id}"}, method = RequestMethod.GET)
    public String addDocuments(@PathVariable Long id, ModelMap model) {
        User user = userService.findById(id);
        try {
            if (user.getUsername() != null && !user.getUsername().equals(context.getUserPrincipal().getName())) {
                return "login";
            }
        } catch (Exception ex) {
            throw new DocumentNotFoundException("No document found with that id or userName");
        }
        model.addAttribute("user", user);
        FileBucket fileModel = new FileBucket();
        model.addAttribute("fileBucket", fileModel);
        List<Catalog> documents = catalogService.findAllByUserId(id);
        model.addAttribute("documents", documents);

        return "manageDocuments";
    }

    @RequestMapping(value = {"/download-document-{userId}-{docId}"}, method = RequestMethod.GET)
    public String downloadDocument(@PathVariable Long userId, @PathVariable Long docId, HttpServletResponse response) throws IOException {
        domainUtils.downloadDocument(response, docId);

        return "redirect:/add-document-" + userId;
    }

    @RequestMapping(value = {"/delete-document-{userId}-{docId}"}, method = RequestMethod.GET)
    public String deleteDocument(@PathVariable Long userId, @PathVariable Long docId) {
        catalogService.deleteById(docId);

        return "redirect:/add-document-" + userId;
    }

    @RequestMapping(value = {"/add-document-{userId}"}, method = RequestMethod.POST)
    public String uploadDocument(@Valid FileBucket fileBucket, BindingResult result, ModelMap model, @PathVariable Long userId) throws IOException {

        return domainUtils.uploadDocument(fileBucket, result, model, userId);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ModelAndView handleUserError() {
        ModelAndView modelAndView = new ModelAndView();
        String domain = messageSource.getMessage("account.user.label", null, null);
        modelAndView.addObject("domain", domain);
        modelAndView.setViewName("notFound");

        return modelAndView;
    }

    @ExceptionHandler(DocumentNotFoundException.class)
    public ModelAndView handleDocumentError() {
        ModelAndView modelAndView = new ModelAndView();
        String domain = messageSource.getMessage("account.document.label", null, null);
        modelAndView.addObject("domain", domain);
        modelAndView.setViewName("notFound");

        return modelAndView;
    }
}