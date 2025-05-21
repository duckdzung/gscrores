import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card';
import { Table, TableBody, TableCell, TableRow, TableHeader } from '@/components/ui/table';
import { StudentDetailsData } from '@/types';

interface SearchResultsProps {
    studentDetails: StudentDetailsData | null;
    isLoading: boolean;
    error: string | null;
}

export function SearchResults({ studentDetails, isLoading, error }: SearchResultsProps) {
    return (
        <Card className="w-full mt-6 bg-sidebar">
            <CardHeader>
                <CardTitle>Detailed Scores</CardTitle>
                <CardDescription>
                    {studentDetails ? (
                        `Scores for ${studentDetails.registrationNumber}`
                    ) : isLoading ? (
                        <span className="h-4 w-48 bg-gray-200 rounded animate-pulse inline-block"></span>
                    ) : (
                        'Detailed view of search scores here !'
                    )}
                    {studentDetails?.foreignLanguageCode && ` - Language Code: ${studentDetails.foreignLanguageCode}`}
                </CardDescription>
            </CardHeader>

            {isLoading && (
                <CardContent className="space-y-4">
                    <div className="h-8 w-full bg-gray-200 rounded animate-pulse"></div> {/* Table Header Skeleton */}
                    {[...Array(3)].map((_, i) => (
                        <div
                            key={i}
                            className="h-10 w-full bg-gray-200 rounded animate-pulse"
                        ></div> /* Table Row Skeleton */
                    ))}
                </CardContent>
            )}

            {error && !isLoading && (
                <CardContent>
                    <p className="text-red-500">Error: {error}</p>
                </CardContent>
            )}

            {!isLoading && !error && studentDetails && studentDetails.scores && studentDetails.scores.length > 0 ? (
                <CardContent>
                    <Table>
                        <TableHeader className="text-gray-500 font-semibold">
                            <TableRow>
                                <TableCell>Subject Code</TableCell>
                                <TableCell>Subject Name</TableCell>
                                <TableCell>Score</TableCell>
                            </TableRow>
                        </TableHeader>
                        <TableBody>
                            {studentDetails.scores.map((score) => (
                                <TableRow key={score.subjectCode}>
                                    <TableCell>{score.subjectCode}</TableCell>
                                    <TableCell>{score.subjectName}</TableCell>
                                    <TableCell>{score.score}</TableCell>
                                </TableRow>
                            ))}
                        </TableBody>
                    </Table>
                </CardContent>
            ) : (
                !isLoading &&
                !error && (
                    <CardContent>
                        <p className="text-center text-lg font-semibold">
                            {studentDetails && studentDetails.scores && studentDetails.scores.length === 0
                                ? 'No scores found for this student.'
                                : 'No data found or search not performed yet.'}
                        </p>
                    </CardContent>
                )
            )}
        </Card>
    );
}
