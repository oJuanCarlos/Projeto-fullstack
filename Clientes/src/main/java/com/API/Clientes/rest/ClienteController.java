package com.API.Clientes.rest;

import com.API.Clientes.model.entity.Cliente;
import com.API.Clientes.model.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/clientes")

public class ClienteController {

    private ClienteRepository repository;

    @Autowired
    public ClienteController(ClienteRepository repository){
        this.repository = repository;
    }

    @GetMapping
    public List<Cliente> obterTodos(){
        return repository.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED ) //Vai mapear o objeto de retorno para o corpo da resposta e vai informar ao json
    public Cliente salvar(@RequestBody @Valid Cliente cliente){
        return repository.save(cliente);
        //Quando for feito um post para api/clientes passando um cliente json
    }
    @GetMapping("{id}")
    public Cliente acharPorId(@PathVariable Integer id){
        return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        //Se ele não conseguir obter o cliente pelo id ele lança esse erro de status
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT) //o NO_CONTENT faz com que o client saiba que o recurso foi deletado no servidor
    public void deletar( @PathVariable Integer id){
        repository
                .findById(id)
                .map(cliente -> {
                    repository.delete(cliente);
                    return Void.TYPE;
                })
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
    @PutMapping("{id}") //Utilizado para atualizar completamente um recurso
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void atualizar(@PathVariable Integer id, @RequestBody Cliente clienteAtualizado){
        repository
            .findById(id)
            .map(cliente -> {
                cliente.setNome(clienteAtualizado.getNome());
                cliente.setCpf(clienteAtualizado.getCpf());
                clienteAtualizado.setId(cliente.getId()); //Tendo o ID setado, os demais dados serão atualizados para o ID
                return repository.save(clienteAtualizado);
            })
            .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}
