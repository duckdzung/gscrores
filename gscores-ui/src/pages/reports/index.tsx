import { useEffect, useMemo, useState } from 'react';

import { Header } from '@/components/layout/header';
import { Main } from '@/components/layout/main';
import { getScoreStatistics, getTopAGroupStudents } from '@/services';
import { SubjectStatisticsData, TopStudentData } from '@/types';
import Layout from '../layout';
import { ReportContent } from './components/ReportContent';
import { ReportPageSkeleton } from './components/ReportPageSkeleton';

export default function Reports() {
    const [currentSubjectCode, setCurrentSubjectCode] = useState<string | undefined>(undefined);
    const [allSubjectStats, setAllSubjectStats] = useState<SubjectStatisticsData[]>([]);
    const [topStudents, setTopStudents] = useState<TopStudentData[]>([]);
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    const selectSubjectMap = useMemo(() => {
        const map = new Map<string, string>();
        allSubjectStats.forEach((stat) => {
            map.set(stat.subjectCode, stat.subjectName);
        });
        return map;
    }, [allSubjectStats]);

    useEffect(() => {
        const fetchData = async () => {
            setIsLoading(true);
            setError(null);
            try {
                const stats = await getScoreStatistics();
                setAllSubjectStats(stats);
                if (stats.length > 0 && !currentSubjectCode) {
                    setCurrentSubjectCode(stats[0].subjectCode);
                }

                const top = await getTopAGroupStudents();
                setTopStudents(top);
            } catch (err: any) {
                console.error('Failed to fetch report data:', err);
                let errorMessage = 'An unknown error occurred while fetching report data.';
                if (err.response && err.response.data && typeof err.response.data.message === 'string') {
                    errorMessage = err.response.data.message;
                } else if (typeof err.message === 'string') {
                    errorMessage = err.message;
                }
                setError(errorMessage);
            }
            setIsLoading(false);
        };

        fetchData();
    }, []);

    if (isLoading) {
        return <ReportPageSkeleton />;
    }

    if (error) {
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
                    <p className="text-red-500">Error: {error}</p>
                </Main>
            </Layout>
        );
    }

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
                <ReportContent
                    currentSubjectCode={currentSubjectCode}
                    allSubjectStats={allSubjectStats}
                    topStudents={topStudents}
                    selectSubjectMap={selectSubjectMap}
                    onSubjectChange={setCurrentSubjectCode}
                />
            </Main>
        </Layout>
    );
}
