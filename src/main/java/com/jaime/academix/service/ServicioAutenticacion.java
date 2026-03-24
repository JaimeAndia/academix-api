package com.jaime.academix.service;

import com.jaime.academix.assembler.UsuarioAssembler;
import com.jaime.academix.domain.request.LoginRequest;
import com.jaime.academix.domain.request.RegistroRequest;
import com.jaime.academix.domain.response.RespuestaAuth;
import com.jaime.academix.entity.Carrera;
import com.jaime.academix.entity.Rol;
import com.jaime.academix.entity.Usuario;
import com.jaime.academix.exception.SolicitudInvalidaException;
import com.jaime.academix.repository.CarreraRepository;
import com.jaime.academix.repository.UsuarioRepository;
import com.jaime.academix.security.JwtProveedorToken;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ServicioAutenticacion {

    private final UsuarioRepository usuarioRepository;
    private final CarreraRepository carreraRepository;
    private final PasswordEncoder codificadorContrasena;
    private final AuthenticationManager gestorAutenticacion;
    private final JwtProveedorToken jwtProveedorToken;
    private final UsuarioAssembler usuarioAssembler;

    @Transactional
    public RespuestaAuth registrar(RegistroRequest request) {
        if (usuarioRepository.existsByNombreUsuario(request.getNombreUsuario())) {
            throw new SolicitudInvalidaException("El nombre de usuario ya está en uso");
        }

        if (usuarioRepository.existsByCorreo(request.getCorreo())) {
            throw new SolicitudInvalidaException("El correo electrónico ya está registrado");
        }

        Carrera carrera = null;
        if (request.getCarrera() != null && !request.getCarrera().isBlank()) {
            carrera = carreraRepository.findByNombre(request.getCarrera()).orElse(null);
        }

        Usuario usuario = Usuario.builder()
                .nombreUsuario(request.getNombreUsuario())
                .correo(request.getCorreo())
                .contrasena(codificadorContrasena.encode(request.getContrasena()))
                .nombreCompleto(request.getNombreCompleto())
                .rol(Rol.USER)
                .reputacion(0)
                .nivel(1)
                .apuntesSubidos(0)
                .apuntesDescargados(0)
                .activo(true)
                .carrera(carrera)
                .build();

        usuarioRepository.save(usuario);

        Authentication autenticacion = gestorAutenticacion.authenticate(
                new UsernamePasswordAuthenticationToken(request.getNombreUsuario(), request.getContrasena()));

        String accessToken = jwtProveedorToken.generarAccessToken(autenticacion);
        String refreshToken = jwtProveedorToken.generarRefreshToken(autenticacion);

        return RespuestaAuth.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .usuario(usuarioAssembler.aResponse(usuario))
                .build();
    }

    public RespuestaAuth login(LoginRequest request) {
        Usuario usuario = usuarioRepository.findByCorreo(request.getUsuarioOCorreo())
                .or(() -> usuarioRepository.findByNombreUsuario(request.getUsuarioOCorreo()))
                .orElseThrow(() -> new UsernameNotFoundException(
                        "No se encontró el usuario con: " + request.getUsuarioOCorreo()));

        Authentication autenticacion = gestorAutenticacion.authenticate(
                new UsernamePasswordAuthenticationToken(usuario.getNombreUsuario(), request.getContrasena()));

        String accessToken = jwtProveedorToken.generarAccessToken(autenticacion);
        String refreshToken = jwtProveedorToken.generarRefreshToken(autenticacion);

        return RespuestaAuth.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .usuario(usuarioAssembler.aResponse(usuario))
                .build();
    }

    public RespuestaAuth renovarToken(String refreshToken) {
        if (!jwtProveedorToken.validarToken(refreshToken)) {
            throw new SolicitudInvalidaException("Refresh token inválido o expirado");
        }

        String nombreUsuario = jwtProveedorToken.obtenerNombreUsuario(refreshToken);
        Usuario usuario = usuarioRepository.findByNombreUsuario(nombreUsuario)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "No se encontró el usuario: " + nombreUsuario));

        String nuevoAccessToken = jwtProveedorToken.generarAccessTokenDesdeNombreUsuario(nombreUsuario);

        return RespuestaAuth.builder()
                .accessToken(nuevoAccessToken)
                .refreshToken(refreshToken)
                .usuario(usuarioAssembler.aResponse(usuario))
                .build();
    }
}
