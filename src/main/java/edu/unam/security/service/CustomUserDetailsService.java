package edu.unam.security.service;

import edu.unam.auth.model.entity.Usuario;
import edu.unam.auth.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService
        implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final MessageSource messageSource;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                getMessage("auth.invalid.credentials")
                        ));

        String rol = usuario.getPerfil()
                .getNombre()
                .toUpperCase();

        return User.builder()
                .username(usuario.getEmail())
                .password(usuario.getPasswordHash())
                .roles(rol)
                .disabled(!usuario.getActivo())
                .build();
    }

    private String getMessage(String key) {
        return messageSource.getMessage(
                key,
                null,
                key,
                LocaleContextHolder.getLocale()
        );
    }
}