package com.example.gateway.security;

import java.nio.charset.Charset;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import com.example.gateway.enums.WebFulxGlobaErr;
import com.example.gateway.utils.WebFluxResponseUtils;

import lombok.SneakyThrows;
import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
public class ResourceServerAutoConfiguration {

    public static final String[] IGNORE_URLS = {
            "/public/**",
            "api/auth-service/oauth2/**",
    };

    /**
     * webflux下跨域过滤器配置
     *
     * @return
     */
    @Bean
    public CorsWebFilter corsWebFilter() {
        //cors配置
        final CorsConfiguration config = new CorsConfiguration();
        //允许客户端携带凭证
        config.setAllowCredentials(true);
        //允许所有源发起跨域请求
        config.addAllowedOriginPattern("*");
        //允许接受所有请求头
        config.addAllowedHeader("*");
        // 预检请求的缓存时间（秒），在此时间内重复的跨域请求不会再触发预检
        config.setMaxAge(18000L);
        config.addAllowedMethod(HttpMethod.OPTIONS.name());
        config.addAllowedMethod(HttpMethod.HEAD.name());
        config.addAllowedMethod(HttpMethod.GET.name());
        config.addAllowedMethod(HttpMethod.POST.name());
        config.addAllowedMethod(HttpMethod.PUT.name());
        config.addAllowedMethod(HttpMethod.PATCH.name());
        config.addAllowedMethod(HttpMethod.DELETE.name());
        //跨域配置的源类，可以通过 URL 匹配配置不同路径的 CORS 规则
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        //所有路径都遵从次规则
        source.registerCorsConfiguration("/**", config);
        return new CorsWebFilter(source);
    }

    /**
     * 配置认证相关的过滤器链
     */
    @Bean
    public SecurityWebFilterChain defaultSecurityFilterChain(ServerHttpSecurity http) {
        return http
                //禁用CSRF（跨站请求伪造）
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                //请求访问控制
                .authorizeExchange(authorize -> authorize
                        //忽略路径
                        .pathMatchers(IGNORE_URLS)
                        //其他所有请求使用自定义认证管理器进行认证
                        .permitAll()
                        .anyExchange()
                        .access(resourceServerManager())
                )
                .exceptionHandling(exception -> exception
                        //权限不足
                        .accessDeniedHandler(((exchange, denied) -> WebFluxResponseUtils.writeWithThrowable(WebFulxGlobaErr.x403, exchange, denied)))
                        //未认证
                        .authenticationEntryPoint(((exchange, ex) -> WebFluxResponseUtils.writeWithThrowable(WebFulxGlobaErr.x401, exchange, ex)))
                )
                //设置当前应用（网关为oauth2资源服务器） 主要进行认证
                .oauth2ResourceServer((resourceServer) -> resourceServer
                        //使用jwt模式
                        .jwt(jwt -> jwt
                                //转换器
                                .jwtAuthenticationConverter(grantedAuthoritiesExtractor())
                                //解码器
                                .jwtDecoder(jwtDecoder())
                                //自定义认证
                                .authenticationManager(oAuthJwtReactiveAuthenticationManager())
                        )
                )
                //添加自定义cors过滤器 并指定过滤器顺序，cors优先执行
                .addFilterAt(corsWebFilter(), SecurityWebFiltersOrder.CORS)
                .build();
    }

    @Bean
    @SneakyThrows
    public ResourceServerManager resourceServerManager() {
        return new ResourceServerManager();
    }

    /**
     * oauth模式下 jwt认证管理器
     */
    @Bean
    public OAuthJwtReactiveAuthenticationManager oAuthJwtReactiveAuthenticationManager() {
        return new OAuthJwtReactiveAuthenticationManager();
    }

    /**
     * 自定义jwt解析器，设置解析出来的权限信息的前缀与在jwt中的key
     */
    public Converter<Jwt, Mono<AbstractAuthenticationToken>> grantedAuthoritiesExtractor() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        // 设置解析权限信息的前缀，设置为空是去掉前缀
        grantedAuthoritiesConverter.setAuthorityPrefix("");
        // 设置权限信息在jwt claims中的key
        grantedAuthoritiesConverter.setAuthoritiesClaimName("authorities");
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return new ReactiveJwtAuthenticationConverterAdapter(jwtAuthenticationConverter);
    }

    public NimbusReactiveJwtDecoder jwtDecoder() {
        try {
            RSAPublicKey publicKey = (RSAPublicKey) loadPublicKey();

            return NimbusReactiveJwtDecoder.withPublicKey(publicKey).build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static PublicKey loadPublicKey() throws Exception {
        Resource pubkeyResource = new ClassPathResource("public.key");
        String publicKeyContent = pubkeyResource.getContentAsString(Charset.defaultCharset());

        publicKeyContent = publicKeyContent.replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s+", "");

        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyContent));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpec);
    }

}
