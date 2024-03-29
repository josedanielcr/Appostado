package cr.ac.cenfotec.appostado.web.rest;

import cr.ac.cenfotec.appostado.domain.CuentaUsuario;
import cr.ac.cenfotec.appostado.domain.Notificacion;
import cr.ac.cenfotec.appostado.domain.User;
import cr.ac.cenfotec.appostado.domain.Usuario;
import cr.ac.cenfotec.appostado.repository.CuentaUsuarioRepository;
import cr.ac.cenfotec.appostado.repository.UserRepository;
import cr.ac.cenfotec.appostado.repository.UsuarioRepository;
import cr.ac.cenfotec.appostado.security.SecurityUtils;
import cr.ac.cenfotec.appostado.service.*;
import cr.ac.cenfotec.appostado.service.dto.AdminUserDTO;
import cr.ac.cenfotec.appostado.service.dto.PasswordChangeDTO;
import cr.ac.cenfotec.appostado.web.rest.errors.*;
import cr.ac.cenfotec.appostado.web.rest.errors.EmailAlreadyUsedException;
import cr.ac.cenfotec.appostado.web.rest.errors.InvalidPasswordException;
import cr.ac.cenfotec.appostado.web.rest.vm.*;
import java.io.IOException;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing the current user's account.
 */
@RestController
@RequestMapping("/api")
public class AccountResource {

    private static class AccountResourceException extends RuntimeException {

        private AccountResourceException(String message) {
            super(message);
        }
    }

    private final Logger log = LoggerFactory.getLogger(AccountResource.class);

    private final UserRepository userRepository;

    private final UsuarioRepository usuarioRepository;

    private final UserService userService;

    private final UsuarioService usuarioService;

    private final MailService mailService;

    private final CuentaUsuarioService cuentaUsuarioService;

    private final TwilioMailService twilioMailService;

    private final CuentaUsuarioRepository cuentaUsuarioRepository;

    public AccountResource(
        UserRepository userRepository,
        UsuarioRepository usuarioRepository,
        UserService userService,
        MailService mailService,
        UsuarioService usuarioService,
        CuentaUsuarioService cuentaUsuarioService,
        TwilioMailService twilioMailService,
        CuentaUsuarioRepository cuentaUsuarioRepository
    ) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.mailService = mailService;
        this.usuarioRepository = usuarioRepository;
        this.usuarioService = usuarioService;
        this.cuentaUsuarioService = cuentaUsuarioService;
        this.twilioMailService = twilioMailService;
        this.cuentaUsuarioRepository = cuentaUsuarioRepository;
    }

    /**
     * {@code POST  /register} : register the user.
     *
     * @param managedUserVM the managed user View Model.
     * @throws InvalidPasswordException {@code 400 (Bad Request)} if the password is incorrect.
     * @throws EmailAlreadyUsedException {@code 400 (Bad Request)} if the email is already used.
     * @throws LoginAlreadyUsedException {@code 400 (Bad Request)} if the login is already used.
     */
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerAccount(@Valid @RequestBody ManagedUserVM managedUserVM) throws IOException {
        if (isPasswordLengthInvalid(managedUserVM.getPassword())) {
            throw new InvalidPasswordException();
        }
        User user = userService.registerUser(
            managedUserVM,
            managedUserVM.getPassword(),
            managedUserVM.getFechaNacimiento(),
            managedUserVM.getPais()
        );
        twilioMailService.sendActivationMail(
            user.getEmail(),
            user.getLogin(),
            managedUserVM.getActivationURL() + "?key=" + user.getActivationKey()
        );
    }

    /**
     * {@code GET  /activate} : activate the registered user.
     *
     * @param key the activation key.
     * @throws RuntimeException {@code 500 (Internal Server Error)} if the user couldn't be activated.
     */
    @GetMapping("/activate")
    public void activateAccount(@RequestParam(value = "key") String key) {
        Optional<User> user = userService.activateRegistration(key);
        if (!user.isPresent()) {
            throw new AccountResourceException("No user was found for this activation key");
        }
        Optional<Usuario> usuario = usuarioRepository.findById(user.get().getId());

        //Create monetary account for each activated user
        if (user.isPresent()) {
            cuentaUsuarioService.createCuenta(usuario.get());
        }
    }

    /**
     * {@code GET  /authenticate} : check if the user is authenticated, and return its login.
     *
     * @param request the HTTP request.
     * @return the login if the user is authenticated.
     */
    @GetMapping("/authenticate")
    public String isAuthenticated(HttpServletRequest request) {
        log.debug("REST request to check if the current user is authenticated");
        return request.getRemoteUser();
    }

    /**
     * {@code GET  /account} : get the current user.
     *
     * @return the current user.
     * @throws RuntimeException {@code 500 (Internal Server Error)} if the user couldn't be returned.
     */
    @GetMapping("/account")
    public AdminUserDTO getAccount() {
        return userService
            .getUserWithAuthorities()
            .map(AdminUserDTO::new)
            .orElseThrow(() -> new AccountResourceException("User could not be found"));
    }

    /**
     * {@code POST  /account} : update the current user information.
     *
     * @param userDTO the current user information.
     * @throws EmailAlreadyUsedException {@code 400 (Bad Request)} if the email is already used.
     * @throws RuntimeException {@code 500 (Internal Server Error)} if the user login wasn't found.
     */
    @PostMapping("/account")
    public void saveAccount(@Valid @RequestBody AdminUserDTO userDTO) {
        String userLogin = SecurityUtils
            .getCurrentUserLogin()
            .orElseThrow(() -> new AccountResourceException("Current user login not found"));
        Optional<User> existingUser = userRepository.findOneByEmailIgnoreCase(userDTO.getEmail());
        if (existingUser.isPresent() && (!existingUser.get().getLogin().equalsIgnoreCase(userLogin))) {
            throw new EmailAlreadyUsedException();
        }
        Optional<User> user = userRepository.findOneByLogin(userLogin);
        if (!user.isPresent()) {
            throw new AccountResourceException("User could not be found");
        }
        userService.updateUser(
            userDTO.getFirstName(),
            userDTO.getLastName(),
            userDTO.getEmail(),
            userDTO.getLangKey(),
            userDTO.getImageUrl()
        );
    }

    /**
     * {@code POST  /account/change-password} : changes the current user's password.
     *
     * @param passwordChangeDto current and new password.
     * @throws InvalidPasswordException {@code 400 (Bad Request)} if the new password is incorrect.
     */
    @PostMapping(path = "/account/change-password")
    public void changePassword(@RequestBody PasswordChangeDTO passwordChangeDto) {
        if (isPasswordLengthInvalid(passwordChangeDto.getNewPassword())) {
            throw new InvalidPasswordException();
        }
        userService.changePassword(passwordChangeDto.getCurrentPassword(), passwordChangeDto.getNewPassword());
    }

    /**
     *
     * @param resetPasswordVM
     * @throws IOException
     */
    @PostMapping(path = "/account/reset-password/init")
    public void requestPasswordReset(@RequestBody ResetPasswordVM resetPasswordVM) throws IOException {
        Optional<User> user = userService.requestPasswordReset(resetPasswordVM.getEmail());
        if (user.isPresent()) {
            String url = resetPasswordVM.getResetURL() + "?key=" + user.get().getResetKey();
            twilioMailService.sendResetPasswordMail(user.get().getEmail(), user.get().getLogin(), url);
        } else {
            // Pretend the request has been successful to prevent checking which emails really exist
            // but log that an invalid attempt has been made
            log.warn("Password reset requested for non existing mail");
        }
    }

    /**
     * {@code POST   /account/reset-password/finish} : Finish to reset the password of the user.
     *
     * @param keyAndPassword the generated key and the new password.
     * @throws InvalidPasswordException {@code 400 (Bad Request)} if the password is incorrect.
     * @throws RuntimeException {@code 500 (Internal Server Error)} if the password could not be reset.
     */
    @PostMapping(path = "/account/reset-password/finish")
    public void finishPasswordReset(@RequestBody KeyAndPasswordVM keyAndPassword) {
        if (isPasswordLengthInvalid(keyAndPassword.getNewPassword())) {
            throw new InvalidPasswordException();
        }
        Optional<User> user = userService.completePasswordReset(keyAndPassword.getNewPassword(), keyAndPassword.getKey());

        if (!user.isPresent()) {
            throw new AccountResourceException("No user was found for this reset key");
        }
    }

    private static boolean isPasswordLengthInvalid(String password) {
        return (
            StringUtils.isEmpty(password) ||
            password.length() < ManagedUserVM.PASSWORD_MIN_LENGTH ||
            password.length() > ManagedUserVM.PASSWORD_MAX_LENGTH
        );
    }

    @GetMapping("/account/logged")
    public AmigoDetailsVM getLoggedUserInfo() {
        log.debug("REST request info from logged in User");
        AmigoDetailsVM logged = new AmigoDetailsVM();
        SecurityUtils
            .getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .ifPresent(user -> {
                Usuario usuario = usuarioRepository.findById(user.getId()).get();
                CuentaUsuario cuentaUsuario = cuentaUsuarioRepository.findByUsuarioId(usuario.getId()).get();
                logged.setLogin(user.getLogin());
                logged.setAvatar(user.getImageUrl());
                logged.setCanjes(cuentaUsuario.getNumCanjes());
                logged.setTotales(cuentaUsuario.getApuestasTotales());
                logged.setGanados(cuentaUsuario.getApuestasGanadas());
                logged.setPerfil(usuario.getNombrePerfil());
                logged.setCountry(usuario.getPais());
                logged.setBalance(cuentaUsuario.getBalance());
            });

        log.debug("Sending logged-in User information: {}", logged);

        return logged;
    }
}
