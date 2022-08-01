import { Injectable } from '@angular/core';
import { BlobServiceClient, ContainerClient } from '@azure/storage-blob';
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
    '?sv=2021-06-08&ss=bfqt&srt=sco&sp=rwdlacupiytfx&se=2022-09-22T05:19:37Z&st=2022-07-27T21:19:37Z&sip=0.0.0.0-255.255.255.255&spr=https,http&sig=3Ei5XThmSKX08XXQwriObM2DiAJw%2BPQH28776h1xLlA%3D';

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
    return `https://${this.account}.blob.core.windows.net/${this.containerName}/${name}`;
  }
}
