import { Component, OnInit } from '@angular/core'
import{Router, ActivatedRoute, Params}from '@angular/router'
import { Cliente } from '../cliente'
import { ClientesService } from '../../clientes.service'
import { HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-clientes-form',
  templateUrl: './clientes-form.component.html',
  styleUrls: ['./clientes-form.component.css']
})
export class ClientesFormComponent implements OnInit {

  cliente?: Cliente;
  success: boolean = false;
  id: number;

  constructor (
    private service : ClientesService,
    private router: Router,
    private activatedRoute : ActivatedRoute
   ) { this.cliente = new Cliente();}

  ngOnInit(): void {
    let params : Observable<Params> = this.activatedRoute.params
    params.subscribe( urlParams => {
      this.id = urlParams['id']
      if(this.id){
      this.service
      .getClienteById(this.id)
      .subscribe(
        response => this.cliente = response,
        errorResponse => this.cliente = new Cliente())
      }
    })
  
  }
    voltarParaListagem(){
        this.router.navigate(['/clientes/lista'])
    }
  onSubmit(){
    if(this.id){
      this.service.atualizar(this.cliente)
      .subscribe(response => {
        this.success = true;
      })
    }
    else{
      this.service
        .salvar(this.cliente)
          .subscribe(response => {
            this.success = true;
            this.cliente=response;
          })
    }
  }
}
