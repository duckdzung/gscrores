import { createBrowserRouter } from 'react-router-dom';

import Dashboard from './pages/dashboard';
import Search_Score from './pages/search_scores';
import Reports from './pages/reports';
import Settings from './pages/settings';

export const router = createBrowserRouter([
    {
        path: '/settings',
        element: <Settings />,
    },

    {
        path: '/reports',
        element: <Reports />,
    },

    {
        path: '/search',
        element: <Search_Score />,
    },

    {
        path: '/',
        element: <Dashboard />,
    },
]);
