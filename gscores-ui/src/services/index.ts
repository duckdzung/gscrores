import axios from '../lib/customApi';
import type { ApiResponse, StudentDetailsData, SubjectStatisticsData, TopStudentData } from '@/types';

export const getScoresByRegistrationNumber = async (registrationNumber: string): Promise<StudentDetailsData> => {
    const response = await axios.get<ApiResponse<StudentDetailsData>>(`/scores/${registrationNumber}`);
    if (response.data.success) {
        return response.data.data;
    }
    throw new Error(response.data.message || 'Failed to fetch student scores');
};

export const getScoreStatistics = async (): Promise<SubjectStatisticsData[]> => {
    const response = await axios.get<ApiResponse<SubjectStatisticsData[]>>(`/reports/statistics`);
    if (response.data.success) {
        return response.data.data;
    }
    throw new Error(response.data.message || 'Failed to fetch score statistics');
};

export const getTopAGroupStudents = async (limit?: number): Promise<TopStudentData[]> => {
    const params = limit ? { limit } : {};
    const response = await axios.get<ApiResponse<TopStudentData[]>>(`/reports/top-A`, { params });
    if (response.data.success) {
        return response.data.data;
    }
    throw new Error(response.data.message || 'Failed to fetch top A group students');
};
