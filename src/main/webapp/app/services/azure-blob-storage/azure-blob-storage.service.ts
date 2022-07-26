import { Injectable } from '@angular/core';
import { BlobServiceClient, ContainerClient } from '@azure/storage-blob';
import { Observable } from 'rxjs';
import { IQuiniela } from '../../entities/quiniela/quiniela.model';
import { EntityResponseType } from '../../entities/quiniela/service/quiniela.service';
import { HttpClient } from '@angular/common/http';
import { ApplicationConfigService } from '../../core/config/application-config.service';

@Injectable({
  providedIn: 'root',
})
export class AzureBlobStorageService {
  private blobServiceClient: BlobServiceClient;
  private containerClient: ContainerClient;
  private account = 'storagepentaware';
  private containerName = 'pentaware';
  private sasToken =
    '?sv=2021-06-08&ss=bfqt&srt=sco&sp=rwdlacupiytfx&se=2022-09-15T14:47:57Z&st=2022-07-26T06:47:57Z&sip=138.94.57.161&spr=https,http&sig=V4CnbrsI6EBEkr%2Fjb3JsEZGJk3hqYfuc6PJcIeDNrYg%3D';

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {
    this.blobServiceClient = new BlobServiceClient(`https://${this.account}.blob.core.windows.net/?${this.sasToken}`);
    this.containerClient = this.blobServiceClient.getContainerClient(this.containerName);
  }

  createBlobInContainer(file: File, name: string): string {
    const blobClient = this.containerClient.getBlockBlobClient(name);
    const options = { blobHTTPHeaders: { blobContentType: file.type } };
    blobClient.uploadData(file, options).then((r: any) => {
      console.log(r);
    });
    return `https://${this.account}.blob.core.windows.net/${this.containerName}/"${name}`;
  }
}
