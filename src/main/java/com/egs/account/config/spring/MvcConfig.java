package com.egs.account.config.spring;

import com.egs.account.utils.domainUtils.DomainUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.AbstractLocaleContextResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.Locale;

@EnableWebMvc
@Configuration
@ComponentScan(basePackages = {"com.egs.account.*"})
public class MvcConfig extends WebMvcConfigurerAdapter {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
	}

	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}

	@Bean
	public InternalResourceViewResolver jspViewResolver() {
		final InternalResourceViewResolver bean = new InternalResourceViewResolver();
		bean.setPrefix("/WEB-INF/views/");
		bean.setSuffix(".jsp");

		return bean;
	}

	@Bean(name = "multipartResolver")
	public CommonsMultipartResolver getMultipartResolver() {
		final CommonsMultipartResolver resolver = new CommonsMultipartResolver();
//		resolver.setMaxUploadSize(10240000);

		return resolver;
	}

	@Bean(name = "messageSource")
	public MessageSource getMessageSource() {
		final ReloadableResourceBundleMessageSource resource = new ReloadableResourceBundleMessageSource();
		resource.setBasenames("classpath:validation", "classpath:messages");
		resource.setDefaultEncoding("UTF-8");

		return resource;
	}

	@Bean(name = "localeResolver")
	public AbstractLocaleContextResolver getLocaleResolver() {
		final AbstractLocaleContextResolver resolver = new SessionLocaleResolver();
		resolver.setDefaultLocale(Locale.ENGLISH);

		return resolver;
	}

	@Bean(name = "domainUtils")
	public DomainUtils getDomainUtils() {
		return new DomainUtils();
	}

	@Bean
	public LocaleChangeInterceptor localeInterceptor() {
		final LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
		interceptor.setParamName("language");

		return interceptor;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(localeInterceptor());
	}

	@Bean(name = "mailSender")
	public JavaMailSender mailSender() {
		return new JavaMailSenderImpl();
	}
}
