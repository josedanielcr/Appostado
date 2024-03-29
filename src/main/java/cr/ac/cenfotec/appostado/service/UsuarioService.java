package cr.ac.cenfotec.appostado.service;

import cr.ac.cenfotec.appostado.domain.CuentaUsuario;
import cr.ac.cenfotec.appostado.domain.Mision;
import cr.ac.cenfotec.appostado.domain.MisionUsuario;
import cr.ac.cenfotec.appostado.domain.Usuario;
import cr.ac.cenfotec.appostado.repository.MisionRepository;
import cr.ac.cenfotec.appostado.repository.MisionUsuarioRepository;
import cr.ac.cenfotec.appostado.repository.UsuarioRepository;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class for managing user's extra attributes.
 */
@Service
@Transactional
public class UsuarioService {

    private final Logger log = LoggerFactory.getLogger(UsuarioService.class);

    private final UsuarioRepository usuarioRepository;

    private final MisionRepository misionRepository;

    private final MisionUsuarioRepository misionUsuarioRepository;

    public UsuarioService(
        UsuarioRepository usuarioRepository,
        MisionRepository misionRepository,
        MisionUsuarioRepository misionUsuarioRepository
    ) {
        this.usuarioRepository = usuarioRepository;
        this.misionRepository = misionRepository;
        this.misionUsuarioRepository = misionUsuarioRepository;
    }

    /**
     *
     * @param usuario Additional attributes of user to register
     * @return The registered User+extra attributes
     */
    public Usuario registerUsuario(Usuario usuario) {
        usuarioRepository.save(usuario);
        log.debug("Created Information for Usuario: {}", usuario);
        List<Mision> misionesExistentes = this.misionRepository.findAll();

        for (int i = 0; i < misionesExistentes.size(); i++) {
            MisionUsuario asig = new MisionUsuario();
            asig.setUsuario(usuario);
            asig.setMision(misionesExistentes.get(i));
            asig.setCompletado(false);
            this.misionUsuarioRepository.save(asig);
        }
        return usuario;
    }

    /**
     *
     * @param usuario Usuario to modify
     * @param cuenta Account to add to Usuario
     */
    public void updateUsuario(Usuario usuario, CuentaUsuario cuenta) {
        usuarioRepository
            .findById(usuario.getId())
            .ifPresent(user -> {
                usuario.setPais(usuario.getPais());
                usuario.setFechaNacimiento(usuario.getFechaNacimiento());
                if (usuario.getNombrePerfil() != null) {
                    usuario.setNombrePerfil(usuario.getNombrePerfil());
                }

                log.debug("Changed Information for Usuario: {}", usuario);
            });
    }
}
