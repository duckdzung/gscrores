import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card';
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '@/components/ui/select';
import type { SubjectStatisticsData, TopStudentData } from '@/types';
import { Statistics } from './Statistics';
import { TopGroupA } from './TopGroupA';

interface ReportContentProps {
    currentSubjectCode: string | undefined;
    allSubjectStats: SubjectStatisticsData[];
    topStudents: TopStudentData[];
    selectSubjectMap: Map<string, string>;
    onSubjectChange: (value: string) => void;
}

const levelNameMapping: Record<string, string> = {
    level1: '0 - 3.99 points',
    level2: '4 - 5.99 points',
    level3: '6 - 7.99 points',
    level4: '8 - 10 points',
};

const preferredLevelOrder = ['< 4 points', '4 - <6 points', '6 - <8 points', '>= 8 points'];

export function ReportContent({
    currentSubjectCode,
    allSubjectStats,
    topStudents,
    selectSubjectMap,
    onSubjectChange,
}: ReportContentProps) {
    const currentSubjectStats = allSubjectStats.find((s) => s.subjectCode === currentSubjectCode);

    const scoreDistributionData = currentSubjectStats?.scoreDistribution
        ? Object.entries(currentSubjectStats.scoreDistribution)
              .map(([levelKey, count]) => ({
                  name: levelNameMapping[levelKey] || levelKey,
                  count,
              }))
              .sort((a, b) => {
                  const indexA = preferredLevelOrder.indexOf(a.name);
                  const indexB = preferredLevelOrder.indexOf(b.name);
                  if (indexA === -1) return 1;
                  if (indexB === -1) return -1;
                  return indexA - indexB;
              })
        : [];

    return (
        <div className="grid grid-cols-1 gap-4 lg:grid-cols-7 mt-4">
            <Card className="col-span-1 lg:col-span-4 bg-sidebar">
                <CardHeader>
                    <CardTitle>Student Score Distribution</CardTitle>
                    <div className="flex justify-end items-center">
                        <Select value={currentSubjectCode} onValueChange={onSubjectChange}>
                            <SelectTrigger className="w-auto min-w-[120px]">
                                <SelectValue placeholder="Select Subject">
                                    {currentSubjectCode ? selectSubjectMap.get(currentSubjectCode) : 'Select Subject'}
                                </SelectValue>
                            </SelectTrigger>
                            <SelectContent>
                                {Array.from(selectSubjectMap.entries()).map(([code, name]) => (
                                    <SelectItem key={code} value={code}>
                                        {name}
                                    </SelectItem>
                                ))}
                            </SelectContent>
                        </Select>
                    </div>
                </CardHeader>
                <CardContent>
                    <Statistics data={scoreDistributionData} />
                </CardContent>
            </Card>
            <Card className="col-span-1 lg:col-span-3 bg-sidebar">
                <CardHeader>
                    <CardTitle>Top 10 Group A Students</CardTitle>
                    <CardDescription>Ranking the Top Students in Group A</CardDescription>
                </CardHeader>
                <CardContent className="p-2">
                    <TopGroupA data={topStudents} />
                </CardContent>
            </Card>
        </div>
    );
}
