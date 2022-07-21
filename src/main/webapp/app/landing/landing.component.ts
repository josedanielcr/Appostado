import { Component, OnInit } from '@angular/core';
import AOS from 'aos';

@Component({
  selector: 'jhi-landing',
  templateUrl: './landing.component.html',
  styleUrls: ['./landing.component.css'],
})
export class LandingComponent implements OnInit {
  constructor() {
    return;
  }

  ngOnInit(): void {
    AOS.init();
  }
}
