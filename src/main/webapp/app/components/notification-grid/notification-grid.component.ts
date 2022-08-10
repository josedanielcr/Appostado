import { Component, OnInit } from '@angular/core';
import { NotificacionService } from '../../entities/notificacion/service/notificacion.service';
import { HttpResponse } from '@angular/common/http';
import { INotificacion } from '../../entities/notificacion/notificacion.model';

@Component({
  selector: 'jhi-notification-grid',
  templateUrl: './notification-grid.component.html',
  styleUrls: ['./notification-grid.component.scss'],
})
export class NotificationGridComponent implements OnInit {
  public notifications: INotificacion[] | null | undefined;
  public errorMessage = '';

  constructor(private notificationService: NotificacionService) {}

  ngOnInit(): void {
    this.getNotiticationsByUser();
  }

  getNotiticationsByUser(): void {
    this.notificationService.findByUsuario().subscribe({
      next: (res: HttpResponse<INotificacion[]>) => {
        this.notifications = res.body;
        console.log(this.notifications);
      },
      error: () => {
        this.errorMessage = 'Error al cargar notificaciones, intente nuevamente';
      },
    });
  }

  public updateNotifications(notificationId: number | undefined): void {
    const internalNotification = this.notifications?.find(notification => (notification.id = notificationId));
    this.notificationService.updateReadNotification(internalNotification?.id, internalNotification!).subscribe({
      next: () => {
        this.getNotiticationsByUser();
      },
    });
  }
}
