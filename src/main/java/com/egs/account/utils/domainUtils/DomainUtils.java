package com.egs.account.utils.domainUtils;

import com.egs.account.mapping.UIAttribute;
import com.egs.account.mapping.UrlMapping;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;

public class DomainUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(DomainUtils.class);

	private static final String SLASH_SIGN = "/";

	@Autowired
	private CatalogService catalogService;

	@Autowired
	private UserService userService;

	private void saveDocument(FileBucket fileBucket, User user) throws IOException {
		final Catalog document = new Catalog();
		final MultipartFile multipartFile = fileBucket.getFile();

		document.setLink(multipartFile.getOriginalFilename());
		document.setComment(fileBucket.getComment());
		document.setContent(multipartFile.getBytes());
		document.setType(multipartFile.getContentType());
		document.setInsertDate(new Date());
		document.setUser(user);

		catalogService.saveDocument(document);
	}

	public void downloadDocument(HttpServletResponse response, Long docId) throws IOException {
		final Catalog document = catalogService.findById(docId);
		response.setContentType(document.getType());
		response.setContentLength(document.getContent().length);
		response.setHeader("Content-Disposition", "attachment; filename=\"" + document.getLink() + "\"");

		FileCopyUtils.copy(document.getContent(), response.getOutputStream());
	}

	public boolean isLoggedInUser(HttpServletRequest context, User user) {
		return user.getUsername() != null && user.getUsername().equals(context.getUserPrincipal().getName());
	}

	public String uploadDocument(FileBucket fileBucket, BindingResult result, ModelMap model, Long userId) throws IOException {

		if (result.hasErrors()) {
			LOGGER.error("validation errors");
			final User user = userService.findById(userId);
			model.addAttribute(UIAttribute.USER, user);
			final List<Catalog> documents = catalogService.findAllByUserId(userId);
			model.addAttribute(UIAttribute.DOCUMENTS, documents);

			return UrlMapping.MANAGE_DOC_DESTINATION_JSP;
		} else {
			LOGGER.info("Fetching file");
			final User user = userService.findById(userId);
			model.addAttribute(UIAttribute.USER, user);
			saveDocument(fileBucket, user);

			return UrlMapping.ADD_DOC_REDIRECT_JSP + SLASH_SIGN + userId;
		}
	}
}
