export interface ApiResponse<T> {
    success: boolean;
    message: string;
    data: T;
}

export interface Score {
    subjectName: string;
    subjectCode: string;
    score: number;
}

export interface StudentDetailsData {
    registrationNumber: string;
    foreignLanguageCode: string;
    scores: Score[];
}

export interface ScoreDistribution {
    [level: string]: number;
}

export interface SubjectStatisticsData {
    subjectCode: string;
    subjectName: string;
    scoreDistribution: ScoreDistribution;
}

export interface IndividualScores {
    [subjectCode: string]: number;
}

export interface TopStudentData {
    registrationNumber: string;
    totalScore: number;
    individualScores: IndividualScores;
}
