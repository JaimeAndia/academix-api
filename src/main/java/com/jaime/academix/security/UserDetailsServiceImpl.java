package com.jaime.academix.security;

import com.jaime.academix.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String nombreUsuarioOCorreo) throws UsernameNotFoundException {
        return usuarioRepository.findByCorreo(nombreUsuarioOCorreo)
                .or(() -> usuarioRepository.findByNombreUsuario(nombreUsuarioOCorreo))
                .orElseThrow(() -> new UsernameNotFoundException(
                        "No se encontró el usuario con: " + nombreUsuarioOCorreo));
    }
}
