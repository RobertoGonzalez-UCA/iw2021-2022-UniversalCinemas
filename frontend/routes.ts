import { Route } from '@vaadin/router';
import './views/main-layout';
import './views/registro/registro-view';

export type ViewRoute = Route & {
  title?: string;
  icon?: string;
  children?: ViewRoute[];
};

export const views: ViewRoute[] = [
  // place routes below (more info https://vaadin.com/docs/latest/fusion/routing/overview)
  {
    path: '',
    component: 'registro-view',
    icon: '',
    title: '',
  },
  {
    path: 'signup',
    component: 'registro-view',
    icon: 'la la-user',
    title: 'Registro',
  },
  {
    path: 'login',
    component: 'inicio-sesión-view',
    icon: 'la la-user',
    title: 'Inicio Sesión',
    action: async (_context, _command) => {
      await import('./views/iniciosesión/inicio-sesión-view');
      return;
    },
  },
  {
    path: 'news',
    component: 'novedades-view',
    icon: 'la la-newspaper',
    title: 'Novedades',
    action: async (_context, _command) => {
      await import('./views/novedades/novedades-view');
      return;
    },
  },
  {
    path: 'plans',
    component: 'planes-view',
    icon: 'la la-th-list',
    title: 'Planes',
    action: async (_context, _command) => {
      await import('./views/planes/planes-view');
      return;
    },
  },
  {
    path: 'movies',
    component: 'películas-view',
    icon: 'la la-video',
    title: 'Películas',
    action: async (_context, _command) => {
      await import('./views/películas/películas-view');
      return;
    },
  },
  {
    path: 'chooseseat',
    component: 'elegirasiento-view',
    icon: 'la la-chair',
    title: 'Elegir asiento',
    action: async (_context, _command) => {
      await import('./views/elegirasiento/elegirasiento-view');
      return;
    },
  },
  {
    path: 'checkout',
    component: 'comprarentrada-view',
    icon: 'la la-shopping-cart',
    title: 'Comprar entrada',
    action: async (_context, _command) => {
      await import('./views/comprarentrada/comprarentrada-view');
      return;
    },
  },
];
export const routes: ViewRoute[] = [
  {
    path: '',
    component: 'main-layout',
    children: [...views],
  },
];
