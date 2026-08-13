package dev.epse.app.config.security

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
class CorsConfig(
    private val corsProperties: CorsProperties
) : WebMvcConfigurer {
    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .allowedOrigins(*corsProperties.allowedOrigins.toTypedArray())
            .allowedMethods(*corsProperties.allowedMethods.toTypedArray())
            .allowedHeaders(*corsProperties.allowedHeaders.toTypedArray())
            .allowCredentials(corsProperties.allowCredentials)
    }

    @Bean
    fun openApi(): OpenAPI {
        return OpenAPI()
            .info(Info().title("API Documentation").version("1.0"))
    }
}