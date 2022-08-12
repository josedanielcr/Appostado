import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { INotificacion } from '../../entities/notificacion/notificacion.model';

@Component({
  selector: 'jhi-notification',
  templateUrl: './notification.component.html',
  styleUrls: ['../../layouts/navbar/navbar.component.scss', './notification.component.scss'],
})
export class NotificationComponent implements OnInit {
  @Input() notification: INotificacion;
  @Output() notificationEmitter = new EventEmitter<number>();

  constructor() {
    return;
  }

  ngOnInit(): void {
    return;
  }

  refreshNotifications(notificationId: number | undefined): void {
    this.notificationEmitter.emit(notificationId);
  }
}
