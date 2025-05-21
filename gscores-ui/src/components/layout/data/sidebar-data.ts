import { IconSearch, IconLayoutDashboard, IconSettings, IconReport } from '@tabler/icons-react';
import { Command } from 'lucide-react';
import { type SidebarData } from '../types';
export const sidebarData: SidebarData = {
    teams: [
        {
            name: 'G-Scores',
            logo: Command,
            plan: 'Golden Owl Solutions',
        },
    ],
    navGroups: [
        {
            title: 'General',
            items: [
                {
                    title: 'Dashboard',
                    url: '/',
                    icon: IconLayoutDashboard,
                },
                {
                    title: 'Search Scores',
                    url: '/search',
                    icon: IconSearch,
                },
                {
                    title: 'Reports',
                    url: '/reports',
                    icon: IconReport,
                },
                {
                    title: 'Settings',
                    url: '/settings',
                    icon: IconSettings,
                },
            ],
        },
    ],
};
