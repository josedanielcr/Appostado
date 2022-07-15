import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'usuario',
        data: { pageTitle: 'appostadoApp.usuario.home.title' },
        loadChildren: () => import('./usuario/usuario.module').then(m => m.UsuarioModule),
      },
      {
        path: 'cuenta-usuario',
        data: { pageTitle: 'appostadoApp.cuentaUsuario.home.title' },
        loadChildren: () => import('./cuenta-usuario/cuenta-usuario.module').then(m => m.CuentaUsuarioModule),
      },
      {
        path: 'opcion-rol',
        data: { pageTitle: 'appostadoApp.opcionRol.home.title' },
        loadChildren: () => import('./opcion-rol/opcion-rol.module').then(m => m.OpcionRolModule),
      },
      {
        path: 'notificacion',
        data: { pageTitle: 'appostadoApp.notificacion.home.title' },
        loadChildren: () => import('./notificacion/notificacion.module').then(m => m.NotificacionModule),
      },
      {
        path: 'amigo',
        data: { pageTitle: 'appostadoApp.amigo.home.title' },
        loadChildren: () => import('./amigo/amigo.module').then(m => m.AmigoModule),
      },
      {
        path: 'liga',
        data: { pageTitle: 'appostadoApp.liga.home.title' },
        loadChildren: () => import('./liga/liga.module').then(m => m.LigaModule),
      },
      {
        path: 'liga-usuario',
        data: { pageTitle: 'appostadoApp.ligaUsuario.home.title' },
        loadChildren: () => import('./liga-usuario/liga-usuario.module').then(m => m.LigaUsuarioModule),
      },
      {
        path: 'producto',
        data: { pageTitle: 'appostadoApp.producto.home.title' },
        loadChildren: () => import('./producto/producto.module').then(m => m.ProductoModule),
      },
      {
        path: 'producto-usuario',
        data: { pageTitle: 'appostadoApp.productoUsuario.home.title' },
        loadChildren: () => import('./producto-usuario/producto-usuario.module').then(m => m.ProductoUsuarioModule),
      },
      {
        path: 'premio',
        data: { pageTitle: 'appostadoApp.premio.home.title' },
        loadChildren: () => import('./premio/premio.module').then(m => m.PremioModule),
      },
      {
        path: 'compra',
        data: { pageTitle: 'appostadoApp.compra.home.title' },
        loadChildren: () => import('./compra/compra.module').then(m => m.CompraModule),
      },
      {
        path: 'canje',
        data: { pageTitle: 'appostadoApp.canje.home.title' },
        loadChildren: () => import('./canje/canje.module').then(m => m.CanjeModule),
      },
      {
        path: 'apuesta',
        data: { pageTitle: 'appostadoApp.apuesta.home.title' },
        loadChildren: () => import('./apuesta/apuesta.module').then(m => m.ApuestaModule),
      },
      {
        path: 'transaccion',
        data: { pageTitle: 'appostadoApp.transaccion.home.title' },
        loadChildren: () => import('./transaccion/transaccion.module').then(m => m.TransaccionModule),
      },
      {
        path: 'apuesta-transaccion',
        data: { pageTitle: 'appostadoApp.apuestaTransaccion.home.title' },
        loadChildren: () => import('./apuesta-transaccion/apuesta-transaccion.module').then(m => m.ApuestaTransaccionModule),
      },
      {
        path: 'mision',
        data: { pageTitle: 'appostadoApp.mision.home.title' },
        loadChildren: () => import('./mision/mision.module').then(m => m.MisionModule),
      },
      {
        path: 'mision-transaccion',
        data: { pageTitle: 'appostadoApp.misionTransaccion.home.title' },
        loadChildren: () => import('./mision-transaccion/mision-transaccion.module').then(m => m.MisionTransaccionModule),
      },
      {
        path: 'mision-usuario',
        data: { pageTitle: 'appostadoApp.misionUsuario.home.title' },
        loadChildren: () => import('./mision-usuario/mision-usuario.module').then(m => m.MisionUsuarioModule),
      },
      {
        path: 'evento',
        data: { pageTitle: 'appostadoApp.evento.home.title' },
        loadChildren: () => import('./evento/evento.module').then(m => m.EventoModule),
      },
      {
        path: 'deporte',
        data: { pageTitle: 'appostadoApp.deporte.home.title' },
        loadChildren: () => import('./deporte/deporte.module').then(m => m.DeporteModule),
      },
      {
        path: 'division',
        data: { pageTitle: 'appostadoApp.division.home.title' },
        loadChildren: () => import('./division/division.module').then(m => m.DivisionModule),
      },
      {
        path: 'division-competidor',
        data: { pageTitle: 'appostadoApp.divisionCompetidor.home.title' },
        loadChildren: () => import('./division-competidor/division-competidor.module').then(m => m.DivisionCompetidorModule),
      },
      {
        path: 'competidor',
        data: { pageTitle: 'appostadoApp.competidor.home.title' },
        loadChildren: () => import('./competidor/competidor.module').then(m => m.CompetidorModule),
      },
      {
        path: 'quiniela',
        data: { pageTitle: 'appostadoApp.quiniela.home.title' },
        loadChildren: () => import('./quiniela/quiniela.module').then(m => m.QuinielaModule),
      },
      {
        path: 'parametro',
        data: { pageTitle: 'appostadoApp.parametro.home.title' },
        loadChildren: () => import('./parametro/parametro.module').then(m => m.ParametroModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
