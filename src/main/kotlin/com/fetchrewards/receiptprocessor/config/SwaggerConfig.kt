package com.fetchrewards.receiptprocessor.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import io.swagger.v3.oas.models.servers.Server
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {
    @Bean
    fun myOpenAPI(): OpenAPI {
        val devServer = Server()
        devServer.url = "http://localhost:8080"
        devServer.description = "Server URL in Local environment"
        val mitLicense = License().name("MIT License").url("https://choosealicense.com/licenses/mit/")
        val info = Info()
            .title("Receipt Processor API")
            .version("1.0")
            .description("This API exposes endpoints to process receipts.")
            .license(mitLicense)
        return OpenAPI().info(info).servers(listOf(devServer))
    }
}