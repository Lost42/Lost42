import { createBrowserRouter } from 'react-router-dom';
import routerData from './data/routerData';
import AuthCheck from './layout/AuthCheck';

const router = createBrowserRouter(
  routerData.map(config => {
    if (config.withAuth) {
      return {
        path: config.path,
        element: <AuthCheck>{config.element}</AuthCheck>,
      };
    }
    return {
      path: config.path,
      element: config.element,
    };
  }),
);

export default router;
