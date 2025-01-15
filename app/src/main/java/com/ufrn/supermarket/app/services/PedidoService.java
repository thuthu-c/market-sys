package com.ufrn.supermarket.app.services;

import com.ufrn.supermarket.app.dtos.ClienteDTO;
import com.ufrn.supermarket.app.dtos.PedidoDTO;
import com.ufrn.supermarket.app.dtos.ProdutoDTO;
import com.ufrn.supermarket.app.entities.ClienteEntity;
import com.ufrn.supermarket.app.entities.PedidoEntity;
import com.ufrn.supermarket.app.entities.ProdutoEntity;
import com.ufrn.supermarket.app.repositories.PedidoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ProdutoService produtoService;
    private final ClienteService clienteService;

    private PedidoDTO convertToDto(PedidoEntity pedidoEntity) {
        // Mapeando os produtos corretamente de ProdutoEntity para ProdutoDTO
        List<ProdutoDTO> produtosDTO = pedidoEntity.getProdutos().stream()
                .map(produtoEntity -> produtoService.convertToDto(produtoEntity)) // Convertendo ProdutoEntity para
                                                                                  // ProdutoDTO
                .collect(Collectors.toList());

        // Convertendo o cliente (ClienteEntity para ClienteDTO)
        ClienteDTO clienteDTO = clienteService.convertToDto(pedidoEntity.getCliente());

        // Criando o PedidoDTO e retornando
        return new PedidoDTO(
                pedidoEntity.getId(),
                pedidoEntity.getCodigo(),
                produtosDTO, // Lista de ProdutoDTO
                clienteDTO, // ClienteDTO
                pedidoEntity.isAtivo() // Atributo 'ativo' de PedidoEntity
        );
    }

    private List<ProdutoEntity> convertProdutoDTOsToEntities(List<ProdutoDTO> produtoDTOs) {
        return produtoDTOs.stream()
                .map(produtoService::convertToEntity)
                .collect(Collectors.toList());
    }

    private PedidoEntity convertToEntity(PedidoDTO pedidoDTO) {
        PedidoEntity pedidoEntity = new PedidoEntity();

        pedidoEntity.setCodigo(pedidoDTO.codigo());
        pedidoEntity.setAtivo(pedidoDTO.ativo());

        List<ProdutoEntity> produtos = pedidoDTO.produtos().stream()
                .map(produtoService::convertToEntity)
                .collect(Collectors.toList());
        pedidoEntity.setProdutos(produtos);

        ClienteEntity cliente = clienteService.findById(pedidoDTO.cliente().id())
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado: " + pedidoDTO.cliente().id()));
        pedidoEntity.setCliente(cliente);

        return pedidoEntity;
    }

    public PedidoService(PedidoRepository pedidoRepository, ProdutoService produtoService,
            ClienteService clienteService) {
        this.pedidoRepository = pedidoRepository;
        this.produtoService = produtoService;
        this.clienteService = clienteService;
    }

    public List<PedidoDTO> getAll() {
        return pedidoRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Optional<PedidoEntity> getById(Long id) {
        return pedidoRepository.findById(id);
    }

    public PedidoDTO postPedido(PedidoDTO pedidoDTO) {

        // Verifica se o cliente existe
        ClienteEntity cliente = clienteService.findById(pedidoDTO.cliente().id())
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));

        // Verifica se todos os produtos existem
        List<Long> idsProdutos = pedidoDTO.produtos().stream()
                .map(ProdutoDTO::id)
                .collect(Collectors.toList());

        List<ProdutoEntity> produtos = produtoService.findByIds(idsProdutos);

        if (produtos.size() != idsProdutos.size()) {
            List<Long> idsNaoEncontrados = idsProdutos.stream()
                    .filter(id -> produtos.stream().noneMatch(produto -> produto.getId().equals(id)))
                    .collect(Collectors.toList());
            throw new IllegalArgumentException("Produtos não encontrados: " + idsNaoEncontrados);
        }

        // Converte o DTO para a entidade
        PedidoEntity pedidoEntity = convertToEntity(pedidoDTO);

        // Define o cliente e os produtos no pedido
        pedidoEntity.setCliente(cliente);
        pedidoEntity.setProdutos(produtos);

        // Salva o pedido
        PedidoEntity savedPedido = pedidoRepository.save(pedidoEntity);

        return convertToDto(savedPedido);
    }

    public PedidoDTO putPedido(Long id, PedidoDTO pedidoDTOAtualizado) {
        return pedidoRepository.findById(id)
                .map(existingPedido -> {

                    if (pedidoDTOAtualizado.codigo() != null) {
                        existingPedido.setCodigo(pedidoDTOAtualizado.codigo());
                    }

                    if (pedidoDTOAtualizado.produtos() != null) {

                        List<ProdutoEntity> produtosAtualizados = convertProdutoDTOsToEntities(
                                pedidoDTOAtualizado.produtos());
                        existingPedido.setProdutos(produtosAtualizados);
                    }

                    if (pedidoDTOAtualizado.cliente() != null) {

                        ClienteEntity clienteAtualizado = convertClienteDTOToEntity(pedidoDTOAtualizado.cliente());
                        existingPedido.setCliente(clienteAtualizado);
                    }

                    PedidoEntity pedidoAtualizado = pedidoRepository.save(existingPedido);

                    return convertToDto(pedidoAtualizado);
                }).orElse(null);
    }

    // Método para converter ClienteDTO para ClienteEntity
    private ClienteEntity convertClienteDTOToEntity(ClienteDTO clienteDTO) {
        ClienteEntity clienteEntity = new ClienteEntity();
        clienteEntity.setId(clienteDTO.id());
        clienteEntity.setNome(clienteDTO.nome());
        clienteEntity.setCpf(clienteDTO.cpf());
        clienteEntity.setGenero(clienteDTO.genero());
        clienteEntity.setDataNascimento(clienteDTO.dataNascimento());
        clienteEntity.setAtivo(clienteDTO.ativo());
        return clienteEntity;
    }

    public void deletePedido(Long id) {
        if (pedidoRepository.existsById(id)) {
            pedidoRepository.deleteById(id);
        }
    }

    public void deleteLogic(Long id) {
        pedidoRepository.findById(id).ifPresent(existingPedido -> {

            existingPedido.setAtivo(false);

            pedidoRepository.save(existingPedido);
        });
    }

    public PedidoDTO adicionarProduto(Long pedidoId, ProdutoDTO produtoDTO) {

        PedidoEntity pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new EntityNotFoundException("Pedido com ID " + pedidoId + " não encontrado."));

        ProdutoDTO produto = produtoService.findById(produtoDTO.id())
                .orElseThrow(
                        () -> new EntityNotFoundException("Produto com ID " + produtoDTO.id() + " não encontrado."));

        if (pedido.getProdutos().contains(produto)) {
            throw new IllegalArgumentException("Produto já está no pedido.");
        }

        ProdutoEntity produtoEntity = produtoService.convertToEntity(produto);

        pedido.adicionarProduto(produtoEntity);

        PedidoEntity pedidoAtualizado = pedidoRepository.save(pedido);

        return convertToDto(pedidoAtualizado);
    }

    public PedidoDTO removerProduto(Long pedidoId, Long produtoId) {

        return pedidoRepository.findById(pedidoId)
                .map(existingPedido -> {

                    ProdutoEntity produtoARemover = existingPedido.getProdutos()
                            .stream()
                            .filter(produto -> produto.getId().equals(produtoId))
                            .findFirst()
                            .orElseThrow(() -> new EntityNotFoundException(
                                    "Produto com ID " + produtoId + " não encontrado no pedido."));

                    existingPedido.getProdutos().remove(produtoARemover);

                    PedidoEntity pedidoAtualizado = pedidoRepository.save(existingPedido);

                    return convertToDto(pedidoAtualizado);
                })
                .orElseThrow(() -> new EntityNotFoundException("Pedido com ID " + pedidoId + " não encontrado."));
    }
}