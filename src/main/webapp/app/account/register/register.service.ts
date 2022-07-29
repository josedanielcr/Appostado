import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { Registration } from './register.model';

@Injectable({ providedIn: 'root' })
export class RegisterService {
  constructor(private http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  save(registration: Registration): Observable<{}> {
    console.log(registration);
    registration.imageUrl = 'https://storagepentaware.blob.core.windows.net/pentaware/perfil.png';
    registration.activationURL = this.applicationConfigService.getURLFor('account/activate');
    return this.http.post(this.applicationConfigService.getEndpointFor('api/register'), registration);
  }
}
