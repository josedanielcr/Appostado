import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICompetidor } from '../competidor.model';
import { AzureBlobStorageService } from '../../../services/azure-blob-storage/azure-blob-storage.service';

@Component({
  selector: 'jhi-competidor-detail',
  templateUrl: './competidor-detail.component.html',
})
export class CompetidorDetailComponent implements OnInit {
  competidor: ICompetidor | null = null;

  constructor(protected activatedRoute: ActivatedRoute, protected imagenService: AzureBlobStorageService) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ competidor }) => {
      this.competidor = competidor;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
