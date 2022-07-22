import { Component, OnInit } from '@angular/core';
import { AccountService } from '../../core/auth/account.service';
import { Router } from '@angular/router';

@Component({
  selector: 'jhi-panel',
  templateUrl: './panel.component.html',
  styleUrls: ['./panel.component.scss'],
})
export class PanelComponent implements OnInit {
  constructor(private accountService: AccountService, private router: Router) {
    return;
  }

  ngOnInit(): void {
    this.accountService.identity().subscribe(() => {
      if (!this.accountService.isAuthenticated()) {
        this.router.navigate(['/landing']);
      }
    });
  }
}
