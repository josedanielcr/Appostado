import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ResetObject } from './password-reset-init.model';

import { ApplicationConfigService } from 'app/core/config/application-config.service';

@Injectable({ providedIn: 'root' })
export class PasswordResetInitService {
  constructor(private http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  save(resetObject: ResetObject): Observable<{}> {
    resetObject.resetURL = this.applicationConfigService.getURLFor('account/reset/finish');
    return this.http.post(this.applicationConfigService.getEndpointFor('api/account/reset-password/init'), resetObject);
  }
}
