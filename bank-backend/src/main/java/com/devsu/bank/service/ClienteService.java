package com.devsu.bank.service;

import com.devsu.bank.dto.ClienteCreateRequest;
import com.devsu.bank.dto.ClienteResponse;
import com.devsu.bank.dto.ClienteUpdateRequest;
import com.devsu.bank.entity.Cliente;
import com.devsu.bank.exception.BusinessException;
import com.devsu.bank.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;

    @Transactional
    public ClienteResponse crear(ClienteCreateRequest req) {

        if (clienteRepository.existsByIdentificacion(req.identificacion())) {
            throw new BusinessException("Identificación ya existe");
        }
        if (clienteRepository.existsByClientId(req.clientKey())) {
            throw new BusinessException("ClientKey ya existe");
        }

        Cliente cliente = new Cliente();

        // Persona (heredado)
        cliente.setNombre(req.nombre());
        cliente.setGenero(req.genero());
        cliente.setEdad(req.edad());
        cliente.setIdentificacion(req.identificacion());
        cliente.setDireccion(req.direccion());
        cliente.setTelefono(req.telefono());

        // Cliente (propio)
        cliente.setClientId(req.clientKey());
        cliente.setContrasena(req.password());
        cliente.setEstado(req.estado());

        Cliente saved = clienteRepository.save(cliente);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public ClienteResponse obtener(Long id) {
        Cliente c = clienteRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Cliente no encontrado"));
        return toResponse(c);
    }

    @Transactional
    public ClienteResponse actualizar(Long id, ClienteUpdateRequest req) {
        Cliente c = clienteRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Cliente no encontrado"));

        c.setNombre(req.nombre());
        c.setGenero(req.genero());
        c.setEdad(req.edad());
        c.setDireccion(req.direccion());
        c.setTelefono(req.telefono());
        c.setEstado(req.estado());

        Cliente saved = clienteRepository.save(c);
        return toResponse(saved);
    }

    @Transactional
    public void eliminar(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new BusinessException("Cliente no encontrado");
        }
        clienteRepository.deleteById(id);
    }

    private ClienteResponse toResponse(Cliente c) {
        return new ClienteResponse(
                c.getId(),
                c.getNombre(),
                c.getGenero(),
                c.getEdad(),
                c.getIdentificacion(),
                c.getDireccion(),
                c.getTelefono(),
                c.getClientId(),
                c.getEstado()
        );
    }

    @Transactional(readOnly = true)
    public java.util.List<ClienteResponse> listar() {
        return clienteRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }
}