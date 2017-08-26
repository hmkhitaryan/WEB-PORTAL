package com.egs.account.mapping;

/**
 * Created by User on 12.03.2017.
 */
public class UrlMapping {

	public static final String ROOT = "/";

	public final static String LOGIN = "/login";

	public final static String LOGOUT = "/logout";

	public final static String WELCOME = "/welcome";

	public final static String REGISTRATION = "/registration";

	public final static String TO_MAP = "/toMap";

	public final static String EDIT_USER = "/edit/user";

	public static final String DELETE_USER = "/delete/user";

	public static final String ADD_DOCUMENT = "/add/document";

	public static final String DOWNLOAD_DOCUMENT = "/download/document";

	public static final String DELETE_DOCUMENT = "/delete/document";

	public static final String ENGLISH_LANG = "?language=en";

	public static final String FRENCH_LANG = "?language=fr";

	//jsp views
	public static final String LOGIN_DESTINATION_JSP = "login";

	public static final String REGISTRATION_DESTINATION_JSP = "registration";

	public static final String WELCOME_REDIRECT_JSP = "redirect:/welcome";

	public static final String EDIT_USER_REDIRECT_JSP = "redirect:/edit/user";

	public static final String ADD_DOC_REDIRECT_JSP = "redirect:/add/document";

	public static final String WELCOME_DESTINATION_JSP = "welcomeUser";

	public static final String EDIT_USER_DESTINATION_JSP = "editUser";

	public static final String MANAGE_DOC_DESTINATION_JSP = "manageDocuments";

	public static final String REGISTRATION_SUCCESS_DESTINATION_JSP = "registrationSuccess";

	public static final String DELETE_SUCCESS_DESTINATION_JSP = "deleteSuccess";

}
