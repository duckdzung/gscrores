import { Card, CardContent, CardHeader } from '@/components/ui/card';
import Layout from '@/pages/layout';
import { Header } from '@/components/layout/header';
import { Main } from '@/components/layout/main';

export function ReportPageSkeleton() {
    return (
        <Layout>
            <Header sticky>
                <div className="ml-auto flex items-center space-x-4"></div>
            </Header>
            <Main>
                <div className="mb-2 flex items-center justify-between space-y-2 flex-wrap">
                    <div>
                        <h2 className="text-2xl font-bold tracking-tight">Reports</h2>
                        <p className="text-muted-foreground">
                            View and analyze student performance data with detailed reports.
                        </p>
                    </div>
                </div>

                <div className="grid grid-cols-1 gap-4 lg:grid-cols-7 mt-4">
                    {/* Skeleton for Student Score Distribution Card */}
                    <Card className="col-span-1 lg:col-span-4 bg-sidebar">
                        <CardHeader>
                            <div className="h-6 w-3/4 bg-gray-200 rounded animate-pulse mb-2"></div>
                            <div className="flex justify-end items-center">
                                <div className="h-10 w-28 bg-gray-200 rounded animate-pulse"></div>
                            </div>
                        </CardHeader>
                        <CardContent>
                            <div className="h-64 w-full bg-gray-200 rounded animate-pulse"></div>
                        </CardContent>
                    </Card>

                    {/* Skeleton for Top 10 Group A Students Card */}
                    <Card className="col-span-1 lg:col-span-3 bg-sidebar">
                        <CardHeader>
                            <div className="h-6 w-1/2 bg-gray-200 rounded animate-pulse mb-2"></div>
                            <div className="h-4 w-3/4 bg-gray-200 rounded animate-pulse"></div>
                        </CardHeader>
                        <CardContent className="p-2 space-y-4">
                            {[...Array(3)].map((_, i) => (
                                <div key={i} className="flex items-center gap-4">
                                    <div className="h-6 w-6 bg-gray-200 rounded animate-pulse"></div>
                                    <div className="flex-1 space-y-2">
                                        <div className="h-4 w-3/4 bg-gray-200 rounded animate-pulse"></div>
                                        <div className="h-3 w-1/2 bg-gray-200 rounded animate-pulse"></div>
                                    </div>
                                    <div className="h-4 w-8 bg-gray-200 rounded animate-pulse"></div>
                                </div>
                            ))}
                        </CardContent>
                    </Card>
                </div>
            </Main>
        </Layout>
    );
}
