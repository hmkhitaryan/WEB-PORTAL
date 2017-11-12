package com.egs.account.controller;

import com.egs.account.event.OnRegistrationCompleteEvent;
import com.egs.account.exception.DocumentNotFoundException;
import com.egs.account.exception.EmailExistsException;
import com.egs.account.exception.UserNotFoundException;
import com.egs.account.mapping.UIAttribute;
import com.egs.account.mapping.UrlMapping;
import com.egs.account.model.Catalog;
import com.egs.account.model.FileBucket;
import com.egs.account.model.User;
import com.egs.account.model.VerificationToken;
import com.egs.account.service.catalog.CatalogService;
import com.egs.account.service.security.SecurityService;
import com.egs.account.service.user.UserService;
import com.egs.account.utils.domainUtils.DomainUtils;
import com.egs.account.validator.FileValidator;
import com.egs.account.validator.UserValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static com.egs.account.mapping.UIAttribute.NOT_FOUND;

/**
 * @author Hayk_Mkhitaryan
 */
@Controller
public class UserController {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

	private static final String SLASH_SIGN = "/";

	@Autowired
	FileValidator fileValidator;

	@Autowired
	MessageSource messageSource;

	@Autowired
	DomainUtils domainUtils;

	@Autowired
	ApplicationEventPublisher eventPublisher;

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
	public UserController(UserService userService) {
		this.userService = userService;
	}

	@InitBinder("fileBucket")
	protected void initBinder(WebDataBinder binder) {
		binder.setValidator(fileValidator);
	}

	@RequestMapping(value = UrlMapping.REGISTRATION, method = RequestMethod.GET)
	public String registration(Model model) {
		model.addAttribute(UIAttribute.USER_FORM, new User());

		return UrlMapping.REGISTRATION_DESTINATION_JSP;
	}

//	@RequestMapping(value = UrlMapping.REGISTRATION, method = RequestMethod.POST)
//	public String registration(@ModelAttribute(UIAttribute.USER_FORM) User userForm, BindingResult bindingResult) {
//		userValidator.validate(userForm, bindingResult);
//
//		if (bindingResult.hasErrors()) {
//			return UrlMapping.REGISTRATION_DESTINATION_JSP;
//		}
//		userService.saveUser(userForm);
//		securityService.autoLogin(userForm.getUsername(), userForm.getPasswordConfirm());
//		LOGGER.info("user with username {} successfully registered", userForm.getUsername());
//
//		return UrlMapping.WELCOME_REDIRECT_JSP;
//	}

	@RequestMapping(value = UrlMapping.REGISTRATION, method = RequestMethod.POST)
	public String registerUserAccount(
			@ModelAttribute("user") @Valid User accountDto,
			BindingResult bindingResult,
			WebRequest request) throws EmailExistsException {

		if (bindingResult.hasErrors()) {
			return UrlMapping.REGISTRATION_DESTINATION_JSP;
		}

		User registered = userService.registerNewUserAccount(accountDto);
		if (registered == null) {
			bindingResult.rejectValue("email", "message.regError");
		}
		try {
			String appUrl = request.getContextPath();
			eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registered, request.getLocale(), appUrl));
		} catch (Exception me) {
			//return new ModelAndView("emailError", "user", accountDto);
		}
		securityService.autoLogin(registered.getUsername(), registered.getPasswordConfirm());

		return UrlMapping.WELCOME_REDIRECT_JSP;
	}

	@RequestMapping(value = "/regitrationConfirm", method = RequestMethod.GET)
	public String confirmRegistration
			(WebRequest request, Model model, @RequestParam("token") String token) {

		Locale locale = request.getLocale();

		VerificationToken verificationToken = userService.getVerificationToken(token);
		if (verificationToken == null) {
			String message = messageSource.getMessage("auth.message.invalidToken", null, locale);
			model.addAttribute("message", message);
			return "redirect:/badUser.html?lang=" + locale.getLanguage();
		}

		User user = verificationToken.getUser();
		Calendar cal = Calendar.getInstance();
		if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
			String messageValue = messageSource.getMessage("auth.message.expired", null, locale);
			model.addAttribute("message", messageValue);
			return "redirect:/badUser.html?lang=" + locale.getLanguage();
		}

		user.setEnabled(true);
		userService.saveRegisteredUser(user);
		return "redirect:/login.html?lang=" + request.getLocale().getLanguage();
	}

	@RequestMapping(value = UrlMapping.LOGIN, method = RequestMethod.GET)
	public String login(Model model, String error, String logout) {
		if (error != null) {
			model.addAttribute(UIAttribute.ERROR, "Your username and password is invalid.");
		}

		if (logout != null) {
			model.addAttribute(UIAttribute.MESSAGE, "You have been logged out successfully.");
		}

		return UrlMapping.LOGIN_DESTINATION_JSP;
	}

	@RequestMapping(value = UrlMapping.TO_MAP, method = RequestMethod.GET)
	public ModelAndView getPages() {
		return new ModelAndView("map");
	}

	@RequestMapping(value = {UrlMapping.ROOT, UrlMapping.WELCOME}, method = RequestMethod.GET)
	public String welcome(Model model) {
		final String userName = context.getUserPrincipal().getName();
		final User userForm = userService.findByUsername(userName);
		if (userForm == null) {
			throw new UserNotFoundException(String.format("No user found with this userName %s", userName));
		}
		model.addAttribute(UIAttribute.USER_FORM, userForm);

		return UrlMapping.WELCOME_DESTINATION_JSP;
	}

	@RequestMapping(value = {UrlMapping.EDIT_USER + "/{id}"}, method = RequestMethod.GET)
	public String editUser(@PathVariable Long id, ModelMap model) {
		final User user = userService.findById(id);
		try {
			if (user.getUsername() != null && !user.getUsername().equals(context.getUserPrincipal().getName())) {
				return UrlMapping.LOGIN_DESTINATION_JSP;
			}
		} catch (Exception ex) {
			throw new UserNotFoundException();
		}
		model.addAttribute(UIAttribute.USER_FORM, user);

		return UrlMapping.EDIT_USER_DESTINATION_JSP;
	}

	@RequestMapping(value = {UrlMapping.EDIT_USER + "/{id}"}, method = RequestMethod.POST)
	public String updateUser(@Valid User userForm, BindingResult bindingResult, ModelMap model, @PathVariable Long id) {
		userValidator.validateEdit(userForm, bindingResult);

		if (bindingResult.hasErrors()) {
			return UrlMapping.EDIT_USER_REDIRECT_JSP + id;
		}
		userService.updateUser(userForm);
		model.addAttribute(UIAttribute.USER_FORM, userForm);
		model.addAttribute(UIAttribute.SUCCESS, "User " + userForm.getFirstName() + " " + userForm.getLastName() +
				" updated successfully");

		return UrlMapping.REGISTRATION_SUCCESS_DESTINATION_JSP;
	}

	@RequestMapping(value = {UrlMapping.DELETE_USER + "/{id}"}, method = RequestMethod.GET)
	public String deleteUser(@PathVariable Long id) {
		userService.deleteUserById(id);

		return UrlMapping.DELETE_SUCCESS_DESTINATION_JSP;
	}

	@RequestMapping(value = {UrlMapping.ADD_DOCUMENT + "/{id}"}, method = RequestMethod.GET)
	public String addDocuments(@PathVariable Long id, ModelMap model) {
		final User user = userService.findById(id);
		try {
			boolean isLoggedInUser = user.getUsername() != null && user.getUsername().equals(context.getUserPrincipal().getName());
			if (!isLoggedInUser) {
				return UrlMapping.LOGIN_DESTINATION_JSP;
			}
		} catch (Exception ex) {
			throw new DocumentNotFoundException("No document found with that id or userName");
		}
		model.addAttribute(UIAttribute.USER, user);
		final FileBucket fileModel = new FileBucket();
		model.addAttribute(UIAttribute.FILE_BUCKET, fileModel);
		final List<Catalog> documents = catalogService.findAllByUserId(id);
		model.addAttribute(UIAttribute.DOCUMENTS, documents);

		return UrlMapping.MANAGE_DOC_DESTINATION_JSP;
	}

	@RequestMapping(value = {UrlMapping.DOWNLOAD_DOCUMENT + "/{userId}/{docId}"}, method = RequestMethod.GET)
	public String downloadDocument(@PathVariable Long userId, @PathVariable Long docId, HttpServletResponse response)
			throws IOException {
		domainUtils.downloadDocument(response, docId);

		return UrlMapping.ADD_DOC_REDIRECT_JSP + SLASH_SIGN + userId;
	}

	@RequestMapping(value = {UrlMapping.DELETE_DOCUMENT + "/{userId}/{docId}"}, method = RequestMethod.GET)
	public String deleteDocument(@PathVariable Long userId, @PathVariable Long docId) {
		catalogService.deleteById(docId);

		return UrlMapping.ADD_DOC_REDIRECT_JSP + SLASH_SIGN + userId;
	}

	@RequestMapping(value = {UrlMapping.ADD_DOCUMENT + "/{userId}"}, method = RequestMethod.POST)
	public String uploadDocument(@Valid FileBucket fileBucket, BindingResult result, ModelMap model, @PathVariable Long userId)
			throws IOException {

		return domainUtils.uploadDocument(fileBucket, result, model, userId);
	}

	@ExceptionHandler(UserNotFoundException.class)
	public ModelAndView handleUserError() {
		final ModelAndView modelAndView = new ModelAndView();
		final String domain = messageSource.getMessage("account.user.label", null, null);
		modelAndView.addObject(UIAttribute.DOMAIN, domain);
		modelAndView.setViewName(NOT_FOUND);

		return modelAndView;
	}

	@ExceptionHandler(DocumentNotFoundException.class)
	public ModelAndView handleDocumentError() {
		final ModelAndView modelAndView = new ModelAndView();
		final String domain = messageSource.getMessage("account.document.label", null, null);
		modelAndView.addObject(UIAttribute.DOMAIN, domain);
		modelAndView.setViewName(NOT_FOUND);

		return modelAndView;
	}
}