import { Header } from '@/components/layout/header';
import { Main } from '@/components/layout/main';
import Layout from '../layout';
export default function Dashboard() {
    return (
        <Layout>
            {/* ===== Top Heading ===== */}
            <Header sticky>
                {/* <Search /> */}
                <div className="ml-auto flex items-center space-x-4"></div>
            </Header>
            {/* ===== Main ===== */}
            <Main>
                <div className="mb-2 flex items-center justify-between space-y-2 flex-wrap">
                    <div>
                        <h2 className="text-2xl font-bold tracking-tight">Dashboard</h2>
                        <p className="text-muted-foreground">Dashboard</p>
                    </div>
                </div>
            </Main>
        </Layout>
    );
}
