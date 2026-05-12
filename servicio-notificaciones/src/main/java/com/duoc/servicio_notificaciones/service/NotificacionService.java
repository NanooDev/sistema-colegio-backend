package com.duoc.servicio_notificaciones.service;

import com.duoc.servicio_notificaciones.dto.NotificacionDTO;
import com.duoc.servicio_notificaciones.dto.NotificacionRequest;
import com.duoc.servicio_notificaciones.exception.NotificacionNotFoundException;
import com.duoc.servicio_notificaciones.model.Notificacion;
import com.duoc.servicio_notificaciones.repository.NotificacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificacionService {

    @Autowired
    private NotificacionRepository repository;

    public NotificacionDTO guardar(NotificacionRequest request) {
        Notificacion e = new Notificacion();
        e.setDestinatario(request.getDestinatario());
        e.setAsunto(request.getAsunto());
        e.setMensaje(request.getMensaje());
        e.setFechaEnvio(request.getFechaEnvio());
        return convertirADTO(repository.save(e));
    }

    public List<NotificacionDTO> listar() {
        return repository.findAll().stream().map(this::convertirADTO).collect(Collectors.toList());
    }

    public NotificacionDTO buscarPorId(Long id) {
        Notificacion e = repository.findById(id).orElseThrow(() -> new NotificacionNotFoundException(id));
        return convertirADTO(e);
    }

    public NotificacionDTO actualizar(Long id, NotificacionRequest request) {
        Notificacion ex = repository.findById(id).orElseThrow(() -> new NotificacionNotFoundException(id));
        ex.setDestinatario(request.getDestinatario());
        ex.setAsunto(request.getAsunto());
        ex.setMensaje(request.getMensaje());
        ex.setFechaEnvio(request.getFechaEnvio());
        return convertirADTO(repository.save(ex));
    }

    public void eliminar(Long id) {
        repository.findById(id).orElseThrow(() -> new NotificacionNotFoundException(id));
        repository.deleteById(id);
    }

    private NotificacionDTO convertirADTO(Notificacion e) {
        NotificacionDTO dto = new NotificacionDTO();
        dto.setCategoria("notificacion");
        dto.setId(e.getId());
        dto.setDestinatario(e.getDestinatario());
        dto.setAsunto(e.getAsunto());
        dto.setMensaje(e.getMensaje());
        dto.setFechaEnvio(e.getFechaEnvio());
        return dto;
    }
}
