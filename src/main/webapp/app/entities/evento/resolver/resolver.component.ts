import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { IEvento } from '../evento.model';
import { EventoService } from '../service/evento.service';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'jhi-resolver',
  templateUrl: './resolver.component.html',
  styleUrls: ['./resolver.component.scss'],
})
export class ResolverComponent implements OnInit {
  evento: IEvento | null = null;
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    marcador1: [null, [Validators.required]],
    marcador2: [null, [Validators.required]],
  });

  constructor(protected fb: FormBuilder, protected eventoService: EventoService, protected activatedRoute: ActivatedRoute) {
    return;
  }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ evento }) => {
      this.evento = evento;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
