import Cookies from 'js-cookie';
import { cn } from '@/lib/utils';
import { SidebarProvider } from '@/components/ui/sidebar';
import { AppSidebar } from '@/components/layout/app-sidebar';

interface LayoutProps {
    children: React.ReactNode;
}

export default function Layout({ children }: LayoutProps) {
    const defaultOpen = Cookies.get('sidebar:state') !== 'false';
    return (
        <SidebarProvider defaultOpen={defaultOpen}>
            <AppSidebar />
            <div
                id="content"
                className={cn(
                    'max-w-full w-full ml-auto',
                    'peer-data-[state=collapsed]:w-[calc(100%-var(--sidebar-width-icon))]',
                    'peer-data-[state=expanded]:w-[calc(100%-var(--sidebar-width))]',
                    'transition-[width] ease-linear duration-200',
                    'h-svh flex flex-col',
                )}
            >
                {children}
            </div>
        </SidebarProvider>
    );
}
