package floripa.autenticacao.backend.config;

import com.google.common.collect.Lists;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.*;

/**
 * 
 * @author brunno
 *
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig extends WebMvcConfigurationSupport {
	
	@Bean
    public Docket api() { 
        return new Docket(DocumentationType.SWAGGER_2)
				.securitySchemes(Arrays.asList(apiKey()))
				//.globalOperationParameters(Lis)

				.select()
				.apis(RequestHandlerSelectors.basePackage( "floripa.autenticacao.backend.rest" ))
				.paths(PathSelectors.any())
				.build()
				.produces(getAllProduceContentTypes())
				.consumes(new HashSet<>(
						Arrays.asList(
								"application/xml",
								"application/json")))
				.apiInfo(createApiInfo())
				.securityContexts(Lists.newArrayList(securityContext()));
    }

	private Set<String> getAllConsumeContentTypes() {
		Set<String> consumes = new HashSet<>();
		consumes.add("application/json");
		return consumes;
	}

	private Set<String> getAllProduceContentTypes() {
		Set<String> produces = new HashSet<>();
		produces.add("application/json");
		return produces;
	}
	
	private ApiInfo createApiInfo() {
		return new ApiInfoBuilder()
				.title("Autenticação de usuários via mongodb")
				.version("1.0.0").build();
	}


	private SecurityContext securityContext() {
		return SecurityContext.builder()
				.securityReferences(defaultAuth())
				.forPaths(PathSelectors.any())
				.build();
	}

	List<SecurityReference> defaultAuth() {
		AuthorizationScope authorizationScope
				= new AuthorizationScope("global", "accessEverything");
		AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
		authorizationScopes[0] = authorizationScope;
		return Collections.singletonList(new SecurityReference("Bearer", authorizationScopes));
	}

	private ApiKey apiKey() {
		return new ApiKey("Bearer", "Authorization", "header");
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
	    registry.addInterceptor(new LocaleChangeInterceptor());
	}
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
	    registry.addResourceHandler("swagger-ui.html")
	      .addResourceLocations("classpath:/META-INF/resources/");

	    registry.addResourceHandler("/webjars/**")
	      .addResourceLocations("classpath:/META-INF/resources/webjars/");
	}

}