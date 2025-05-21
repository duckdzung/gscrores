import { useState } from 'react';
import { Header } from '@/components/layout/header';
import { Main } from '@/components/layout/main';
import Layout from '../layout';
import { getScoresByRegistrationNumber } from '@/services';
import { StudentDetailsData } from '@/types';
import { SearchForm, SearchFormValues } from './components/SearchForm';
import { SearchResults } from './components/SearchResults';

export default function Search_Score() {
    const [studentDetails, setStudentDetails] = useState<StudentDetailsData | null>(null);
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);

    async function handleSearchSubmit(data: SearchFormValues) {
        setIsLoading(true);
        setError(null);
        try {
            const responseData = await getScoresByRegistrationNumber(data.registrationNumber);
            setStudentDetails(responseData);
        } catch (err: any) {
            console.error('Failed to fetch student scores:', err);
            let errorMessage = 'An unknown error occurred while fetching student scores.';
            if (err.response && err.response.data && typeof err.response.data.message === 'string') {
                errorMessage = err.response.data.message;
            } else if (typeof err.message === 'string') {
                errorMessage = err.message;
            }
            setError(errorMessage);
            setStudentDetails(null);
        }
        setIsLoading(false);
    }

    return (
        <Layout>
            <Header sticky>
                <div className="ml-auto flex items-center space-x-4"></div>
            </Header>
            <Main>
                <div className="mb-2 flex items-center justify-between space-y-2 flex-wrap">
                    <div>
                        <h2 className="text-2xl font-bold tracking-tight">Search Score</h2>
                        <p className="text-muted-foreground">Quickly search and check scores with accuracy!</p>
                    </div>
                </div>
                <div className="w-full">
                    <SearchForm onSubmit={handleSearchSubmit} isLoading={isLoading} />
                    <SearchResults studentDetails={studentDetails} isLoading={isLoading} error={error} />
                </div>
            </Main>
        </Layout>
    );
}
