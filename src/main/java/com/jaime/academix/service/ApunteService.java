package com.jaime.academix.service;

import com.jaime.academix.assembler.ApunteAssembler;
import com.jaime.academix.dto.request.ApunteRequest;
import com.jaime.academix.dto.response.ApunteResponse;
import com.jaime.academix.dto.response.MensajeResponse;
import com.jaime.academix.domain.Apunte;
import com.jaime.academix.domain.Rol;
import com.jaime.academix.domain.Tema;
import com.jaime.academix.domain.Usuario;
import com.jaime.academix.exception.UnauthorizedAccessException;
import com.jaime.academix.exception.ResourceNotFoundException;
import com.jaime.academix.repository.ApunteRepository;
import com.jaime.academix.repository.TemaRepository;
import com.jaime.academix.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ApunteService {

    private final ApunteRepository apunteRepository;
    private final TemaRepository temaRepository;
    private final UsuarioRepository usuarioRepository;
    private final ApunteAssembler apunteAssembler;
    private final ReputacionService reputacionService;

    @Value("${app.upload.dir}")
    private String directorioSubida;

    @Transactional
    public ApunteResponse crearApunte(ApunteRequest request, Usuario autor) {
        Tema tema = null;
        if (request.getTemaId() != null) {
            tema = temaRepository.findById(request.getTemaId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "No se encontró el tema con id: " + request.getTemaId()));
        }

        Apunte apunte = Apunte.builder()
                .titulo(request.getTitulo())
                .descripcion(request.getDescripcion())
                .contenido(request.getContenido())
                .autor(autor)
                .tema(tema)
                .esPublico(false)
                .cantidadDescargas(0)
                .promedioValoracion(0.0)
                .build();

        apunteRepository.save(apunte);
        return apunteAssembler.aResponse(apunte);
    }

    public Page<ApunteResponse> listarApuntesPropios(Usuario autor, Pageable pageable) {
        return apunteRepository.findByAutorOrderByFechaCreacionDesc(autor, pageable)
                .map(apunteAssembler::aResponse);
    }

    public Page<ApunteResponse> listarApuntesPublicos(Pageable pageable) {
        return apunteRepository.findByEsPublicoTrueOrderByFechaCreacionDesc(pageable)
                .map(apunteAssembler::aResponse);
    }

    public Page<ApunteResponse> buscarApuntesPublicos(String busqueda, Pageable pageable) {
        return apunteRepository.buscarApuntesPublicos(busqueda, pageable)
                .map(apunteAssembler::aResponse);
    }

    public ApunteResponse obtenerApunte(Long id, Usuario usuarioActual) {
        Apunte apunte = buscarApuntePorId(id);

        if (!apunte.getEsPublico() && (usuarioActual == null ||
                !apunte.getAutor().getId().equals(usuarioActual.getId()))) {
            throw new UnauthorizedAccessException("No tienes permiso para ver este apunte");
        }

        return apunteAssembler.aResponse(apunte);
    }

    @Transactional
    public ApunteResponse editarApunte(Long id, ApunteRequest request, Usuario autor) {
        Apunte apunte = buscarApuntePorId(id);
        verificarAutoria(apunte, autor);

        if (request.getTitulo() != null) {
            apunte.setTitulo(request.getTitulo());
        }
        if (request.getDescripcion() != null) {
            apunte.setDescripcion(request.getDescripcion());
        }
        if (request.getContenido() != null) {
            apunte.setContenido(request.getContenido());
        }
        if (request.getTemaId() != null) {
            Tema tema = temaRepository.findById(request.getTemaId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "No se encontró el tema con id: " + request.getTemaId()));
            apunte.setTema(tema);
        }

        apunteRepository.save(apunte);
        return apunteAssembler.aResponse(apunte);
    }

    @Transactional
    public MensajeResponse eliminarApunte(Long id, Usuario usuario) {
        Apunte apunte = buscarApuntePorId(id);

        boolean esAutor = apunte.getAutor().getId().equals(usuario.getId());
        boolean esAdmin = usuario.getRol() == Rol.ADMIN;

        if (!esAutor && !esAdmin) {
            throw new UnauthorizedAccessException("No tienes permiso para eliminar este apunte");
        }

        apunteRepository.delete(apunte);
        return new MensajeResponse("Apunte eliminado correctamente");
    }

    @Transactional
    public ApunteResponse publicarApunte(Long id, Usuario autor) {
        Apunte apunte = buscarApuntePorId(id);
        verificarAutoria(apunte, autor);

        if (!apunte.getEsPublico()) {
            apunte.setEsPublico(true);
            autor.setApuntesSubidos(autor.getApuntesSubidos() + 1);
            reputacionService.sumarReputacion(autor, 10);
            apunteRepository.save(apunte);
        }

        return apunteAssembler.aResponse(apunte);
    }

    @Transactional
    public ApunteResponse descargarApunte(Long id, Usuario usuario) {
        Apunte apunte = buscarApuntePorId(id);

        apunte.setCantidadDescargas(apunte.getCantidadDescargas() + 1);
        usuario.setApuntesDescargados(usuario.getApuntesDescargados() + 1);
        usuarioRepository.save(usuario);

        // Bonus por popularidad: +5 cada 10 descargas
        if (apunte.getCantidadDescargas() % 10 == 0) {
            reputacionService.sumarReputacion(apunte.getAutor(), 5);
        }

        apunteRepository.save(apunte);
        return apunteAssembler.aResponse(apunte);
    }

    @Transactional
    public ApunteResponse subirArchivo(Long id, MultipartFile archivo, Usuario autor) throws IOException {
        Apunte apunte = buscarApuntePorId(id);
        verificarAutoria(apunte, autor);

        Path dirSubida = Paths.get(directorioSubida);
        if (!Files.exists(dirSubida)) {
            Files.createDirectories(dirSubida);
        }

        String nombreArchivo = UUID.randomUUID() + "_" + archivo.getOriginalFilename();
        Path rutaArchivo = dirSubida.resolve(nombreArchivo);
        Files.copy(archivo.getInputStream(), rutaArchivo, StandardCopyOption.REPLACE_EXISTING);

        apunte.setArchivoUrl("/uploads/" + nombreArchivo);
        apunteRepository.save(apunte);

        return apunteAssembler.aResponse(apunte);
    }

    private Apunte buscarApuntePorId(Long id) {
        return apunteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el apunte con id: " + id));
    }

    private void verificarAutoria(Apunte apunte, Usuario autor) {
        if (!apunte.getAutor().getId().equals(autor.getId())) {
            throw new UnauthorizedAccessException("No tienes permiso para modificar este apunte");
        }
    }
}
