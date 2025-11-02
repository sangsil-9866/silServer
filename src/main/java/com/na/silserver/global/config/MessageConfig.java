
package com.na.silserver.global.config;

import com.na.silserver.global.util.UtilCommon;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Configuration
public class MessageConfig implements WebMvcConfigurer {

    @Bean
    @Primary  // 기본 LocaleResolver로 사용
    public LocaleResolver customLocaleResolver() {
        CustomLocaleResolver customLocaleResolver = new CustomLocaleResolver();
        customLocaleResolver.setDefaultLocale(Locale.KOREA);
        return customLocaleResolver;
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
        lci.setParamName("lang");
        return lci;
    }

    @Bean
    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasenames("messages/messages", "messages/validations", "org.hibernate.validator.ValidationMessages");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setCacheSeconds(60);
        messageSource.setUseCodeAsDefaultMessage(true);
        return messageSource;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }

    public static class CustomLocaleResolver extends AcceptHeaderLocaleResolver {
        private final String[] languageCodes = {"ko", "en"};
        private List<Locale> locales = Arrays.asList(
                Locale.forLanguageTag("ko"),
                Locale.forLanguageTag("en")
        );

        @NotNull
        @Override
        public Locale resolveLocale(HttpServletRequest request) {
            String acceptLanguage = request.getHeader(HttpHeaders.ACCEPT_LANGUAGE);

            if (UtilCommon.isEmpty(acceptLanguage)) {
                return getDefaultLocale() != null ? getDefaultLocale() : Locale.KOREA;
            }

            try {
                List<Locale.LanguageRange> languageRanges = Locale.LanguageRange.parse(acceptLanguage);

                // 지원하는 언어 목록 재구성
                List<Locale> supportedLocales = new ArrayList<>();
                for (String code : languageCodes) {
                    supportedLocales.add(Locale.forLanguageTag(code));
                }

                Locale resolvedLocale = Locale.lookup(languageRanges, supportedLocales);
                return resolvedLocale != null ? resolvedLocale : getDefaultLocale();

            } catch (Exception e) {
                return getDefaultLocale() != null ? getDefaultLocale() : Locale.KOREA;
            }
        }
    }
}