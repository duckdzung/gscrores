import { ScrollArea } from '@/components/ui/scroll-area';
import { IndividualScores, TopStudentData } from '@/types';

const formatIndividualScores = (scores: IndividualScores): string => {
    return Object.entries(scores)
        .map(([subjectCode, score]) => `${subjectCode}: ${score}`)
        .join(', ');
};

export function TopGroupA({ data }: { data: TopStudentData[] }) {
    return (
        <div className="space-y-8">
            <ScrollArea className="h-96">
                {data.map((item, index) => (
                    <div className="flex items-center gap-4 mb-1 p-4 pt-0" key={item.registrationNumber}>
                        <div className="flex-shrink-0 w-6 text-center font-medium">{index + 1}</div>
                        <div className="flex flex-1 flex-wrap items-center justify-between">
                            <div className="space-y-1 flex-grow">
                                <p className="text-[0.7rem] text-muted-foreground">Reg No: {item.registrationNumber}</p>
                                <p className="text-[0.7rem] text-muted-foreground">
                                    Scores: {formatIndividualScores(item.individualScores)}
                                </p>
                            </div>
                            <div className="font-medium ml-2">{item.totalScore}</div>
                        </div>
                    </div>
                ))}
            </ScrollArea>
        </div>
    );
}
