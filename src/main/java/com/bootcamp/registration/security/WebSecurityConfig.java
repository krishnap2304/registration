package com.bootcamp.registration.security;

import com.bootcamp.registration.model.Role;
import com.bootcamp.registration.model.RoleEnum;
import com.bootcamp.registration.model.User;
import com.bootcamp.registration.repository.RoleRepository;
import com.bootcamp.registration.repository.UserRepository;
import com.bootcamp.registration.service.SendEmailService;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.Properties;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    UserDetailsServiceImpl userDetailsServiceImpl;

    @Autowired
    private AuthEntryPointJwt unauthorizationHandler;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter(){
        return new AuthTokenFilter();
    }


    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder)throws Exception{
        authenticationManagerBuilder.userDetailsService(userDetailsServiceImpl).passwordEncoder(getPasswordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception{
        return super.authenticationManagerBean();
    }

    /**
     * For the Password Encoding
     * @return
     */
    @Bean
    public BCryptPasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SendEmailService createSendEmailService(){
        return new SendEmailService();
    }

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);

        mailSender.setUsername("my.gmail@gmail.com");
        mailSender.setPassword("password");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }
    /**
     * For the generation of swagger documentation
     * @return
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Registration and Login Application API")
                        .version("1.0")
                        .description("Registration and Login Service Description")
                        .termsOfService("http://swagger.io/terms/")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")));
    }

    @Override
    public void configure(HttpSecurity http)throws Exception{
        System.out.println("security starts*******");
        http.cors().and().csrf().disable()
                .exceptionHandling().authenticationEntryPoint(unauthorizationHandler).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().authorizeRequests()
                .antMatchers("/api/auth/**").permitAll()
                .antMatchers("/**").permitAll()
                .anyRequest().authenticated();
    }

    @Bean
    public RoleRepository getRoleRepostiry(){
        return new RoleRepository() {
            @Override
            public Optional<Role> findByName(RoleEnum name) {
                return Optional.empty();
            }

            @Override
            public Boolean existsByName(RoleEnum name) {
                return null;
            }

            @Override
            public void deleteAllById(Iterable<? extends String> iterable) {

            }

            @Override
            public <S extends Role> List<S> saveAll(Iterable<S> iterable) {
                return null;
            }

            @Override
            public List<Role> findAll() {
                return null;
            }

            @Override
            public List<Role> findAll(Sort sort) {
                return null;
            }

            @Override
            public <S extends Role> S insert(S s) {
                return null;
            }

            @Override
            public <S extends Role> List<S> insert(Iterable<S> iterable) {
                return null;
            }

            @Override
            public <S extends Role> List<S> findAll(Example<S> example) {
                return null;
            }

            @Override
            public <S extends Role> List<S> findAll(Example<S> example, Sort sort) {
                return null;
            }

            @Override
            public Page<Role> findAll(Pageable pageable) {
                return null;
            }

            @Override
            public <S extends Role> S save(S s) {
                return null;
            }

            @Override
            public Optional<Role> findById(String s) {
                return Optional.empty();
            }

            @Override
            public boolean existsById(String s) {
                return false;
            }

            @Override
            public Iterable<Role> findAllById(Iterable<String> iterable) {
                return null;
            }

            @Override
            public long count() {
                return 0;
            }

            @Override
            public void deleteById(String s) {

            }

            @Override
            public void delete(Role role) {

            }

            @Override
            public void deleteAll(Iterable<? extends Role> iterable) {

            }

            @Override
            public void deleteAll() {

            }

            @Override
            public <S extends Role> Optional<S> findOne(Example<S> example) {
                return Optional.empty();
            }

            @Override
            public <S extends Role> Page<S> findAll(Example<S> example, Pageable pageable) {
                return null;
            }

            @Override
            public <S extends Role> long count(Example<S> example) {
                return 0;
            }

            @Override
            public <S extends Role> boolean exists(Example<S> example) {
                return false;
            }
        };
    }
    @Bean
    public UserRepository getUserRepository(){
        return new UserRepository() {
            @Override
            public Optional<User> findByUsername(String username) {
                return Optional.empty();
            }

            @Override
            public Boolean existsByUsername(String username) {
                return null;
            }

            @Override
            public Boolean existsByEmail(String email) {
                return null;
            }

            @Override
            public Optional<User> findByEmail(String email) {
                return Optional.empty();
            }

            @Override
            public void deleteAllById(Iterable<? extends String> iterable) {

            }

            @Override
            public <S extends User> List<S> saveAll(Iterable<S> iterable) {
                return null;
            }

            @Override
            public List<User> findAll() {
                return null;
            }

            @Override
            public List<User> findAll(Sort sort) {
                return null;
            }

            @Override
            public <S extends User> S insert(S s) {
                return null;
            }

            @Override
            public <S extends User> List<S> insert(Iterable<S> iterable) {
                return null;
            }

            @Override
            public <S extends User> List<S> findAll(Example<S> example) {
                return null;
            }

            @Override
            public <S extends User> List<S> findAll(Example<S> example, Sort sort) {
                return null;
            }

            @Override
            public Page<User> findAll(Pageable pageable) {
                return null;
            }

            @Override
            public <S extends User> S save(S s) {
                return null;
            }

            @Override
            public Optional<User> findById(String s) {
                return Optional.empty();
            }

            @Override
            public boolean existsById(String s) {
                return false;
            }

            @Override
            public Iterable<User> findAllById(Iterable<String> iterable) {
                return null;
            }

            @Override
            public long count() {
                return 0;
            }

            @Override
            public void deleteById(String s) {

            }

            @Override
            public void delete(User user) {

            }

            @Override
            public void deleteAll(Iterable<? extends User> iterable) {

            }

            @Override
            public void deleteAll() {

            }

            @Override
            public <S extends User> Optional<S> findOne(Example<S> example) {
                return Optional.empty();
            }

            @Override
            public <S extends User> Page<S> findAll(Example<S> example, Pageable pageable) {
                return null;
            }

            @Override
            public <S extends User> long count(Example<S> example) {
                return 0;
            }

            @Override
            public <S extends User> boolean exists(Example<S> example) {
                return false;
            }
        };
    }


}
